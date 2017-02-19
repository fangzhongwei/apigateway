package com.jxjxgo.apigateway.service

import java.lang.reflect.{InvocationTargetException, Method}
import javax.inject.Inject

import akka.http.scaladsl.model.HttpHeader
import com.jxjxgo.apigateway.annotations.ApiMapping
import com.jxjxgo.apigateway.base.ApiConfigContext.ActionMethodParamAttribute
import com.jxjxgo.apigateway.base.{ApiConfigContext, ParamHelper}
import com.jxjxgo.apigateway.conext.SessionContext
import com.jxjxgo.apigateway.domain.http.resp.SimpleApiResponse
import com.jxjxgo.apigateway.server.HttpService
import com.jxjxgo.common.edecrypt.DESUtils
import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import com.jxjxgo.common.helper.GZipHelper
import com.jxjxgo.sso.rpc.domain.SessionResponse
import com.trueaccord.scalapb.GeneratedMessage
import com.typesafe.config.ConfigFactory
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.immutable.Seq
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/11/28.
  */
trait ActionExecuteService {
  def exe(headers: Seq[HttpHeader], parameterMap: Map[String, String], bodyArray: Array[Byte]): Future[(Boolean, Array[Byte])]
}

class ActionExecuteServiceImpl @Inject()(sessionService: SessionService) extends ActionExecuteService {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  implicit val timeout = (90 seconds)

  val HEADER_TRACE_ID = "TI"
  val HEADER_ACTION_ID = "AI"
  val HEADER_TOKEN = "TK"
  val HEADER_FINGERPRINT = "FP"

  //  def doExecute(actionId: Int, paramValues: Array[AnyRef]): GeneratedMessage = {
  //    actionId match {
  //      case 1001 => smsAction.sendLoginVerificationCode(paramValues(0).toString, paramValues(1).toString, paramValues(2).asInstanceOf[SendLoginVerificationCodeReq])
  //      case 1002 => ssoAction.login(paramValues(0).toString, paramValues(1).toString, paramValues(2).asInstanceOf[LoginReq])
  //      case 1003 => ssoAction.loginByToken(paramValues(0).toString, paramValues(1).toString, paramValues(2).asInstanceOf[LoginByTokenReq])
  //      case 1004 => ssoAction.logout(paramValues(0).toString)
  //      case 1005 => memberAction.updateNickName(paramValues(0).toString, paramValues(1).asInstanceOf[UpdateNickNameReq])
  //      case 1006 => i18NAction.getLatest(paramValues(0).toString, paramValues(1).asInstanceOf[SimpleReq])
  //      case 1007 => i18NAction.pullLatest(paramValues(0).toString, paramValues(1).asInstanceOf[PullResourceReq])
  //
  //      case 2001 => accountAction.queryDiamondAmount(paramValues(0).toString)
  //      case 2002 => accountAction.getPriceList(paramValues(0).toString)
  //      case 2003 => accountAction.getChannelList(paramValues(0).toString)
  //      case 2004 => accountAction.depositRequest(paramValues(0).toString, paramValues(1).asInstanceOf[DepositReq])
  //      case 2005 => accountAction.queryDeposit(paramValues(0).toString, paramValues(1).asInstanceOf[SimpleReq])
  //
  //      case _ => SimpleApiResponse(code = ErrorCode.EC_INVALID_REQUEST.getCode)
  //    }
  //  }

  override def exe(headers: Seq[HttpHeader], parameterMap: Map[String, String], bodyArray: Array[Byte]): Future[(Boolean, Array[Byte])] = {
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
                val actionId: Int = actionIdStr.toInt
                ApiConfigContext.getAipConfig(actionId) match {
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
                    promise.success(true, responseBody(method.invoke(HttpService.getInjector.getInstance(method.getDeclaringClass), paramValues: _*).asInstanceOf[GeneratedMessage], salt))
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
