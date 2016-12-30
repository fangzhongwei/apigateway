package com.lawsofnature.apigateway.service

import javax.inject.Inject

import RpcSms.{SendLoginVerificationCodeRequest, SendLoginVerificationCodeResponse}
import com.lawsofnature.apigateway.domain.http.req.SendLoginVerificationCodeReq
import com.lawsofnature.sms.client.SmsClientService

/**
  * Created by fangzhongwei on 2016/12/27.
  */
trait SmsService {
  def sendLoginVerificationCode(traceId: String, req: SendLoginVerificationCodeReq): SendLoginVerificationCodeResponse
}

class SmsServiceImpl @Inject()(smsClientService: SmsClientService) extends SmsService {
  override def sendLoginVerificationCode(traceId: String, req: SendLoginVerificationCodeReq): SendLoginVerificationCodeResponse = {
    smsClientService.sendLoginVerificationCode(traceId, new SendLoginVerificationCodeRequest(req.deviceType, req.fingerPrint, req.mobile, req.resend, req.lastChannel))
  }
}
