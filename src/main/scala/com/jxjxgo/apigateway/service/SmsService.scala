package com.jxjxgo.apigateway.service

import javax.inject.Inject

import com.jxjxgo.apigateway.domain.http.req.login.LoginReq
import com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq
import com.jxjxgo.edcenter.rpc.domain.{EdServiceEndpoint, EncryptResponse}
import com.jxjxgo.sms.rpc.domain._
import com.twitter.util.{Await, Future}

/**
  * Created by fangzhongwei on 2016/12/27.
  */
trait SmsService {
  def sendLoginVerificationCode(traceId: String, ip: Long, req: SendLoginVerificationCodeReq): SendLoginVerificationCodeResponse

  def verifyLoginVerificationCode(traceId: String, ip: Long, req: LoginReq, identityTicket: String): SmsBaseResponse
}

class SmsServiceImpl @Inject()(smsClientService: SmsServiceEndpoint[Future], edClientService: EdServiceEndpoint[Future]) extends SmsService {
  private[this] val sendLoginVerificationCodeSmsType = 1

  override def sendLoginVerificationCode(traceId: String, ip: Long, req: SendLoginVerificationCodeReq): SendLoginVerificationCodeResponse = {
    val response: EncryptResponse = Await.result(edClientService.encrypt(traceId, req.mobile))
    response.code match {
      case "0" =>
        Await.result(smsClientService.sendLoginVerificationCode(traceId, SendLoginVerificationCodeRequest(ip, req.deviceType, req.fingerPrint, response.ticket, sendLoginVerificationCodeSmsType, req.resend.equals("1"), req.lastChannel)))
      case _ => SendLoginVerificationCodeResponse(code = response.code)
    }
  }

  override def verifyLoginVerificationCode(traceId: String, ip: Long, req: LoginReq, identityTicket: String): SmsBaseResponse = {
    Await.result(smsClientService.verifyLoginVerificationCode(traceId, VerifyLoginVerificationCodeRequest(ip, req.deviceType, req.fingerPrint, identityTicket, sendLoginVerificationCodeSmsType, req.verificationCode)))
  }
}
