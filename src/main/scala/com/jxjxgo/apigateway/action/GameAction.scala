package com.jxjxgo.apigateway.action

import javax.inject.Inject

import com.jxjxgo.apigateway.annotations.{ApiMapping, Param}
import com.jxjxgo.apigateway.domain.http.resp.checkgamestatus.{CheckGameStatusResp}
import com.jxjxgo.apigateway.enumerate.ParamSource
import com.jxjxgo.apigateway.service.GameService
import com.jxjxgo.gamecenter.rpc.domain.CheckGameStatusResponse

/**
  * Created by fangzhongwei on 2017/3/1.
  */
trait GameAction {
  def checkGameStatus(traceId: String): CheckGameStatusResp
}

class GameActionImpl @Inject()(gameService: GameService) extends GameAction with BaseAction {

  implicit def convert(response: CheckGameStatusResponse): CheckGameStatusResp = {
    CheckGameStatusResp(
      code = response.code,
      memberId = response.memberId,
      reconnect = response.reconnect
    )
  }

  @ApiMapping(id = 2006)
  override def checkGameStatus(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                               traceId: String): CheckGameStatusResp = {
    gameService.checkGameStatus(traceId, getMemberId, getSession.fingerPrint)
  }
}
