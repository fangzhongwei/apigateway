package com.lawsofnature.apigateway.action

import javax.inject.Inject

import com.lawsofnature.apigateway.annotations.{ApiMapping, Param}
import com.lawsofnature.apigateway.domain.http.req.SendLoginVerificationCodeReq
import com.lawsofnature.apigateway.domain.http.resp.SendLoginVerificationCodeResp
import com.lawsofnature.apigateway.enumerate.ParamSource
import com.lawsofnature.apigateway.service.SmsService
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/12/27.
  */
trait SmsAction {
  def sendLoginVerificationCode(traceId: String, sendLoginVerificationCodeReq: SendLoginVerificationCodeReq): SendLoginVerificationCodeResp
}

class SmsActionImpl @Inject()(smsService: SmsService) extends SmsAction {
  private[this] val logger: Logger = LoggerFactory.getLogger(getClass)

  @ApiMapping(id = 1001, ignoreSession = false)
  override def sendLoginVerificationCode(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                                         traceId: String,
                                         @Param(required = true, source = ParamSource.BODY)
                                         sendLoginVerificationCodeReq: SendLoginVerificationCodeReq): SendLoginVerificationCodeResp = {
    logger.info(s"send code, request:$sendLoginVerificationCodeReq")
    //    smsService.sendLoginVerificationCode(traceId, sendLoginVerificationCodeReq)
    SendLoginVerificationCodeResp("1", "操作过于频繁，请稍候再试。")
  }
}