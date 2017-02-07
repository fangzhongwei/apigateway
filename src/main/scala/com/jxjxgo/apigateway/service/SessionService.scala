package com.jxjxgo.apigateway.service

import javax.inject.Inject

import com.jxjxgo.apigateway.domain.http.req.login.LoginReq
import com.jxjxgo.apigateway.domain.http.resp.LoginResp
import com.jxjxgo.common.exception.ErrorCode
import com.jxjxgo.edcenter.rpc.domain.{EdServiceEndpoint, EncryptResponse}
import com.jxjxgo.memberber.rpc.domain.MemberResponse
import com.jxjxgo.sms.rpc.domain.SmsBaseResponse
import com.jxjxgo.sso.rpc.domain.{CreateSessionRequest, SSOBaseResponse, SSOServiceEndpoint, SessionResponse}
import com.twitter.util.{Await, Future}

/**
  * Created by fangzhongwei on 2016/11/25.
  */
trait SessionService {
  def loginByToken(traceId: String, session: SessionResponse): LoginResp

  def login(traceId: String, ip: Long, request: LoginReq): LoginResp

  def logout(traceId: String, token: String): SSOBaseResponse

  def touch(traceId: String, token: String): SessionResponse
}

class SessionServiceImpl @Inject()(edClientService: EdServiceEndpoint[Future], ssoClientService: SSOServiceEndpoint[Future], smsService: SmsService, memberService: MemberService) extends SessionService {
  override def login(traceId: String, ip: Long, r: LoginReq): LoginResp = {

    val encryptResponse: EncryptResponse = Await.result(edClientService.encrypt(traceId, r.mobile))
    encryptResponse.code match {
      case "0" =>
        val ticket: String = encryptResponse.ticket
        val baseResponse: SmsBaseResponse = smsService.verifyLoginVerificationCode(traceId, ip, r, ticket)
        baseResponse.code match {
          case "0" =>
            val memberResponse: MemberResponse = memberService.getMemberByMobile(traceId, ticket)
            memberResponse.code match {
              case "0" =>
                val sessionResponse: SessionResponse = Await.result(ssoClientService.createSession(traceId, CreateSessionRequest(r.clientId, r.version, ip, r.deviceType, r.fingerPrint, memberResponse.memberId)))
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

  override def logout(traceId: String, token: String): SSOBaseResponse = Await.result(ssoClientService.logout(traceId, token))

  override def touch(traceId: String, token: String): SessionResponse = Await.result(ssoClientService.touch(traceId, token))

  override def loginByToken(traceId: String, session: SessionResponse): LoginResp = {
    val memberResponse: MemberResponse = memberService.getMemberByMemberId(traceId, session.memberId)
    memberResponse.code match {
      case "0" =>
        memberResponse.status match {
          case -1 => LoginResp(code = ErrorCode.EC_UC_MEMBER_ACCOUNT_FREEZE.getCode)
          case _ => LoginResp(code = "0",
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
