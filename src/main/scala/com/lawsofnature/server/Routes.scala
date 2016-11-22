package com.lawsofnature.server

import javax.inject.{Inject, Named}

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import com.lawsofnature.action.RegisterAction
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.helper.{Constant, JsonHelper}
import com.lawsofnature.request.{CheckIdentityRequest, RegisterRequest}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by kgoralski on 2016-05-02.
  */
@Named
class Routes @Inject()(registerAction: RegisterAction) {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: Exception =>
      extractUri { uri =>
        logger.error("apigateway", ex)
        println(s"Request to $uri could not be handled normally")
        ResponseFactory.commonErrorResponse()
      }
  }

  val apigatewayRoutes =
    (path("register") & post) {
      headerValueByName("X-Real-IP") {
        ip =>
          entity(as[String]) {
            body => {
              registerAction.register(ip, JsonHelper.read[RegisterRequest](DESUtils.decrypt(body, Constant.defaultDesKey), classOf[RegisterRequest]))
            }
          }
      }
    } ~ (path("check-identity") & post) {
      entity(as[String]) { body =>
        registerAction.checkIdentity(JsonHelper.read[CheckIdentityRequest](DESUtils.decrypt(body, Constant.defaultDesKey), classOf[CheckIdentityRequest]))
      }
    } ~ handleExceptions(myExceptionHandler) {
      logger.error("system error")
      ResponseFactory.commonInvalidRequestResponse()
    }
}
