package com.lawsofnature.service

import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.response.ApiResponse

import scala.concurrent.Future

/**
  * Created by fangzhongwei on 2016/10/10.
  */
trait MemberService {
  def register(registerRequest: RegisterRequest):Future[Option[ApiResponse]]
}
