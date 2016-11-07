package com.lawsofnature.server

import javax.inject.{Inject, Named}

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.exception.ServiceException
import com.lawsofnature.common.exception.ServiceErrorCode._
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.helper.{Constant, JsonHelper}
import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.service.MemberService
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by kgoralski on 2016-05-02.
  */
@Named
class Routes @Inject()(memberService: MemberService) {
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
      headerValueByName("tk") {
        tk =>
          println("tk=" + tk)
          parameterMap {
            map => entity(as[String]) { raw => {
              val registerRequest: RegisterRequest = JsonHelper.read[RegisterRequest](DESUtils.decrypt(raw, Constant.defaultDesKey), classOf[RegisterRequest])
              logger.info("receive get banks request {}", registerRequest)
              registerRequest.validate() match {
                case Some(error) => ResponseFactory.serviceErrorResponse(error)
                case None => onSuccess(memberService.register(registerRequest)) {
                  case Some(apiResponse) =>
                    ResponseFactory.response(apiResponse)
                  case None =>
                    ResponseFactory.commonErrorResponse()
                }
              }
            }
            }
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
