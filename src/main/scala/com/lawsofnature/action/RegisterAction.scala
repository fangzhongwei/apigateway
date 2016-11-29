package com.lawsofnature.action

import javax.inject.Inject

import com.lawsofnature.annotations.ApiMapping
import com.lawsofnature.common.exception.ServiceErrorCode
import com.lawsofnature.enumeration.SuccessResponse
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.request.{CheckIdentityRequest, RegisterRequest}
import com.lawsofnature.response.ApiResponse
import com.lawsofnature.service.MemberService
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by fangzhongwei on 2016/11/7.
  */
trait RegisterAction {
  def register(traceId: String, ip: String, registerRequest: RegisterRequest): Future[ApiResponse]

  def checkIdentity(traceId: String, ip: String, checkIdentityRequest: CheckIdentityRequest): Future[ApiResponse]
}

class RegisterActionImpl @Inject()(memberService: MemberService) extends RegisterAction {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  @ApiMapping(id = 1002, ignoreSession = true)
  def register(traceId: String, ip: String, registerRequest: RegisterRequest): Future[ApiResponse] = {
    val promise: Promise[ApiResponse] = Promise[ApiResponse]()
    Future {
      registerRequest.validate() match {
        case Some(error) => promise.success(ResponseFactory.serviceErrorResponse(error))
        case None => (memberService.register(traceId, ip, registerRequest)) onComplete {
          case Success(maybeBaseResponse) => maybeBaseResponse match {
            case Some(response) =>
              response.success match {
                case true =>
                  promise.success(ResponseFactory.successConstResponse(SuccessResponse.SUCCESS_REGISTER))
                case false =>
                  promise.success(ResponseFactory.serviceErrorResponse(ServiceErrorCode.get(response.code)))
              }
            case None =>
              promise.success(ResponseFactory.commonErrorResponse())
          }
        }
      }
    }
    promise.future
  }

  @ApiMapping(id = 1001, ignoreSession = true)
  override def checkIdentity(traceId: String, ip: String, checkIdentityRequest: CheckIdentityRequest): Future[ApiResponse] = {
    val promise: Promise[ApiResponse] = Promise[ApiResponse]()
    Future {
      checkIdentityRequest.validate() match {
        case Some(error) => promise.success(ResponseFactory.serviceErrorResponse(error))
        case None =>
          val millis: Long = System.currentTimeMillis()
          (memberService.isMemberIdentityExists(traceId, checkIdentityRequest.i)) onComplete {
          case Success(memberIdentityExistsResponse) => memberIdentityExistsResponse match {
            case Some(exists) =>
              logger.info(traceId + " remote call checkIdentity cost:" + (System.currentTimeMillis() - millis))
              exists match {
                case true =>
                  checkIdentityRequest.pid match {
                  case 0 => promise.success(ResponseFactory.serviceErrorResponse(ServiceErrorCode.EC_UC_USERNAME_TOKEN))
                  case 1 => promise.success(ResponseFactory.serviceErrorResponse(ServiceErrorCode.EC_UC_MOBILE_TOKEN))
                  case 2 => promise.success(ResponseFactory.serviceErrorResponse(ServiceErrorCode.EC_UC_EMAIL_TOKEN))
                }
                case false =>
                  promise.success(ResponseFactory.successConstResponse(SuccessResponse.SUCCESS))
              }
            case None =>
              promise.success(ResponseFactory.commonErrorResponse())
          }
          case Failure(ex) =>
            logger.error(traceId, ex)
            promise.failure(ex)
        }
      }
    }
    promise.future
  }
}
