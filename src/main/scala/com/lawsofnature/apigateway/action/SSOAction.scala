package com.lawsofnature.apigateway.action

import javax.inject.Inject

import RpcSSO.SessionResponse
import com.lawsofnature.apigateway.annotations.{ApiMapping, Param}
import com.lawsofnature.apigateway.enumerate.ParamSource
import com.lawsofnature.apigateway.request.AppLoginRequest
import com.lawsofnature.apigateway.response.ApiResponse
import com.lawsofnature.apigateway.service.SessionService
import com.lawsofnature.common.exception.ErrorCode

/**
  * Created by fangzhongwei on 2016/11/23.
  */
trait SSOAction {
  def login(traceId: String, ip: String, request: AppLoginRequest): ApiResponse
}

class SSOActionImpl @Inject()(sessionService: SessionService) extends SSOAction {

  @ApiMapping(id = 2001, ignoreSession = true)
  override def login(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                     traceId: String,
                     @Param(required = true, source = ParamSource.HEADER, name = "X-Real-Ip")
                     ip: String,
                     @Param(required = true, source = ParamSource.BODY)
                     request: AppLoginRequest): ApiResponse = {
    val sessionResponse: SessionResponse = sessionService.login(traceId, ip, request)
    sessionResponse.success match {
      case true => ApiResponse.make(data = sessionResponse)
      case _ => ApiResponse.makeErrorResponse(ErrorCode.get(sessionResponse.code))
    }
  }
}
