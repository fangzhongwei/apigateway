package com.lawsofnature.apigateway.invoker

import java.lang.reflect.{InvocationTargetException, Method, Parameter}
import java.util

import RpcSSO.SessionResponse
import akka.http.scaladsl.model.HttpHeader
import com.lawsofnature.apigateway.annotations.{ApiMapping, Param}
import com.lawsofnature.apigateway.conext.SessionContext
import com.lawsofnature.apigateway.enumerate.ParamSource
import com.lawsofnature.apigateway.helper.Constants
import com.lawsofnature.apigateway.response.ApiResponse
import com.lawsofnature.apigateway.server.HttpService
import com.lawsofnature.apigateway.service.SessionService
import com.lawsofnature.apigateway.validate.Validator
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.exception.{ErrorCode, ServiceException}
import com.lawsofnature.common.helper.{JsonHelper, RegHelper}
import org.apache.commons.lang.StringUtils
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.immutable.Seq
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/11/28.
  */
object ActionInvoker {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  implicit val timeout = (90 seconds)

  val HEADER_TRACE_ID = "TI"
  val HEADER_ACTION_ID = "AI"
  val HEADER_TOKEN = "TK"

  class ActionMethodParamAttribute(val source: ParamSource,
                                   val name: String,
                                   val classType: Class[_],
                                   val required: Boolean,
                                   val mask: String,
                                   val minLength: Int,
                                   val maxLength: Int,
                                   val min: Int,
                                   val max: Int,
                                   val errorCode: ErrorCode)

  private val apiMap: mutable.Map[Int, (Method, Class[_], ApiMapping, scala.collection.mutable.Seq[ActionMethodParamAttribute])] = scala.collection.mutable.Map()
  //  private val apiCheckMap: mutable.Map[Int, (Method, Class[_], ApiMapping)] = scala.collection.mutable.Map()

  def initActionMap: Unit = {
    val classList: util.ArrayList[Class[_]] = HttpService.actionBeanClassList
    val iterator: util.Iterator[Class[_]] = classList.iterator()
    var clazz: Class[_] = null
    while (iterator.hasNext) {
      clazz = iterator.next()
      val declaredMethods: Array[Method] = clazz.getDeclaredMethods
      for (i <- 0 to declaredMethods.length - 1) {
        val method: Method = declaredMethods(i)
        val apiMapping: ApiMapping = method.getAnnotation[ApiMapping](classOf[ApiMapping])
        if (apiMapping != null) {
          val types: Array[Class[_]] = method.getParameterTypes
          apiMap.get(apiMapping.id()) match {
            case Some(tuple) =>
              logger.error(s"api ${apiMapping.id()} conflict: ${tuple._1.toGenericString}, ${method.toGenericString}, system will shutdown ...")
              System.exit(1)
            case None =>
              var actionMethodParamAttributeSeq: scala.collection.mutable.Seq[ActionMethodParamAttribute] = scala.collection.mutable.Seq[ActionMethodParamAttribute]()
              val parameters: Array[Parameter] = method.getParameters
              parameters.length match {
                case 0 => //actionMethodParamAttributeSeq = actionMethodParamAttributeSeq :+ None
                case _ => parameters.foreach {
                  p =>
                    val pv: Param = p.getAnnotation(classOf[Param])
                    (null == pv) match {
                      case true =>
                        logger.error(s"${p} has not Param Annotation, please add. system will shutdown ...")
                        System.exit(1)
                      case false =>
                        if (pv.source() != ParamSource.BODY && StringUtils.isBlank(pv.name())) {
                          logger.error(s"${pv.source} Param must has a non-blank name, method:$method, Param:$pv. system will shutdown ...")
                          System.exit(1)
                        }
                        actionMethodParamAttributeSeq = actionMethodParamAttributeSeq :+ new ActionMethodParamAttribute(pv.source(), pv.name(), p.getType, pv.required(), pv.mask(), pv.minLength(), pv.maxLength(), pv.min(), pv.max(), pv.error())
                    }
                }
              }
              println(actionMethodParamAttributeSeq)

              if (actionMethodParamAttributeSeq.filter(_.source == ParamSource.BODY).length >= 2) {
                logger.error(s"There can only be one body source param in a action method, method: ${method.toGenericString}. now system will shutdown ...")
                System.exit(1)
              }

              apiMap += (apiMapping.id() -> (method, types(actionMethodParamAttributeSeq.zipWithIndex.filter(_._1.source == ParamSource.BODY).head._2), apiMapping, actionMethodParamAttributeSeq))
          }
        }
      }
    }
  }

  def extractValueFromHeaders(name: String, headers: Seq[HttpHeader]): Option[String] = {
    headers.filter(h => h.name().equals(name)).headOption match {
      case Some(header) => Some(header.value())
      case None => None
    }
  }

  val EMPTY = ""

  def obtainParamValues(actionMethodParamAttributeSeq: mutable.Seq[ActionMethodParamAttribute], headers: Seq[HttpHeader], parameterMap: Map[String, String], body: String, salt: String, ignoreEDecrypt: Boolean, parseClass: Class[_]): Array[AnyRef] = {
    var strValue: String = null
    var intValue: Int = 0
    var longValue: Long = 0L
    var minLength = -1
    var maxLength = -1
    var min = -1
    var max = -1

    var list: scala.collection.mutable.ListBuffer[AnyRef] = scala.collection.mutable.ListBuffer[AnyRef]()

    actionMethodParamAttributeSeq.foreach {
      attr =>
        val anyRef: AnyRef = {
          val name: String = attr.name
          fieldType(attr.classType) match {
            case 1 =>
              strValue = attr.source match {
                case ParamSource.HEADER => extractValueFromHeaders(name, headers) match {
                  case Some(value) => value
                  case None => EMPTY
                }
                case ParamSource.PARAM => parameterMap.get(name) match {
                  case Some(value) => value
                  case None => EMPTY
                }
                case _ => throw ServiceException.make(attr.errorCode)
              }
              attr.mask match {
                case "" =>
                  if (attr.required && StringUtils.isBlank(strValue)) throw ServiceException.make(attr.errorCode)
                  //minLength
                  minLength = attr.minLength
                  if (minLength != -1 && minLength > strValue.length) throw ServiceException.make(attr.errorCode)
                  //maxLength
                  maxLength = attr.maxLength
                  if (maxLength != -1 && maxLength < strValue.length) throw ServiceException.make(attr.errorCode)
                case _ =>
                  if (attr.required && StringUtils.isBlank(strValue)) throw ServiceException.make(attr.errorCode)
                  if (StringUtils.isNotBlank(strValue) && !RegHelper.isMatched(strValue, attr.mask)) throw ServiceException.make(attr.errorCode)
              }
              strValue
            case 2 =>
              intValue = attr.source match {
                case ParamSource.HEADER =>
                  extractValueFromHeaders(name, headers) match {
                    case Some(value) => value.toInt
                    case None => 0
                  }
                case ParamSource.PARAM => parameterMap.get(name) match {
                  case Some(value) => value.toInt
                  case None => 0
                }
                case _ => throw ServiceException.make(attr.errorCode)
              }
              min = attr.min
              if (min > intValue) throw ServiceException.make(attr.errorCode)
              max = attr.max
              if (max < intValue) throw ServiceException.make(attr.errorCode)
              intValue.asInstanceOf[AnyRef]
            case 3 =>
              longValue = attr.source match {
                case ParamSource.HEADER =>
                  extractValueFromHeaders(name, headers) match {
                    case Some(value) => value.toLong
                    case None => 0L
                  }
                case ParamSource.PARAM => parameterMap.get(name) match {
                  case Some(value) => value.toLong
                  case None => 0L
                }
                case _ => throw ServiceException.make(attr.errorCode)
              }
              min = attr.min
              if (min > longValue) throw ServiceException.make(attr.errorCode)
              max = attr.max
              if (max < longValue) throw ServiceException.make(attr.errorCode)
              longValue.asInstanceOf[AnyRef]
            case _ =>
              if (attr.required && StringUtils.isBlank(body)) throw ServiceException.make(attr.errorCode)
              val request: AnyRef = if (StringUtils.isNotBlank(body)) parseRequest(body, salt, ignoreEDecrypt, parseClass) else null
              if (request != null) {
                Validator.validate(request) match {
                  case Some(errorCode) => throw ServiceException.make(errorCode)
                  case None =>
                }
              }
              request
          }
        }
        list += anyRef
    }
    list.toArray
  }

  private val STRING: String = "string"
  private val INT: String = "int"
  private val LONG: String = "long"

  private def fieldType(clazz: Class[_]): Int = {
    val name: String = clazz.getName.toLowerCase
    if (name.contains(STRING)) 1
    else if (name.contains(INT)) 2
    else if (name.contains(LONG)) 3
    else 99
  }

  def invoke(sessionService: SessionService, headers: Seq[HttpHeader], parameterMap: Map[String, String], body: String): Future[String] = {
    val promise: Promise[String] with Object = Promise[String]()
    Future {
      var traceId: String = null
      var salt: String = Constants.defaultDesKey
      var ignoreEDecrypt: Boolean = false
      try {
        extractValueFromHeaders(HEADER_ACTION_ID, headers) match {
          case Some(actionIdStr) =>
            extractValueFromHeaders(HEADER_TRACE_ID, headers) match {
              case Some(ti) => traceId = ti
                apiMap.get(actionIdStr.toInt) match {
                  case Some(tuple) =>
                    val method: Method = tuple._1
                    val parseClass: Class[_] = tuple._2
                    val apiMapping: ApiMapping = tuple._3
                    val actionMethodParamAttributeSeq: mutable.Seq[ActionMethodParamAttribute] = tuple._4

                    ignoreEDecrypt = apiMapping.ignoreEDecrypt()
                    val ignoreSession: Boolean = apiMapping.ignoreSession()

                    ignoreSession match {
                      case false =>
                        extractValueFromHeaders(HEADER_TOKEN, headers) match {
                          case Some(token) => val sessionResponse: SessionResponse = sessionService.touch(traceId, token)
                            SessionContext.set(sessionResponse)
                            sessionResponse.success match {
                              case true => salt = sessionResponse.salt
                              case _ => throw ServiceException.make(ErrorCode.get(sessionResponse.code))
                            }
                          case None => throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
                        }
                      case _ =>
                    }
                    val paramValues: Array[AnyRef] = obtainParamValues(actionMethodParamAttributeSeq, headers, parameterMap, body, salt, ignoreEDecrypt, parseClass)
                    promise.success(responseBody(method.invoke(HttpService.injector.getInstance(method.getDeclaringClass), paramValues: _*).asInstanceOf[ApiResponse], ignoreEDecrypt, salt))
                  case None => throw ServiceException.make(ErrorCode.EC_SYSTEM_ERROR)
                }
              case None => throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
            }
          case None => throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
        }

      } catch {
        case se: ServiceException =>
          logger.error(traceId, se)
          promise.success(responseBody(ApiResponse.makeErrorResponse(se.getErrorCode), ignoreEDecrypt, salt))
        case ite: InvocationTargetException =>
          logger.error(traceId, ite)
          promise.success(responseBody(ApiResponse.makeErrorResponse(ErrorCode.EC_SYSTEM_ERROR), ignoreEDecrypt, salt))
        case e: Exception =>
          logger.error(traceId, e)
          promise.success(responseBody(ApiResponse.makeErrorResponse(ErrorCode.EC_SYSTEM_ERROR), ignoreEDecrypt, salt))
      } finally {
        SessionContext.clear
      }
    }
    promise.future
  }

  def responseBody(apiResponse: ApiResponse, ignoreEDecrypt: Boolean, salt: String): String = {
    ignoreEDecrypt match {
      case true => JsonHelper.writeValueAsString(apiResponse)
      case _ => DESUtils.encrypt(JsonHelper.writeValueAsString(apiResponse), salt)
    }
  }

  def parseRequest(body: String, salt: String, ignoreEDecrypt: Boolean, parseClass: Class[_]): AnyRef = {
    val json: String = {
      ignoreEDecrypt match {
        case true => body
        case _ => DESUtils.decrypt(body, salt)
      }
    }
    JsonHelper.read(json, parseClass)
  }
}
