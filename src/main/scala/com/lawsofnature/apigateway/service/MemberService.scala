package com.lawsofnature.apigateway.service

import javax.inject.Inject

import RpcMember.{BaseResponse, MemberIdentityExistsResponse, MemberRegisterRequest, MemberResponse}
import com.lawsofnature.common.exception.{ServiceErrorCode, ServiceException}
import com.lawsofnature.member.client.MemberClientService
import com.lawsofnature.apigateway.request.{CheckIdentityRequest, RegisterRequest}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/10/10.
  */
trait MemberService {
  def register(traceId: String, ip: String, registerRequest: RegisterRequest): Future[Option[BaseResponse]]

  def getMemberByIdentity(traceId: String, checkIdentityRequest: CheckIdentityRequest): Future[Option[MemberResponse]]

  def isMemberIdentityExists(traceId: String, identity: String): Future[Option[Boolean]]
}

class MemberServiceImpl @Inject()(memberClient: MemberClientService) extends MemberService {
  val logger = LoggerFactory.getLogger(this.getClass)

  override def register(traceId: String, ip: String, request: RegisterRequest): Future[Option[BaseResponse]] = {
    val response = Promise[Option[BaseResponse]]()
    Future {
      logger.info("register request: {}", request)
      response.success(Some(memberClient.register(traceId, new MemberRegisterRequest(ip, request.lat, request.lng, request.dt, request.di, request.un, request.pid, request.i, request.pwd,request.ctry, request.pro, request.c, request.cty, request.addr))))
    }
    response.future
  }

  override def getMemberByIdentity(traceId: String, checkIdentityRequest: CheckIdentityRequest): Future[Option[MemberResponse]] = {
    val response = Promise[Option[MemberResponse]]()
    Future {
      response.success(Some(memberClient.getMemberByIdentity(traceId, checkIdentityRequest.i)))
    }
    response.future
  }

  override def isMemberIdentityExists(traceId: String, identity: String): Future[Option[Boolean]] = {
    val response = Promise[Option[Boolean]]()
    Future {
      val memberIdentityExistsResponse: MemberIdentityExistsResponse = memberClient.isMemberIdentityExists(traceId, identity)
      memberIdentityExistsResponse.success match {
        case true =>
          response.success(Some(memberIdentityExistsResponse.exists))
        case false =>
          response.failure(ServiceException.make(ServiceErrorCode.get(memberIdentityExistsResponse.code)))
      }
    }
    response.future
  }
}
