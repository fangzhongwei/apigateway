package com.lawsofnature.apigateway.service

import javax.inject.Inject

import RpcEd.EncryptResponse
import RpcSms.{BaseResponse, SendLoginVerificationCodeRequest, SendLoginVerificationCodeResponse, VerifyLoginVerificationCodeRequest}
import com.lawsofnature.apigateway.domain.http.req.{LoginReq, SendLoginVerificationCodeReq}
import com.lawsofnature.edcenter.client.EdClientService
import com.lawsofnature.sms.client.SmsClientService

/**
  * Created by fangzhongwei on 2016/12/27.
  */
trait SmsService {
  def sendLoginVerificationCode(traceId: String, ip: Long, req: SendLoginVerificationCodeReq): SendLoginVerificationCodeResponse

  def verifyLoginVerificationCode(traceId: String, ip: Long, req: LoginReq, identityTicket: String): BaseResponse
}

class SmsServiceImpl @Inject()(smsClientService: SmsClientService, edClientService: EdClientService) extends SmsService {
  private[this] val sendLoginVerificationCodeSmsType = 1

  override def sendLoginVerificationCode(traceId: String, ip: Long, req: SendLoginVerificationCodeReq): SendLoginVerificationCodeResponse = {
    val response: EncryptResponse = edClientService.encrypt(traceId, req.mobile)
    response.code match {
      case "0" =>
        smsClientService.sendLoginVerificationCode(traceId, new SendLoginVerificationCodeRequest(ip, req.deviceType, req.fingerPrint, response.ticket, sendLoginVerificationCodeSmsType, req.resend, req.lastChannel))
      case _ =>
        val response: SendLoginVerificationCodeResponse = new RpcSms.SendLoginVerificationCodeResponse()
        response.code = response.code
        response
    }
  }

  override def verifyLoginVerificationCode(traceId: String, ip: Long, req: LoginReq, identityTicket: String): BaseResponse = {
    smsClientService.verifyLoginVerificationCode(traceId, new VerifyLoginVerificationCodeRequest(ip, req.deviceType, req.fingerPrint, identityTicket, sendLoginVerificationCodeSmsType, req.verificationCode))
  }
}
