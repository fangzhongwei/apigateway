package com.lawsofnature.apigateway.service

import javax.inject.Inject

import RpcMember._
import com.lawsofnature.apigateway.request.{CheckIdentityRequest, RegisterRequest}
import com.lawsofnature.member.client.MemberClientService
import org.slf4j.LoggerFactory

/**
  * Created by fangzhongwei on 2016/10/10.
  */
trait MemberService {
  def register(traceId: String, ip: String, registerRequest: RegisterRequest): BaseResponse

  def getMemberByIdentity(traceId: String, checkIdentityRequest: CheckIdentityRequest): MemberResponse

  def isMemberIdentityExists(traceId: String, identity: String): ExistedResponse

  def isMemberUsernameExists(traceId: String, username: String): ExistedResponse

  def getMemberByMemberId(traceId: String, memberId:Long): MemberResponse
}

class MemberServiceImpl @Inject()(memberClient: MemberClientService) extends MemberService {
  override def register(traceId: String, ip: String, request: RegisterRequest): BaseResponse = memberClient.register(traceId, new MemberRegisterRequest(ip, request.lat, request.lng, request.dt, request.di, request.un, request.pid, request.i, request.pwd, request.ctry, request.pro, request.c, request.cty, request.addr))

  override def getMemberByIdentity(traceId: String, checkIdentityRequest: CheckIdentityRequest): MemberResponse = memberClient.getMemberByIdentity(traceId, checkIdentityRequest.i)

  override def isMemberIdentityExists(traceId: String, identity: String): ExistedResponse = memberClient.isMemberIdentityExists(traceId, identity)

  override def getMemberByMemberId(traceId: String, memberId: Long): MemberResponse = memberClient.getMemberByMemberId(traceId, memberId)

  override def isMemberUsernameExists(traceId: String, username: String): ExistedResponse = memberClient.isMemberUsernameExists(traceId, username)
}
