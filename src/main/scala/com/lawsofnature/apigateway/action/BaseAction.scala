package com.lawsofnature.apigateway.action

import RpcSSO.SessionResponse
import com.lawsofnature.apigateway.conext.SessionContext

/**
  * Created by fangzhongwei on 2016/12/1.
  */
trait BaseAction {
  def getMemberId: Long = SessionContext.get.memberId

  def getSession: SessionResponse = SessionContext.get
}
