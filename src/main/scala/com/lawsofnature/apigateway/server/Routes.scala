package com.lawsofnature.apigateway.server

import javax.inject.{Inject, Named}

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import com.lawsofnature.apigateway.action.{RegisterAction, SSOAction}
import com.lawsofnature.apigateway.invoker.BodyActionInvoker
import com.lawsofnature.apigateway.service.SessionService
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._

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
        complete("error")
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
                          onSuccess(BodyActionInvoker.invoke(sessionService, actionId.toInt, ip, traceId, body, token)) {
                            case result =>
                              logger.info("call service cost : " + (System.currentTimeMillis() - millis))
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
      complete("invalid request")
    }
}
