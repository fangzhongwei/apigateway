package com.lawsofnature.apigateway.service

import javax.inject.Inject

import RpcEd.EncryptResponse
import RpcMember.MemberResponse
import RpcSSO.{CreateSessionRequest, SSOBaseResponse, SessionResponse}
import RpcSms.BaseResponse
import com.lawsofnature.apigateway.domain.http.req.login.LoginReq
import com.lawsofnature.apigateway.domain.http.resp.LoginResp
import com.lawsofnature.common.exception.ErrorCode
import com.lawsofnature.edcenter.client.EdClientService
import com.lawsofnature.sso.client.SSOClientService

/**
  * Created by fangzhongwei on 2016/11/25.
  */
trait SessionService {
  def loginByToken(traceId: String, session: SessionResponse): LoginResp

  def login(traceId: String, ip: Long, request: LoginReq): LoginResp

  def logout(traceId: String, token: String): SSOBaseResponse

  def touch(traceId: String, token: String): SessionResponse
}

class SessionServiceImpl @Inject()(edClientService: EdClientService, ssoClientService: SSOClientService, smsService: SmsService, memberService: MemberService) extends SessionService {
  override def login(traceId: String, ip: Long, r: LoginReq): LoginResp = {

    val encryptResponse: EncryptResponse = edClientService.encrypt(traceId, r.mobile)
    encryptResponse.code match {
      case "0" =>
        val ticket: String = encryptResponse.ticket
        val baseResponse: BaseResponse = smsService.verifyLoginVerificationCode(traceId, ip, r, ticket)
        baseResponse.code match {
          case "0" =>
            val memberResponse: MemberResponse = memberService.getMemberByMobile(traceId, ticket)
            memberResponse.code match {
              case "0" =>
                val sessionResponse: SessionResponse = ssoClientService.createSession(traceId, new CreateSessionRequest(r.clientId, r.version, ip, r.deviceType, r.fingerPrint, memberResponse.memberId))
                LoginResp(code = "0",
                  token = sessionResponse.token,
                  mobile = sessionResponse.identity,
                  status = memberResponse.status,
                  nickName = memberResponse.nickName)
              case _ =>
                LoginResp(code = memberResponse.code)
            }
          case _ =>
            LoginResp(code = baseResponse.code)
        }
      case _ =>
        LoginResp(code = encryptResponse.code)
    }
  }

  override def logout(traceId: String, token: String): SSOBaseResponse = ssoClientService.logout(traceId, token)

  override def touch(traceId: String, token: String): SessionResponse = ssoClientService.touch(traceId, token)

  override def loginByToken(traceId: String, session: SessionResponse): LoginResp = {
    val memberResponse: MemberResponse = memberService.getMemberByMemberId(traceId, session.memberId)
    memberResponse.code match {
      case "0" =>
        memberResponse.status match {
          case -1 => LoginResp(code = ErrorCode.EC_UC_MEMBER_ACCOUNT_FREEZE.getCode)
          case _ =>  LoginResp(code = "0",
            token = session.token,
            mobile = session.identity,
            status = memberResponse.status,
            nickName = memberResponse.nickName)
        }
      case _ =>
        LoginResp(code = memberResponse.code)
    }
  }
}
