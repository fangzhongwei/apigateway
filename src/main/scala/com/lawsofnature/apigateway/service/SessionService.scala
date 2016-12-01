package com.lawsofnature.apigateway.service

import javax.inject.Inject

import RpcSSO.{LoginRequest, SessionResponse}
import com.lawsofnature.apigateway.request.AppLoginRequest
import com.lawsofnature.sso.client.SSOClientService

/**
  * Created by fangzhongwei on 2016/11/25.
  */
trait SessionService {
  def login(traceId: String, ip: String, appLoginRequest: AppLoginRequest): SessionResponse

  def touch(traceId: String, token: String): SessionResponse
}

class SessionServiceImpl @Inject()(ssoClientService: SSOClientService) extends SessionService {
  override def login(traceId: String, ip: String, request: AppLoginRequest): SessionResponse = {
    ssoClientService.login(traceId, new LoginRequest(ip, request.dt, request.di, request.lat, request.lng, request.ctry, request.pro, request.c, request.cty, request.addr, request.ci, request.i, request.pwd))
  }

  override def touch(traceId: String, token: String): SessionResponse = ssoClientService.touch(traceId, token)

}
