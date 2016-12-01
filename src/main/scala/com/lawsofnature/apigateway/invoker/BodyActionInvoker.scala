package com.lawsofnature.apigateway.invoker

import java.lang.reflect.{InvocationTargetException, Method}
import java.util

import RpcSSO.SessionResponse
import com.lawsofnature.apigateway.annotations.ApiMapping
import com.lawsofnature.apigateway.conext.SessionContext
import com.lawsofnature.apigateway.helper.Constant
import com.lawsofnature.apigateway.response.ApiResponse
import com.lawsofnature.apigateway.server.HttpService
import com.lawsofnature.apigateway.service.SessionService
import com.lawsofnature.apigateway.validate.Validator
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.exception.{ErrorCode, ServiceException}
import com.lawsofnature.common.helper.JsonHelper
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/11/28.
  */
object BodyActionInvoker {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  implicit val timeout = (90 seconds)

  private val apiMap: scala.collection.mutable.Map[Int, (Method, Class[_], ApiMapping)] = scala.collection.mutable.Map()

  private val classList: util.ArrayList[Class[_]] = HttpService.actionBeanClassList

  private val iterator: util.Iterator[Class[_]] = classList.iterator()
  var clazz: Class[_] = null
  while (iterator.hasNext) {
    clazz = iterator.next()
    val declaredMethods: Array[Method] = clazz.getDeclaredMethods
    for (i <- 0 to declaredMethods.length - 1) {
      val method: Method = declaredMethods(i)
      val apiMapping: ApiMapping = method.getAnnotation[ApiMapping](classOf[ApiMapping])
      if (apiMapping != null) {
        val types: Array[Class[_]] = method.getParameterTypes
        apiMap += (apiMapping.id() -> (method, types(types.length - 1), apiMapping))
      }
    }
  }

  def invoke(sessionService: SessionService, actionId: Int, ip: String, traceId: String, body: String, token: String): Future[String] = {
    val promise: Promise[String] with Object = Promise[String]()
    Future {
      var salt: String = Constant.defaultDesKey
      var ignoreEDecrypt: Boolean = false
      try {
        val maybeTuple: Option[(Method, Class[_], ApiMapping)] = apiMap.get(actionId)
        maybeTuple match {
          case Some(tuple) =>
            val method: Method = tuple._1
            val parseClass: Class[_] = tuple._2
            val apiMapping: ApiMapping = tuple._3

            ignoreEDecrypt = apiMapping.ignoreEDecrypt()
            val ignoreSession: Boolean = apiMapping.ignoreSession()

            ignoreSession match {
              case false =>
                val sessionResponse: SessionResponse = sessionService.touch(traceId, token)
                SessionContext.set(sessionResponse)
                sessionResponse.success match {
                  case true => salt = sessionResponse.salt
                  case _ => throw ServiceException.make(ErrorCode.get(sessionResponse.code))
                }
              case _ =>
            }

            val request: AnyRef = parseRequest(body, salt, ignoreEDecrypt, parseClass)

            Validator.validate(request) match {
              case Some(errorCode) => throw ServiceException.make(errorCode)
              case None =>
                promise.success(responseBody(method.invoke(HttpService.injector.getInstance(method.getDeclaringClass), traceId, ip, request).asInstanceOf[ApiResponse], ignoreEDecrypt, salt))
            }
          case None =>
            throw ServiceException.make(ErrorCode.EC_SYSTEM_ERROR)
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
