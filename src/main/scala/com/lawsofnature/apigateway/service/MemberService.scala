package com.lawsofnature.apigateway.service

import javax.inject.Inject

import RpcMember._
import com.lawsofnature.member.client.MemberClientService

/**
  * Created by fangzhongwei on 2016/10/10.
  */
trait MemberService {
  def updateNickName(traceId: String, memberId: Long, nickName: String): BaseResponse

  def getMemberByMemberId(traceId: String, memberId: Long): MemberResponse

  def getMemberByMobile(traceId: String, mobileTicket: String): MemberResponse
}

class MemberServiceImpl @Inject()(memberClient: MemberClientService) extends MemberService {
  override def getMemberByMemberId(traceId: String, memberId: Long): MemberResponse = memberClient.getMemberById(traceId, memberId)

  override def getMemberByMobile(traceId: String, mobileTicket: String): MemberResponse = memberClient.getMemberByMobile(traceId, mobileTicket)

  override def updateNickName(traceId: String, memberId: Long, nickName: String): BaseResponse = memberClient.updateNickName(traceId, memberId, nickName)
}
