package com.lawsofnature.apigateway.invoker

import java.lang.reflect.{InvocationTargetException, Method, Parameter}
import java.util

import RpcSSO.SessionResponse
import akka.http.scaladsl.model.HttpHeader
import com.lawsofnature.apigateway.annotations.{ApiMapping, Param}
import com.lawsofnature.apigateway.conext.SessionContext
import com.lawsofnature.apigateway.domain.http.req.SendLoginVerificationCodeReq
import com.lawsofnature.apigateway.domain.http.resp.SimpleApiResponse
import com.lawsofnature.apigateway.enumerate.ParamSource
import com.lawsofnature.apigateway.helper.Constants
import com.lawsofnature.apigateway.server.HttpService
import com.lawsofnature.apigateway.service.SessionService
import com.lawsofnature.apigateway.validate.Validator
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.exception.{ErrorCode, ServiceException}
import com.lawsofnature.common.helper.{GZipHelper, RegHelper}
import com.trueaccord.scalapb.GeneratedMessage
import com.typesafe.config.ConfigFactory
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

  private val apiMap: mutable.Map[Int, (Method, Option[Class[_]], ApiMapping, scala.collection.mutable.Seq[ActionMethodParamAttribute])] = scala.collection.mutable.Map()

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

              if (actionMethodParamAttributeSeq.filter(_.source == ParamSource.BODY).length >= 2) {
                logger.error(s"There can only be one body source param in a action method, method: ${method.toGenericString}. now system will shutdown ...")
                System.exit(1)
              }

              val t: Option[Class[_]] = actionMethodParamAttributeSeq.zipWithIndex.filter(_._1.source == ParamSource.BODY).headOption match {
                case Some(tuple) => Some(types(tuple._2))
                case None => None
              }
              apiMap += (apiMapping.id() -> (method, t, apiMapping, actionMethodParamAttributeSeq))
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

  def obtainParamValues(actionMethodParamAttributeSeq: mutable.Seq[ActionMethodParamAttribute], headers: Seq[HttpHeader], parameterMap: Map[String, String], bodyArray: Array[Byte], salt: String, parseClass: Option[Class[_]]): Array[AnyRef] = {
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
              val bodyIsNull: Boolean = bodyArray == null || bodyArray.length == 0
              if (attr.required && bodyIsNull) throw ServiceException.make(attr.errorCode)
              val request: AnyRef = if (!bodyIsNull) parseRequest(bodyArray, salt, parseClass) else null
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

  def invoke(sessionService: SessionService, headers: Seq[HttpHeader], parameterMap: Map[String, String], bodyArray: Array[Byte]): Future[(Boolean, Array[Byte])] = {
    val promise: Promise[(Boolean, Array[Byte])] with Object = Promise[(Boolean, Array[Byte])]()
    Future {
      var traceId: String = null
      val salt: String = ConfigFactory.load().getString("edecrypt.default.des.key")
      try {
        extractValueFromHeaders(HEADER_ACTION_ID, headers) match {
          case Some(actionIdStr) =>
            extractValueFromHeaders(HEADER_TRACE_ID, headers) match {
              case Some(ti) => traceId = ti
                if (traceId.length != 32) throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
                apiMap.get(actionIdStr.toInt) match {
                  case Some(tuple) =>
                    val method: Method = tuple._1
                    val parseClass: Option[Class[_]] = tuple._2
                    val apiMapping: ApiMapping = tuple._3
                    val actionMethodParamAttributeSeq: mutable.Seq[ActionMethodParamAttribute] = tuple._4

                    apiMapping.ignoreSession() match {
                      case false =>
                        extractValueFromHeaders(HEADER_TOKEN, headers) match {
                          case Some(token) => val sessionResponse: SessionResponse = sessionService.touch(traceId, token)
                            sessionResponse.code match {
                              case "0" => SessionContext.set(sessionResponse)
                              case _ => throw ServiceException.make(ErrorCode.get(sessionResponse.code))
                            }
                          case None => throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
                        }
                      case _ =>
                    }

                    val paramValues: Array[AnyRef] = obtainParamValues(actionMethodParamAttributeSeq, headers, parameterMap, bodyArray, salt, parseClass)
                    promise.success((true, responseBody(method.invoke(HttpService.injector.getInstance(method.getDeclaringClass), paramValues: _*).asInstanceOf[GeneratedMessage], salt)))
                  case None => throw ServiceException.make(ErrorCode.EC_SYSTEM_ERROR)
                }
              case None => throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
            }
          case None => throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
        }

      } catch {
        case se: ServiceException =>
          logger.error(traceId, se)
          promise.success((false, responseBody(SimpleApiResponse(code = se.getErrorCode.getCode, se.getErrorCode.getDesc), salt)))
        case ite: InvocationTargetException =>
          logger.error(traceId, ite)
          promise.success((false, responseBody(SimpleApiResponse(code = ErrorCode.EC_SYSTEM_ERROR.getCode, ErrorCode.EC_SYSTEM_ERROR.getDesc), salt)))
        case e: Exception =>
          logger.error(traceId, e)
          promise.success((false, responseBody(SimpleApiResponse(code = ErrorCode.EC_SYSTEM_ERROR.getCode, ErrorCode.EC_SYSTEM_ERROR.getDesc), salt)))
      } finally {
        SessionContext.clear
      }
    }
    promise.future
  }

  def responseBody(message: GeneratedMessage, salt: String): Array[Byte] = {
    DESUtils.encrypt(GZipHelper.compress(message.toByteArray), salt)
  }

  def parseRequest(bodyArray: Array[Byte], salt: String, parseClass: Option[Class[_]]): AnyRef = {
    val protoArray: Array[Byte] = GZipHelper.uncompress(DESUtils.decrypt(bodyArray, salt))

    val clazz: Class[_] = parseClass match {
      case Some(c) => c
      case None => throw ServiceException.make(ErrorCode.EC_SYSTEM_ERROR)
    }

    clazz.getName match {
      case "com.lawsofnature.apigateway.domain.http.req.SendLoginVerificationCodeReq" =>
        SendLoginVerificationCodeReq.parseFrom(protoArray)
      case _ => throw ServiceException.make(ErrorCode.EC_SYSTEM_ERROR)
    }
  }
}
