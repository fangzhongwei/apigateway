package com.lawsofnature.service

import javax.inject.Inject

import RpcMember.MemberCarrier
import com.lawsofnature.member.client.MemberClientService
import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.response.ApiResponse
import org.slf4j.LoggerFactory

import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/10/10.
  */
trait MemberService {
  def register(registerRequest: RegisterRequest): Future[Option[ApiResponse]]
}

class MemberServiceImpl @Inject()(memberClient: MemberClientService) extends MemberService {
  val logger = LoggerFactory.getLogger(this.getClass)

  override def register(registerRequest: RegisterRequest): Future[Option[ApiResponse]] = {
    val response = Promise[Option[ApiResponse]]()

    Future {
      logger.info("register request: {}", registerRequest)
      val carrier = new MemberCarrier(0, registerRequest.dt, registerRequest.fp, registerRequest.un, registerRequest.pid, mobile, email, registerRequest.pwd)
      memberClient.register(carrier).success match {
        case true =>
          response.success(Some(ApiResponse("0", "register success!")))
        case false =>
          response.success(Some(ApiResponse(r.code, r.message)))
      }
    }
    response.future
  }
}