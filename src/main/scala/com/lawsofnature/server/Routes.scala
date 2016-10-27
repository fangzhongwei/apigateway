package com.lawsofnature.server

import javax.inject.{Inject, Named}

import akka.http.scaladsl.server.Directives._
import com.lawsofnature.json.JsonHelper
import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.response.ApiResponse
import com.lawsofnature.service.MemberService
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by kgoralski on 2016-05-02.
  */
@Named
class Routes @Inject()(memberService: MemberService) extends JsonHelper {
  val logger:Logger = LoggerFactory.getLogger(getClass)
  implicit val registerRequestFormat = jsonFormat7(RegisterRequest.apply)
  implicit val apiResponseFormat = jsonFormat3(ApiResponse.apply)

  val apigatewayRoutes = {
    (path("register") & post) {
      entity(as[RegisterRequest]) { request =>
        onSuccess(memberService.register(request)) {
          case Some(apiResponse) => complete(apiResponse)
          case None => complete(ApiResponse("1", ""))
        }
      }
    }~ (path("banks") & get) {
      logger.info("receive get banks request")
      onSuccess(memberService.register(RegisterRequest(1,"aa","aa",1,"15812346678","aab","abc"))) {
        case Some(apiResponse) => complete(apiResponse)
        case None => complete(ApiResponse("1", ""))
      }
    }
  }
}
