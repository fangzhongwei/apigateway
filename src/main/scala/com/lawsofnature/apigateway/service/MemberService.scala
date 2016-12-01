package com.lawsofnature.apigateway.service

import javax.inject.Inject

import RpcMember.{BaseResponse, MemberIdentityExistsResponse, MemberRegisterRequest, MemberResponse}
import com.lawsofnature.apigateway.request.{CheckIdentityRequest, RegisterRequest}
import com.lawsofnature.member.client.MemberClientService
import org.slf4j.LoggerFactory

/**
  * Created by fangzhongwei on 2016/10/10.
  */
trait MemberService {
  def register(traceId: String, ip: String, registerRequest: RegisterRequest): BaseResponse

  def getMemberByIdentity(traceId: String, checkIdentityRequest: CheckIdentityRequest): MemberResponse

  def isMemberIdentityExists(traceId: String, identity: String): MemberIdentityExistsResponse
}

class MemberServiceImpl @Inject()(memberClient: MemberClientService) extends MemberService {
  override def register(traceId: String, ip: String, request: RegisterRequest): BaseResponse = memberClient.register(traceId, new MemberRegisterRequest(ip, request.lat, request.lng, request.dt, request.di, request.un, request.pid, request.i, request.pwd, request.ctry, request.pro, request.c, request.cty, request.addr))

  override def getMemberByIdentity(traceId: String, checkIdentityRequest: CheckIdentityRequest): MemberResponse = memberClient.getMemberByIdentity(traceId, checkIdentityRequest.i)

  override def isMemberIdentityExists(traceId: String, identity: String): MemberIdentityExistsResponse = memberClient.isMemberIdentityExists(traceId, identity)
}
