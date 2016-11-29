package com.lawsofnature.apigateway.action

import javax.inject.Inject

import com.lawsofnature.apigateway.annotations.ApiMapping
import com.lawsofnature.apigateway.annotations.ApiMapping
import com.lawsofnature.apigateway.factory.ResponseFactory
import com.lawsofnature.apigateway.request.AppLoginRequest
import com.lawsofnature.apigateway.response.ApiResponse
import com.lawsofnature.apigateway.service.SessionService

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

  @ApiMapping(id = 2001, ignoreSession = true)
  override def login(traceId: String, ip: String, request: AppLoginRequest): Future[ApiResponse] = {
    val promise: Promise[ApiResponse] = Promise[ApiResponse]()
    Future {
      request.validate() match {
        case Some(errorCode) => promise.success(ResponseFactory.serviceErrorResponse(errorCode))
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
