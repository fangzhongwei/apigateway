package com.jxjxgo.apigateway.action

import com.jxjxgo.apigateway.conext.SessionContext
import com.jxjxgo.sso.rpc.domain.SessionResponse

/**
  * Created by fangzhongwei on 2016/12/1.
  */
trait BaseAction {
  def getMemberId: Long = getSession.memberId

  def getSession: SessionResponse = SessionContext.get
}
