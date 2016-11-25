package com.lawsofnature.server

import javax.inject.{Inject, Named}

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import com.lawsofnature.action.{RegisterAction, SSOAction}
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.helper.{Constant, JsonHelper}
import com.lawsofnature.request.{AppLoginRequest, CheckIdentityRequest, RegisterRequest}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by kgoralski on 2016-05-02.
  */
@Named
class Routes @Inject()(registerAction: RegisterAction, ssoAction: SSOAction) {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: Exception =>
      extractUri { uri =>
        logger.error("apigateway", ex)
        logger.error(s"Request to $uri could not be handled normally")
        ResponseFactory.commonErrorResponse()
      }
  }

  val apigatewayRoutes =
    (path("v1.0/check-identity") & post) {
      headerValueByName("TI") {
        traceId => entity(as[String]) { body =>
          registerAction.checkIdentity(traceId, JsonHelper.read[CheckIdentityRequest](DESUtils.decrypt(body, Constant.defaultDesKey), classOf[CheckIdentityRequest]))
        }
      }
    } ~ (path("v1.0/register") & post) {
      headerValueByName("X-Real-IP") {
        ip =>
          headerValueByName("TI") {
            traceId => entity(as[String]) {
              body => {
                registerAction.register(traceId, ip, JsonHelper.read[RegisterRequest](DESUtils.decrypt(body, Constant.defaultDesKey), classOf[RegisterRequest]))
              }
            }
          }
      }
    } ~ (path("v1.0/login") & post) {
      headerValueByName("X-Real-IP") {
        ip =>
          headerValueByName("TI") {
            traceId => entity(as[String]) {
              body => {
                ssoAction.login(traceId, ip, JsonHelper.read[AppLoginRequest](DESUtils.decrypt(body, Constant.defaultDesKey), classOf[AppLoginRequest]))
              }
            }
          }
      }
    } ~ handleExceptions(myExceptionHandler) {
      logger.error("system error")
      ResponseFactory.commonInvalidRequestResponse()
    }
}
