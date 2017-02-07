package com.jxjxgo.apigateway.service

import javax.inject.Inject

import com.jxjxgo.memberber.rpc.domain.{MemberBaseResponse, MemberEndpoint, MemberResponse}
import com.twitter.util.{Await, Future}

/**
  * Created by fangzhongwei on 2016/10/10.
  */
trait MemberService {
  def updateNickName(traceId: String, memberId: Long, nickName: String): MemberBaseResponse

  def getMemberByMemberId(traceId: String, memberId: Long): MemberResponse

  def getMemberByMobile(traceId: String, mobileTicket: String): MemberResponse
}

class MemberServiceImpl @Inject()(memberClient: MemberEndpoint[Future]) extends MemberService {
  override def getMemberByMemberId(traceId: String, memberId: Long): MemberResponse = Await.result(memberClient.getMemberById(traceId, memberId))

  override def getMemberByMobile(traceId: String, mobileTicket: String): MemberResponse = Await.result(memberClient.getMemberByMobile(traceId, mobileTicket))

  override def updateNickName(traceId: String, memberId: Long, nickName: String): MemberBaseResponse = Await.result(memberClient.updateNickName(traceId, memberId, nickName))
}
