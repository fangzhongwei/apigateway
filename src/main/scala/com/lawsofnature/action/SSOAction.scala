package com.lawsofnature.action

import javax.inject.Inject

import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.request.AppLoginRequest
import com.lawsofnature.response.ApiResponse
import com.lawsofnature.service.SessionService

import scala.concurrent.{Future, Promise}
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by fangzhongwei on 2016/11/23.
  */
trait SSOAction {
  def login(traceId: String, ip: String, request: AppLoginRequest): Future[ApiResponse]
}

class SSOActionImpl @Inject()(sessionService: SessionService) extends SSOAction {
  override def login(traceId: String, ip: String, request: AppLoginRequest): Future[ApiResponse] = {
    val promise: Promise[ApiResponse] = Promise[ApiResponse]()
    Future {
      request.validate() match {
        case Some(errorCode) => ResponseFactory.serviceErrorResponse(errorCode)
        case None => sessionService.login(traceId, ip, request) onComplete{
          case Success(sessionResponse) => sessionResponse match {
            case Some(response) =>
              promise.success(new ApiResponse(0, "", response))
            case None =>
              promise.success(ResponseFactory.commonErrorResponse())
          }
        }
      }
    }
    promise.future
  }
}
