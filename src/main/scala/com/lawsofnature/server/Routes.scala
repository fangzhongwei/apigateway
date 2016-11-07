package com.lawsofnature.server

import javax.inject.{Inject, Named}

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import com.lawsofnature.action.RegisterAction
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.factory.ResponseFactory
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by kgoralski on 2016-05-02.
  */
@Named
class Routes @Inject()(registerAction: RegisterAction) {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: Exception =>
      println("eeeeeeeee")
      ex.printStackTrace()
      extractUri { uri =>
        logger.error("apigateway", ex)
        println(s"Request to $uri could not be handled normally")
        ResponseFactory.commonErrorResponse()
      }
  }

  val apigatewayRoutes =
    (path("register") & post) {
      entity(as[String]) {
        raw => {
          registerAction.register(raw)
        }
      }
    } ~ (path("banks") & post) {
      logger.info("receive get banks request")
      entity(as[String]) { request =>
        logger.info(request)
        println(DESUtils.decrypt(request, "12345678"))
        complete {
          "Order received12"
        }
      }
    } ~ handleExceptions(myExceptionHandler) {
      println("aaaaaaaaa")
      ResponseFactory.commonInvalidRequestResponse()
    }
}
