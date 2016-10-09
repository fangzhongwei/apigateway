package com.lawsofnature.server

import javax.inject.{Inject, Named}

import akka.http.scaladsl.server.Directives._
import com.lawsofnature.json.JsonHelper
import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.response.ApiResponse
import com.lawsofnature.service.MemberService

/**
  * Created by kgoralski on 2016-05-02.
  */
@Named
class Routes @Inject()(memberService: MemberService) extends JsonHelper {
  val apigatewayRoutes = {
    (path("bank") & post) {
      entity(as[RegisterRequest]) { request =>
        onSuccess(memberService.register(request)) {
          case Some(apiResponse) => complete(apiResponse)
          case None => complete(ApiResponse("1", "", null))
        }
      }
    }
  }
}
