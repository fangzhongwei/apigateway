package com.lawsofnature.apigateway.action

import javax.inject.Inject

import RpcSSO.{SSOBaseResponse, SessionResponse}
import com.lawsofnature.apigateway.annotations.{ApiMapping, Param}
import com.lawsofnature.apigateway.enumerate.ParamSource
import com.lawsofnature.apigateway.request.AppLoginRequest
import com.lawsofnature.apigateway.response.{ApiResponse, SuccessResponse}
import com.lawsofnature.apigateway.service.SessionService
import com.lawsofnature.common.exception.ErrorCode

/**
  * Created by fangzhongwei on 2016/11/23.
  */
trait SSOAction {
  def login(traceId: String, ip: String, request: AppLoginRequest): ApiResponse

//  def autoLogin(traceId: String, ip: String, request: AppLoginRequest): ApiResponse

  def logout(traceId: String): ApiResponse
}

class SSOActionImpl @Inject()(sessionService: SessionService) extends SSOAction with BaseAction {

  @ApiMapping(id = 1002, ignoreSession = true)
  override def login(@Param(required = true, source = ParamSource.HEADER, name = "traceId")
                     traceId: String,
                     @Param(required = true, source = ParamSource.HEADER, name = "X-Real-Ip")
                     ip: String,
                     @Param(required = true, source = ParamSource.BODY)
                     request: AppLoginRequest): ApiResponse = {
    val sessionResponse: SessionResponse = sessionService.login(traceId, ip, request)
    sessionResponse.success match {
      case true => ApiResponse.make(data = sessionResponse)
      case _ => ApiResponse.makeErrorResponse(ErrorCode.get(""))
    }
  }

  @ApiMapping(id = 1003)
  override def logout(@Param(required = true, source = ParamSource.HEADER, name = "traceId")
                      traceId: String): ApiResponse = {
    val response: SSOBaseResponse = sessionService.logout(traceId, getSession.token)
    response.success match {
      case true => ApiResponse.makeSuccessResponse(SuccessResponse.SUCCESS)
      case false => ApiResponse.makeErrorResponse(ErrorCode.get(""))
    }
  }
}
