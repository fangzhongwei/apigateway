package com.lawsofnature.service

import javax.inject.Inject

import RpcMember.{BaseResponse, MemberRegisterRequest}
import com.lawsofnature.enumeration.SuccessResponse
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.member.client.MemberClientService
import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.response.ApiResponse
import org.slf4j.LoggerFactory

import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/10/10.
  */
trait MemberService {
  def register(registerRequest: RegisterRequest): Future[Option[BaseResponse]]
}

class MemberServiceImpl @Inject()(memberClient: MemberClientService) extends MemberService {
  val logger = LoggerFactory.getLogger(this.getClass)

  override def register(registerRequest: RegisterRequest): Future[Option[BaseResponse]] = {
    val response = Promise[Option[BaseResponse]]()
    Future {
      logger.info("register request: {}", registerRequest)
      val carrier = new MemberRegisterRequest("ip", registerRequest.dt, registerRequest.di, registerRequest.un, registerRequest.pid, registerRequest.i, registerRequest.pwd)
      val baseResponse: BaseResponse = memberClient.register(registerRequest.ti, carrier)
      response.success(Some(baseResponse))
    }
    response.future
  }
}