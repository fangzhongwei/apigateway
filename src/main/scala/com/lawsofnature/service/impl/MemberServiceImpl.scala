package com.lawsofnature.service.impl

import javax.inject.Inject

import RpcMember.{MemberCarrier, RegisterResponse}
import com.lawsofnature.helper.RegHelper
import com.lawsofnature.member.client.MemberClientService
import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.response.ApiResponse
import com.lawsofnature.service.MemberService
import org.slf4j.LoggerFactory

import scala.concurrent.Future

/**
  * Created by fangzhongwei on 2016/10/10.
  */
class MemberServiceImpl @Inject()(memberClient: MemberClientService) extends MemberService {
  val logger = LoggerFactory.getLogger(this.getClass)

  override def register(registerRequest: RegisterRequest): Future[Option[ApiResponse]] = {
    if (!memberClient.isInitial) {
      memberClient.init
    }

    logger.info("register request:" + registerRequest)
    val mobile: String = registerRequest.m
    val email: String = registerRequest.e
    val username: String = registerRequest.un
    val pid: Int = registerRequest.pid

    if (1 == pid && !RegHelper.isMobile(mobile)) {
      return Future.successful(Some(ApiResponse("1", "invalid mobile")))
    } else if (2 == pid && !RegHelper.isEmail(email)) {
      return Future.successful(Some(ApiResponse("2", "invalid email")))
    } else {
      if (1 == pid) {
        val memberCarrier: MemberCarrier = memberClient.getMemberByMobile(mobile)
        if (memberCarrier != null) {
          return Future.successful(Some(ApiResponse("1", "taken mobile")))
        }
      } else if (2 == pid) {
        val memberCarrier: MemberCarrier = memberClient.getMemberByEmail(email)
        if (memberCarrier != null) {
          return Future.successful(Some(ApiResponse("2", "taken email")))
        }
      } else {
        return Future.successful(Some(ApiResponse("3", "invalid pid")))
      }

      if (memberClient.getMemberByUsername(username) != null) {
        return Future.successful(Some(ApiResponse("5", "username taken")))
      } else {
        val carrier = new MemberCarrier(0, registerRequest.dt, registerRequest.fp, registerRequest.un, registerRequest.pid, mobile, email, registerRequest.pwd)
        val response: RegisterResponse = memberClient.register(carrier)

        if (response.success) {
          return Future.successful(Some(ApiResponse("0", "register success!")))
        } else {
          return Future.successful(Some(ApiResponse(response.code, response.message)))
        }
      }
    }
  }
}