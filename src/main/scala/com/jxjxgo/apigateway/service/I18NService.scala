package com.jxjxgo.apigateway.service

import javax.inject.Inject

import com.jxjxgo.i18n.rpc.domain.{I18NEndpoint, PullResourceRequest, ResourceResponse}
import com.twitter.util.{Await, Future}

/**
  * Created by fangzhongwei on 2017/1/13.
  */
trait I18NService {
  def getLatest(traceId: String, lan: String): ResourceResponse

  def pullLatest(traceId: String, request: PullResourceRequest): ResourceResponse
}

class I18NServiceImpl @Inject()(i18NClientService: I18NEndpoint[Future]) extends I18NService {
  override def getLatest(traceId: String, lan: String): ResourceResponse = Await.result(i18NClientService.getLatest(traceId, lan))

  override def pullLatest(traceId: String, request: PullResourceRequest): ResourceResponse = Await.result(i18NClientService.pullLatest(traceId, request))
}
