package com.lawsofnature.server

import javax.inject.{Inject, Named}

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.lawsofnature.action.{RegisterAction, SSOAction}
import com.lawsofnature.common.exception.{ServiceErrorCode, ServiceException}
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.helper.Constant
import com.lawsofnature.invoker.ActionInvoker
import com.lawsofnature.service.SessionService
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Created by kgoralski on 2016-05-02.
  */
@Named
class Routes @Inject()(registerAction: RegisterAction, ssoAction: SSOAction, sessionService: SessionService) {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  implicit val timeout = (90 seconds)

  val HEADER_IP = "X-Real-IP"
  val HEADER_TRACE_ID = "TI"
  val HEADER_ACTION_ID = "AI"
  val HEADER_TOKEN = "TK"
  val BLANK = ""

  val myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: Exception =>
      extractUri { uri =>
        logger.error("apigateway", ex)
        logger.error(s"Request to $uri could not be handled normally")
        //        ResponseFactory.commonErrorResponse()
        complete("internal error")
      }
  }

  val apigatewayRoutes =
    (path("v1.0-route") & post) {
      val millis: Long = System.currentTimeMillis()
      headerValueByName(HEADER_IP) {
        ip =>
          headerValueByName(HEADER_TRACE_ID) {
            traceId =>
              headerValueByName(HEADER_ACTION_ID) {
                actionId =>
                  headerValueByName(HEADER_TOKEN) {
                    token =>
                      entity(as[String]) {
                        body => {
                          onSuccess(doRoute(ip, traceId, actionId.toInt, token, body)) {
                            case result =>
                              println("call service cost : " + (System.currentTimeMillis() - millis))
                              complete(result)
                          }
                        }
                      }
                  }
              }
          }
      }
    } ~ handleExceptions(myExceptionHandler) {
      logger.error("system error")
      complete("internal error")
    }

  def doRoute(ip: String, traceId: String, actionId: Int, token: String, body: String): Future[String] = {
    try {
      var salt: String = Constant.defaultDesKey
      if (actionId >= 2002)
        Await.result(sessionService.touch(traceId, token), timeout) match {
          case Some(sessionResponse) =>
            sessionResponse.success match {
              case true =>
                salt = sessionResponse.salt
              case false =>
                throw ServiceException.make(ServiceErrorCode.get(sessionResponse.code))
            }
          case None => throw ServiceException.make(ServiceErrorCode.EC_SSO_SESSION_EXPIRED)
        }

      ActionInvoker.invoke(actionId, ip, traceId, body, salt)
    } finally {

    }
  }
}
