package com.lawsofnature.service

import javax.inject.Inject

import RpcMember.{BaseResponse, MemberRegisterRequest, MemberResponse}
import com.lawsofnature.member.client.MemberClientService
import com.lawsofnature.request.{CheckIdentityRequest, RegisterRequest}
import org.slf4j.LoggerFactory

import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/10/10.
  */
trait MemberService {
  def register(ip: String, registerRequest: RegisterRequest): Future[Option[BaseResponse]]

  def checkIdentity(checkIdentityRequest: CheckIdentityRequest): Future[Option[MemberResponse]]
}

class MemberServiceImpl @Inject()(memberClient: MemberClientService) extends MemberService {
  val logger = LoggerFactory.getLogger(this.getClass)

  override def register(ip: String, registerRequest: RegisterRequest): Future[Option[BaseResponse]] = {
    val response = Promise[Option[BaseResponse]]()
    Future {
      logger.info("register request: {}", registerRequest)
      response.success(Some(memberClient.register(registerRequest.ti, new MemberRegisterRequest(ip, registerRequest.lat, registerRequest.lng, registerRequest.dt, registerRequest.di, registerRequest.un, registerRequest.pid, registerRequest.i, registerRequest.pwd))))
    }
    response.future
  }

  override def checkIdentity(checkIdentityRequest: CheckIdentityRequest): Future[Option[MemberResponse]] = {
    val response = Promise[Option[MemberResponse]]()
    Future {
      response.success(Some(memberClient.getMemberByIdentity(checkIdentityRequest.ti, checkIdentityRequest.i, checkIdentityRequest.pid)))
    }
    response.future
  }
}