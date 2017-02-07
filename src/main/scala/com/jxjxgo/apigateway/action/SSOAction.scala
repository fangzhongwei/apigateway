package com.jxjxgo.apigateway.action

import javax.inject.Inject

import com.jxjxgo.apigateway.annotations.{ApiMapping, Param}
import com.jxjxgo.apigateway.domain.http.req.login.LoginReq
import com.jxjxgo.apigateway.domain.http.req.loginbytoken.LoginByTokenReq
import com.jxjxgo.apigateway.domain.http.resp.{LoginResp, SimpleApiResponse}
import com.jxjxgo.apigateway.enumerate.ParamSource
import com.jxjxgo.apigateway.service.SessionService
import com.jxjxgo.common.helper.IPv4Helper
import com.jxjxgo.sso.rpc.domain.SSOBaseResponse

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
  override def login(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                     traceId: String,
                     @Param(required = true, source = ParamSource.HEADER, name = "X-Real-Ip")
                     ip: String,
                     @Param(required = true, source = ParamSource.BODY)
                     request: LoginReq): LoginResp = {
    sessionService.login(traceId, IPv4Helper.ipToLong(ip), request)
  }

  @ApiMapping(id = 1003)
  override def loginByToken(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                            traceId: String,
                            @Param(required = true, source = ParamSource.HEADER, name = "X-Real-Ip")
                            ip: String,
                            @Param(required = true, source = ParamSource.BODY)
                            request: LoginByTokenReq): LoginResp = {
    sessionService.loginByToken(traceId, getSession)
  }

  @ApiMapping(id = 1004)
  override def logout(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                      traceId: String): SimpleApiResponse = {
    val response: SSOBaseResponse = sessionService.logout(traceId, getSession.token)
    SimpleApiResponse(code = response.code)
  }
}
