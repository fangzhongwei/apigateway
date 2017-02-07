package com.jxjxgo.apigateway.action

import javax.inject.Inject

import com.jxjxgo.apigateway.annotations.{ApiMapping, Param}
import com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq
import com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp
import com.jxjxgo.apigateway.enumerate.ParamSource
import com.jxjxgo.apigateway.service.SmsService
import com.jxjxgo.common.helper.IPv4Helper
import com.jxjxgo.sms.rpc.domain.SendLoginVerificationCodeResponse
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