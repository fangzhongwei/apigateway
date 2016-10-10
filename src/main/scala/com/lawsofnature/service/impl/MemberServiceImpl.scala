package com.lawsofnature.service.impl

import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.response.ApiResponse
import com.lawsofnature.service.MemberService
import com.lawsofnature.member.client.{MemberClient, Pid}

import scala.concurrent.Future

/**
  * Created by fangzhongwei on 2016/10/10.
  */
class MemberServiceImpl extends MemberService {
  override def register(registerRequest: RegisterRequest): Future[Option[ApiResponse]] = {
    new Pid().Email
    new MemberClient
    Future.successful(Some(ApiResponse("0", "register success!")))
  }
}
