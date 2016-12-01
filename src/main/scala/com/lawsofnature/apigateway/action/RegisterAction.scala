package com.lawsofnature.apigateway.action

import javax.inject.Inject

import RpcMember.{BaseResponse, MemberIdentityExistsResponse}
import com.lawsofnature.apigateway.annotations.ApiMapping
import com.lawsofnature.apigateway.request.{CheckIdentityRequest, RegisterRequest}
import com.lawsofnature.apigateway.response.{ApiResponse, SuccessResponse}
import com.lawsofnature.apigateway.service.MemberService
import com.lawsofnature.common.exception.ErrorCode
import com.lawsofnature.common.helper.RegHelper
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/11/7.
  */
trait RegisterAction {
  def register(traceId: String, ip: String, registerRequest: RegisterRequest): ApiResponse

  def checkIdentity(traceId: String, ip: String, checkIdentityRequest: CheckIdentityRequest): ApiResponse
}

class RegisterActionImpl @Inject()(memberService: MemberService) extends RegisterAction {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  @ApiMapping(id = 1002, ignoreSession = true)
  def register(traceId: String, ip: String, registerRequest: RegisterRequest): ApiResponse = {
    val baseResponse: BaseResponse = memberService.register(traceId, ip, registerRequest)
    baseResponse.success match {
      case true =>
        ApiResponse.makeSuccessResponse(SuccessResponse.SUCCESS_REGISTER)
      case false =>
        ApiResponse.makeErrorResponse(ErrorCode.get(baseResponse.code))
    }
  }

  @ApiMapping(id = 1001, ignoreSession = true)
  override def checkIdentity(traceId: String, ip: String, checkIdentityRequest: CheckIdentityRequest): ApiResponse = {
    val pid: Int = checkIdentityRequest.pid
    val identity: String = checkIdentityRequest.i
    pid match {
      case 1 => RegHelper.isMobile(identity) match {
        case true => doCheck(traceId, pid, identity)
        case _ => ApiResponse.makeErrorResponse(ErrorCode.EC_UC_INVALID_MOBILE)
      }
      case 2 => RegHelper.isEmail(identity) match {
        case true => doCheck(traceId, pid, identity)
        case _ => ApiResponse.makeErrorResponse(ErrorCode.EC_UC_INVALID_EMAIL)
      }
      case _ => doCheck(traceId, pid, identity)
    }
  }

  def doCheck(traceId: String, pid: Int, identity: String): ApiResponse = {
    val identityExistsResponse: MemberIdentityExistsResponse = memberService.isMemberIdentityExists(traceId, identity)
    identityExistsResponse.success match {
      case true =>
        identityExistsResponse.exists match {
          case true =>
            pid match {
              case 0 => ApiResponse.makeErrorResponse(ErrorCode.EC_UC_USERNAME_TOKEN)
              case 1 => ApiResponse.makeErrorResponse(ErrorCode.EC_UC_MOBILE_TOKEN)
              case 2 => ApiResponse.makeErrorResponse(ErrorCode.EC_UC_EMAIL_TOKEN)
              case _ => ApiResponse.makeErrorResponse(ErrorCode.EC_INVALID_REQUEST)
            }
          case _ =>
            ApiResponse.makeSuccessResponse(SuccessResponse.SUCCESS)
        }
      case _ =>
        ApiResponse.makeErrorResponse(ErrorCode.get(identityExistsResponse.code))
    }
  }
}
