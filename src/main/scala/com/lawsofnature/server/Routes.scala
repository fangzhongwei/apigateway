package com.lawsofnature.server

import javax.inject.{Inject, Named}

import akka.http.scaladsl.server.{Directive, Directive1}
import akka.http.scaladsl.server.Directives._
import com.lawsofnature.common.edecrypt.DESUtils
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

  private val directive: Directive[Unit] = path("register") & post

  val apigatewayRoutes =
    directive {
      val entity1: Directive1[String] = entity(as[String])



      entity1 { raw => {
        val registerRequest: RegisterRequest = JsonHelper.read[RegisterRequest](DESUtils.decrypt(raw, Constant.defaultDesKey), classOf[RegisterRequest])
        logger.info("receive get banks request {}", registerRequest)
        registerRequest.validate() match {
          case Some(error) => ResponseFactory.serviceErrorResponse(error)
          case None => onSuccess(memberService.register(registerRequest)) {
            case Some(apiResponse) => ResponseFactory.response(apiResponse)
            case None => ResponseFactory.commonErrorResponse()
          }
        }
      }
      }
    } ~ (path("banks") & post) {
      logger.info("receive get banks request")
      entity(as[String]) { request =>
        logger.info(request)
        println(DESUtils.decrypt(request, "12345678"))
        complete {"Order received12"}
      }
    }

}
