package com.lawsofnature.service

import javax.inject.Inject

import RpcMember.{MemberCarrier, RegisterResponse}
import com.lawsofnature.common.exception.ServiceErrorCode._
import com.lawsofnature.common.exception.ServiceException
import com.lawsofnature.helper.RegHelper
import com.lawsofnature.member.client.MemberClientService
import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.response.ApiResponse
import org.slf4j.LoggerFactory

import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/10/10.
  */
trait MemberService {
  def register(registerRequest: RegisterRequest):Future[Option[ApiResponse]]
}

class MemberServiceImpl @Inject()(memberClient: MemberClientService) extends MemberService {
  val logger = LoggerFactory.getLogger(this.getClass)

  override def register(registerRequest: RegisterRequest): Future[Option[ApiResponse]] = {

    val response = Promise[Option[ApiResponse]]()

    logger.info("register request12:" + registerRequest)

    if(1 == 1) throw new ServiceException(EC_UC_PASSWORD_LENGTH_LIMIT)


    val mobile: String = registerRequest.m
    val email: String = registerRequest.e
    val username: String = registerRequest.un
    val pid: Int = registerRequest.pid

//    if (1 == pid && !RegHelper.isMobile(mobile)) {
//       response.success(Some(ApiResponse("1", "invalid mobile")))
//    } else if (2 == pid && !RegHelper.isEmail(email)) {
//      response.success(Some(ApiResponse("2", "invalid email")))
//    } else {
//      if (1 == pid) {
//        val memberCarrier: MemberCarrier = memberClient.getMemberByMobile(mobile)
//        if (memberCarrier != null) {
//          response.success(Some(ApiResponse("1", "taken mobile")))
//
//          response.future
//        }
//      } else if (2 == pid) {
//        val memberCarrier: MemberCarrier = memberClient.getMemberByEmail(email)
//        if (memberCarrier != null) {
//          response.success(Some(ApiResponse("2", "taken email")))
//        }
//      } else {
//        response.success(Some(ApiResponse("3", "invalid pid")))
//      }
//
//      if (memberClient.getMemberByUsername(username) != null) {
//        response.success(Some(ApiResponse("5", "username taken")))
//      } else {
//        val carrier = new MemberCarrier(0, registerRequest.dt, registerRequest.fp, registerRequest.un, registerRequest.pid, mobile, email, registerRequest.pwd)
//        val r: RegisterResponse = memberClient.register(carrier)
//
//        if (r.success) {
//          response.success(Some(ApiResponse("0", "register success!")))
//        } else {
//          response.success(Some(ApiResponse(r.code, r.message)))
//        }
//      }
//    }

    response.future
  }
}