package com.lawsofnature.action

import javax.inject.Inject

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.lawsofnature.common.exception.ServiceErrorCode
import com.lawsofnature.enumeration.SuccessResponse
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.request.{CheckIdentityRequest, RegisterRequest}
import com.lawsofnature.service.MemberService
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/11/7.
  */
trait RegisterAction {
  def register(ip: String, registerRequest: RegisterRequest): Route

  def checkIdentity(checkIdentityRequest: CheckIdentityRequest): Route
}

class RegisterActionImpl @Inject()(memberService: MemberService) extends RegisterAction {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  def register(ip: String, registerRequest: RegisterRequest): Route = {
    registerRequest.validate() match {
      case Some(error) => ResponseFactory.serviceErrorResponse(error)
      case None => onSuccess(memberService.register(ip, registerRequest)) {
        case Some(response) =>
          response.success match {
            case true =>
              ResponseFactory.successConstResponse(SuccessResponse.SUCCESS_REGISTER)
            case false =>
              ResponseFactory.serviceErrorResponse(ServiceErrorCode.get(response.code))
          }
        case None =>
          ResponseFactory.commonErrorResponse()
      }
    }
  }

  override def checkIdentity(checkIdentityRequest: CheckIdentityRequest): Route = {
    checkIdentityRequest.validate() match {
      case Some(error) => ResponseFactory.serviceErrorResponse(error)
      case None => onSuccess(memberService.checkIdentity(checkIdentityRequest)) {
        case Some(response) =>
          response.success match {
            case true => checkIdentityRequest.pid match {
              case 0 => ResponseFactory.serviceErrorResponse(ServiceErrorCode.EC_UC_USERNAME_TOKEN)
              case 1 => ResponseFactory.serviceErrorResponse(ServiceErrorCode.EC_UC_MOBILE_TOKEN)
              case 2 => ResponseFactory.serviceErrorResponse(ServiceErrorCode.EC_UC_EMAIL_TOKEN)
            }
            case false => ResponseFactory.successConstResponse(SuccessResponse.SUCCESS)
          }
        case None =>
          ResponseFactory.commonErrorResponse()
      }
    }
  }
}
