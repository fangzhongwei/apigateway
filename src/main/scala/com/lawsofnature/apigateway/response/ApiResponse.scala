package com.lawsofnature.apigateway.response

import com.lawsofnature.common.exception.ErrorCode

/**
  * Created by fangzhongwei on 2016/10/10.
  */
case class ApiResponse private(code: Int = 0, msg: String = null, data: AnyRef = None)

object ApiResponse {
  private val successResponseCode = 0
  private val ERROR_RESPONSE_MAP: scala.collection.mutable.Map[ErrorCode, ApiResponse] = scala.collection.mutable.Map[ErrorCode, ApiResponse]()
  private val SUCCESS_RESPONSE_MAP: scala.collection.mutable.Map[SuccessResponse, ApiResponse] = scala.collection.mutable.Map[SuccessResponse, ApiResponse]()

  def makeErrorResponse(errorCode: ErrorCode): ApiResponse = {
    ERROR_RESPONSE_MAP.get(errorCode) match {
      case Some(response) => response
      case None =>
        val apiResponse: ApiResponse = new ApiResponse(code = errorCode.getCode, msg = errorCode.getMessage)
        ERROR_RESPONSE_MAP += (errorCode -> apiResponse)
        apiResponse
    }
  }

  def makeSuccessResponse(successResponse: SuccessResponse): ApiResponse = {
    SUCCESS_RESPONSE_MAP.get(successResponse) match {
      case Some(response) => response
      case None =>
        val apiResponse: ApiResponse = new ApiResponse(code = successResponseCode, msg = successResponse.getMsg)
        SUCCESS_RESPONSE_MAP += (successResponse -> apiResponse)
        apiResponse
    }
  }

  def make(code: Int = 0, msg: String = null, data: AnyRef = None): ApiResponse = new ApiResponse(code = code, msg = msg, data = data)
}