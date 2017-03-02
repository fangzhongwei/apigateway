package com.jxjxgo.apigateway.service

import javax.inject.Inject

import com.jxjxgo.gamecenter.rpc.domain.{CheckGameStatusRequest, CheckGameStatusResponse, GameEndpoint}
import com.twitter.util.{Await, Future}

/**
  * Created by fangzhongwei on 2017/3/1.
  */
trait GameService {
  def  checkGameStatus(traceId:String, memberId:Long, fingerPrint:String):CheckGameStatusResponse
}

class GameServiceImpl @Inject()(gameEndpoint: GameEndpoint[Future]) extends GameService {
  override def checkGameStatus(traceId:String, memberId:Long, fingerPrint:String): CheckGameStatusResponse = {
    Await.result(gameEndpoint.checkGameStatus(traceId, CheckGameStatusRequest(memberId = memberId, fingerPrint = fingerPrint)))
  }
}
