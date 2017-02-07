package com.jxjxgo.apigateway.conext

import com.jxjxgo.sso.rpc.domain.SessionResponse


/**
  * Created by fangzhongwei on 2016/12/1.
  */
object SessionContext {
  private val local: ThreadLocal[SessionResponse] = new ThreadLocal[SessionResponse]

  def set(sessionResponse: SessionResponse) = local.set(sessionResponse)

  def get: SessionResponse = local.get()

  def clear = local.remove()
}
