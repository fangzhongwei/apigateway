package com.lawsofnature.service

import javax.inject.Inject

import RpcSSO.{LoginRequest, SessionResponse}
import com.lawsofnature.request.AppLoginRequest
import com.lawsofnature.sso.client.SSOClientService

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by fangzhongwei on 2016/11/25.
  */
trait SessionService {
  def login(traceId: String, ip: String, appLoginRequest: AppLoginRequest): Future[Option[SessionResponse]]
  def touch(traceId: String, token:String): Future[Option[SessionResponse]]
}

class SessionServiceImpl @Inject()(ssoClientService: SSOClientService) extends SessionService {
  override def login(traceId: String, ip: String, request: AppLoginRequest): Future[Option[SessionResponse]] = {
    val promise: Promise[Option[SessionResponse]] = Promise[Option[SessionResponse]]()
    Future {
      promise.success(Some(ssoClientService.login(traceId, new LoginRequest(ip, request.dt, request.di, request.lat, request.lng, request.ctry, request.pro, request.c, request.cty, request.addr, request.ci, request.i, request.pwd))))
    }
    promise.future
  }

  override def touch(traceId: String, token: String): Future[Option[SessionResponse]] = {
    val promise: Promise[Option[SessionResponse]] = Promise[Option[SessionResponse]]()
    Future {
      promise.success(Some(ssoClientService.touch(traceId, token)))
    }
    promise.future
  }
}
