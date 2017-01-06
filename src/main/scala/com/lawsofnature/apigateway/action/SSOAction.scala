package com.lawsofnature.apigateway.action

import javax.inject.Inject

import RpcSSO.SSOBaseResponse
import com.lawsofnature.apigateway.annotations.{ApiMapping, Param}
import com.lawsofnature.apigateway.domain.http.req.{LoginByTokenReq, LoginReq}
import com.lawsofnature.apigateway.domain.http.resp.{LoginResp, SimpleApiResponse}
import com.lawsofnature.apigateway.enumerate.ParamSource
import com.lawsofnature.apigateway.service.SessionService
import com.lawsofnature.common.helper.IPv4Helper

/**
  * Created by fangzhongwei on 2016/11/23.
  */
trait SSOAction {
  def login(traceId: String, ip: String, request: LoginReq): LoginResp

  def loginByToken(traceId: String, ip: String, request: LoginByTokenReq): LoginResp

  def logout(traceId: String): SimpleApiResponse
}

class SSOActionImpl @Inject()(sessionService: SessionService) extends SSOAction with BaseAction {

  @ApiMapping(id = 1002, ignoreSession = true)
  override def login(@Param(required = true, source = ParamSource.HEADER, name = "traceId")
                     traceId: String,
                     @Param(required = true, source = ParamSource.HEADER, name = "X-Real-Ip")
                     ip: String,
                     @Param(required = true, source = ParamSource.BODY)
                     request: LoginReq): LoginResp = {
    sessionService.login(traceId, IPv4Helper.ipToLong(ip), request)
  }

  @ApiMapping(id = 1003)
  override def loginByToken(traceId: String, ip: String, request: LoginByTokenReq): LoginResp = {
    sessionService.loginByToken(traceId, getSession)
  }

  @ApiMapping(id = 1004)
  override def logout(@Param(required = true, source = ParamSource.HEADER, name = "traceId")
                      traceId: String): SimpleApiResponse = {
    val response: SSOBaseResponse = sessionService.logout(traceId, getSession.token)
    SimpleApiResponse(code = response.code)
  }

}
