package com.lawsofnature.apigateway.action

import javax.inject.Inject

import RpcMember.{BaseResponse, MemberIdentityExistsResponse}
import com.lawsofnature.apigateway.annotations.{ApiMapping, Param}
import com.lawsofnature.apigateway.enumerate.ParamSource
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

  def checkIdentity(checkIdentityRequest: CheckIdentityRequest, traceId: String, lan: String): ApiResponse
}

class RegisterActionImpl @Inject()(memberService: MemberService) extends RegisterAction {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val HEADER_IP = "X-Real-Ip"

  @ApiMapping(id = 1002, ignoreSession = true)
  def register(@Param(required = true, source = ParamSource.HEADER, name = "TI")
               traceId: String,
               @Param(required = true, source = ParamSource.HEADER, name = "X-Real-Ip")
               ip: String,
               @Param(required = true, source = ParamSource.BODY)
               registerRequest: RegisterRequest): ApiResponse = {
    val pid: Int = registerRequest.pid
    val identity: String = registerRequest.i
    pid match {
      case 1 => RegHelper.isMobile(identity) match {
        case true => doRegister(traceId, ip, registerRequest)
        case _ => ApiResponse.makeErrorResponse(ErrorCode.EC_UC_INVALID_MOBILE)
      }
      case 2 => RegHelper.isEmail(identity) match {
        case true => doRegister(traceId, ip, registerRequest)
        case _ => ApiResponse.makeErrorResponse(ErrorCode.EC_UC_INVALID_EMAIL)
      }
      case _ => doRegister(traceId, ip, registerRequest)
    }
  }

  def doRegister(traceId: String, ip: String, registerRequest: RegisterRequest): ApiResponse = {
    val baseResponse: BaseResponse = memberService.register(traceId, ip, registerRequest)
    baseResponse.success match {
      case true =>
        ApiResponse.makeSuccessResponse(SuccessResponse.SUCCESS_REGISTER)
      case false =>
        ApiResponse.makeErrorResponse(ErrorCode.get(baseResponse.code))
    }
  }

  @ApiMapping(id = 1001, ignoreSession = true)
  override def checkIdentity(@Param(required = true, source = ParamSource.BODY)
                             checkIdentityRequest: CheckIdentityRequest,
                             @Param(required = true, source = ParamSource.HEADER, name = "TI")
                             traceId: String,
                             @Param(name = "lan")
                             lan: String): ApiResponse = {
    logger.info(s"lan is : $lan")
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
