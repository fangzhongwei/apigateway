package com.lawsofnature.apigateway.action

import javax.inject.Inject

import RpcSms.SendLoginVerificationCodeResponse
import com.lawsofnature.apigateway.annotations.{ApiMapping, Param}
import com.lawsofnature.apigateway.domain.http.req.SendLoginVerificationCodeReq
import com.lawsofnature.apigateway.domain.http.resp.SendLoginVerificationCodeResp
import com.lawsofnature.apigateway.enumerate.ParamSource
import com.lawsofnature.apigateway.service.SmsService
import com.lawsofnature.common.helper.IPv4Helper
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/12/27.
  */
trait SmsAction {
  def sendLoginVerificationCode(traceId: String, ip: String, sendLoginVerificationCodeReq: SendLoginVerificationCodeReq): SendLoginVerificationCodeResp
}

class SmsActionImpl @Inject()(smsService: SmsService) extends SmsAction {
  private[this] val logger: Logger = LoggerFactory.getLogger(getClass)

  @ApiMapping(id = 1001, ignoreSession = false)
  override def sendLoginVerificationCode(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                                         traceId: String,
                                         @Param(required = true, source = ParamSource.HEADER, name = "X-Real-Ip")
                                         ip: String,
                                         @Param(required = true, source = ParamSource.BODY)
                                         sendLoginVerificationCodeReq: SendLoginVerificationCodeReq): SendLoginVerificationCodeResp = {
    val response: SendLoginVerificationCodeResponse = smsService.sendLoginVerificationCode(traceId, IPv4Helper.ipToLong(ip), sendLoginVerificationCodeReq)
    SendLoginVerificationCodeResp(code = response.code, channel = response.channel)
  }
}