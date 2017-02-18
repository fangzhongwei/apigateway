package com.jxjxgo.apigateway.service

import java.lang.reflect.{InvocationTargetException, Method, Parameter}
import java.util

import akka.http.scaladsl.model.HttpHeader
import com.jxjxgo.apigateway.annotations.{ApiMapping, Param}
import com.jxjxgo.apigateway.base.ApiConfigContext.ActionMethodParamAttribute
import com.jxjxgo.apigateway.base.{ApiConfigContext, ParamHelper}
import com.jxjxgo.apigateway.conext.SessionContext
import com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq
import com.jxjxgo.apigateway.domain.http.req.simple.SimpleReq
import com.jxjxgo.apigateway.domain.http.resp.SimpleApiResponse
import com.jxjxgo.apigateway.enumerate.ParamSource
import com.jxjxgo.apigateway.server.HttpService
import com.jxjxgo.apigateway.validate.Validator
import com.jxjxgo.common.edecrypt.DESUtils
import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import com.jxjxgo.common.helper.{GZipHelper, RegHelper}
import com.jxjxgo.sso.rpc.domain.SessionResponse
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
trait ActionInvokerService {
  def invoke(sessionService: SessionService, headers: Seq[HttpHeader], parameterMap: Map[String, String], bodyArray: Array[Byte]): Future[(Boolean, Array[Byte])]
}

class ActionInvokerServiceImpl extends ActionInvokerService {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  implicit val timeout = (90 seconds)

  val HEADER_TRACE_ID = "TI"
  val HEADER_ACTION_ID = "AI"
  val HEADER_TOKEN = "TK"
  val HEADER_FINGERPRINT = "FP"

  override def invoke(sessionService: SessionService, headers: Seq[HttpHeader], parameterMap: Map[String, String], bodyArray: Array[Byte]): Future[(Boolean, Array[Byte])] = {
    val promise: Promise[(Boolean, Array[Byte])] with Object = Promise[(Boolean, Array[Byte])]()
    Future {
      var traceId: String = null
      val salt: String = ConfigFactory.load().getString("edecrypt.default.des.key")
      try {
        ParamHelper.extractValueFromHeaders(HEADER_ACTION_ID, headers) match {
          case Some(actionIdStr) =>
            ParamHelper.extractValueFromHeaders(HEADER_TRACE_ID, headers) match {
              case Some(ti) => traceId = ti
                if (traceId.length != 32) throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
                ApiConfigContext.getAipConfig(actionIdStr.toInt) match {
                  case Some(tuple) =>
                    val method: Method = tuple._1
                    val parseClass: Option[Class[_]] = tuple._2
                    val apiMapping: ApiMapping = tuple._3
                    val actionMethodParamAttributeSeq: mutable.Seq[ActionMethodParamAttribute] = tuple._4

                    apiMapping.ignoreSession() match {
                      case false =>
                        ParamHelper.extractValueFromHeaders(HEADER_TOKEN, headers) match {
                          case Some(token) => val sessionResponse: SessionResponse = sessionService.touch(traceId, token)
                            if (!sessionResponse.identity.equals(ParamHelper.extractValueFromHeaders(HEADER_FINGERPRINT, headers).get)) {
                              throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
                            }
                            sessionResponse.code match {
                              case "0" => SessionContext.set(sessionResponse)
                              case _ => throw ServiceException.make(ErrorCode.get(sessionResponse.code))
                            }
                          case None => throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
                        }
                      case _ =>
                    }

                    val paramValues: Array[AnyRef] = ParamHelper.obtainParamValues(actionMethodParamAttributeSeq, headers, parameterMap, bodyArray, salt, parseClass)
                    val invoke1: AnyRef = method.invoke(HttpService.getInjector.getInstance(method.getDeclaringClass), paramValues: _*)
                    logger.info("invoke result:" + invoke1)
                    logger.info("invoke class:" + invoke1.getClass)



                    promise.success(true, responseBody(invoke1.asInstanceOf[GeneratedMessage], salt))
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
    logger.info(s"return message is : $message")
    DESUtils.encrypt(GZipHelper.compress(message.toByteArray), salt)
  }

}
