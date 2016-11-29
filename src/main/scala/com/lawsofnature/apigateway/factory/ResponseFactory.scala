package com.lawsofnature.apigateway.factory

import com.lawsofnature.apigateway.enumeration.SuccessResponse
import com.lawsofnature.apigateway.response.ApiResponse
import com.lawsofnature.common.exception.ErrorCode
import com.lawsofnature.common.exception.ErrorCode._

/**
  * cache response, avoid create every time
  * Created by fangzhongwei on 2016/11/3.
  */
object ResponseFactory {
  private val successResponseCode = 0

  //error code cache
  private val successConstResponseMap = scala.collection.mutable.HashMap[SuccessResponse.SuccessResponse, ApiResponse]()
  SuccessResponse.values.foreach(v => successConstResponseMap += (v -> ApiResponse(successResponseCode, v.toString)))

  //const response cache
  private val serviceErrorResponseMap = scala.collection.mutable.HashMap[ErrorCode, ApiResponse]()
  ErrorCode.values.foreach(v => serviceErrorResponseMap += (v -> (ApiResponse(v.getCode, v.getMessage))))

  def successConstResponse(response: SuccessResponse.SuccessResponse): ApiResponse = successConstResponseMap(response)

  def serviceErrorResponse(errorCode: ErrorCode): ApiResponse = serviceErrorResponseMap(errorCode)

  def commonErrorResponse(): ApiResponse = serviceErrorResponse(EC_SYSTEM_ERROR)

  def commonInvalidRequestResponse(): ApiResponse = serviceErrorResponse(EC_INVALID_REQUEST)

  //  //error code cache
  //  private val successConstResponseMap = scala.collection.mutable.HashMap[SuccessResponse.SuccessResponse, StandardRoute]()
  //  SuccessResponse.values.foreach(v => successConstResponseMap += (v -> complete(JsonHelper.writeValueAsString(ApiResponse(successResponseCode, v.toString)))))
  //
  //  //const response cache
  //  private val serviceErrorResponseMap = scala.collection.mutable.HashMap[ErrorCode.ErrorCode, StandardRoute]()
  //  ErrorCode.values.foreach(v => serviceErrorResponseMap += (v -> complete(JsonHelper.writeValueAsString(ApiResponse(v.id.toString, v.toString)))))
  //
  //  def successConstResponse(response: SuccessResponse.SuccessResponse): StandardRoute = successConstResponseMap(response)
  //
  //  def serviceErrorResponse(errorCode: ErrorCode.ErrorCode): StandardRoute = serviceErrorResponseMap(errorCode)
  //
  //  def commonErrorResponse(): StandardRoute = serviceErrorResponse(EC_SYSTEM_ERROR)
  //
  //  def commonInvalidRequestResponse(): StandardRoute = serviceErrorResponse(EC_INVALID_REQUEST)
  //
  //  def response(apiResponse: ApiResponse): StandardRoute = complete(JsonHelper.writeValueAsString(apiResponse))

}
