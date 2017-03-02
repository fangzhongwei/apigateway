package com.jxjxgo.apigateway.action

import javax.inject.Inject

import com.jxjxgo.apigateway.annotations.{ApiMapping, Param}
import com.jxjxgo.apigateway.domain.http.resp.checkgamestatus.{CheckGameStatusResp, GameTurnResp}
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
    response.turn match {
      case None =>
        CheckGameStatusResp(
          code = response.code,
          memberId = response.memberId,
          reconnect = response.reconnect
        )
      case Some(t) =>
        CheckGameStatusResp(
          code = response.code,
          memberId = response.memberId,
          reconnect = response.reconnect,
          turn = Some(GameTurnResp(
            gameId = t.gameId,
            gameType = t.gameType,
            deviceType = t.deviceType,
            cards = t.cards,
            landlordCards = t.landlordCards,
            baseAmount = t.baseAmount,
            multiples = t.multiples,
            previousNickname = t.previousNickname,
            previousCardsCount = t.previousCardsCount,
            nextNickname = t.nextNickname,
            nextCardsCount = t.nextCardsCount,
            choosingLandlord = t.choosingLandlord,
            landlord = t.landlord,
            turnToPlay = t.turnToPlay
          ))
        )
    }
  }

  @ApiMapping(id = 2006)
  override def checkGameStatus(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                               traceId: String): CheckGameStatusResp = {
    gameService.checkGameStatus(traceId, getMemberId, getSession.fingerPrint)
  }
}
