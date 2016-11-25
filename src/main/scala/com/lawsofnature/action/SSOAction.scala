package com.lawsofnature.action

import javax.inject.Inject

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.helper.Constant
import com.lawsofnature.request.AppLoginRequest
import com.lawsofnature.response.ApiResponse
import com.lawsofnature.service.SessionService

/**
  * Created by fangzhongwei on 2016/11/23.
  */
trait SSOAction {
  def login(traceId: String, ip: String, request: AppLoginRequest): Route
}

class SSOActionImpl @Inject()(sessionService: SessionService) extends SSOAction {
  override def login(traceId: String, ip: String, request: AppLoginRequest): Route = {
    request.validate() match {
      case Some(errorCode) => ResponseFactory.serviceErrorResponse(errorCode)
      case None => onSuccess(sessionService.login(traceId, ip, request))  {
        case Some(response) =>
          ResponseFactory.response(new ApiResponse(0,"", response), Constant.defaultDesKey)
        case None =>
          ResponseFactory.commonErrorResponse()
      }
    }
  }
}
