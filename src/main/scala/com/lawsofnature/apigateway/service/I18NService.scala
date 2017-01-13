package com.lawsofnature.apigateway.service

import javax.inject.Inject

import RpcI18N.{PullResourceRequest, ResourceResponse}
import com.lawsofnature.i18n.client.I18NClientService

/**
  * Created by fangzhongwei on 2017/1/13.
  */
trait I18NService {
  def getLatest(traceId: String, lan: String): ResourceResponse

  def pullLatest(traceId: String, request: PullResourceRequest): ResourceResponse
}

class I18NServiceImpl @Inject()(i18NClientService: I18NClientService) extends I18NService {
  override def getLatest(traceId: String, lan: String): ResourceResponse = i18NClientService.getLatest(traceId, lan)

  override def pullLatest(traceId: String, request: PullResourceRequest): ResourceResponse = i18NClientService.pullLatest(traceId, request)
}
