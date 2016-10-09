package com.lawsofnature.service.impl

import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.response.ApiResponse
import com.lawsofnature.service.MemberService

import scala.concurrent.Future

/**
  * Created by fangzhongwei on 2016/10/10.
  */
class MemberServiceImpl extends MemberService {
  override def register(registerRequest: RegisterRequest): Future[Option[ApiResponse]] = {
    Future.successful(Some(ApiResponse("0", "11注册成功!111111")))
  }
}
