package com.lawsofnature.apigateway.factory

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute
import com.lawsofnature.apigateway.enumeration.SuccessResponse
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.exception.ServiceErrorCode
import com.lawsofnature.common.exception.ServiceErrorCode._
import com.lawsofnature.apigateway.helper.{Constant, JsonHelper}
import com.lawsofnature.apigateway.response.ApiResponse

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
  private val serviceErrorResponseMap = scala.collection.mutable.HashMap[ServiceErrorCode.ServiceErrorCode, ApiResponse]()
  ServiceErrorCode.values.foreach(v => serviceErrorResponseMap += (v -> (ApiResponse(v.id, v.toString))))

  def successConstResponse(response: SuccessResponse.SuccessResponse): ApiResponse = successConstResponseMap(response)

  def serviceErrorResponse(errorCode: ServiceErrorCode.ServiceErrorCode): ApiResponse = serviceErrorResponseMap(errorCode)

  def commonErrorResponse(): ApiResponse = serviceErrorResponse(EC_SYSTEM_ERROR)

  def commonInvalidRequestResponse(): ApiResponse = serviceErrorResponse(EC_INVALID_REQUEST)


//  //error code cache
//  private val successConstResponseMap = scala.collection.mutable.HashMap[SuccessResponse.SuccessResponse, StandardRoute]()
//  SuccessResponse.values.foreach(v => successConstResponseMap += (v -> complete(JsonHelper.writeValueAsString(ApiResponse(successResponseCode, v.toString)))))
//
//  //const response cache
//  private val serviceErrorResponseMap = scala.collection.mutable.HashMap[ServiceErrorCode.ServiceErrorCode, StandardRoute]()
//  ServiceErrorCode.values.foreach(v => serviceErrorResponseMap += (v -> complete(JsonHelper.writeValueAsString(ApiResponse(v.id.toString, v.toString)))))
//
//  def successConstResponse(response: SuccessResponse.SuccessResponse): StandardRoute = successConstResponseMap(response)
//
//  def serviceErrorResponse(errorCode: ServiceErrorCode.ServiceErrorCode): StandardRoute = serviceErrorResponseMap(errorCode)
//
//  def commonErrorResponse(): StandardRoute = serviceErrorResponse(EC_SYSTEM_ERROR)
//
//  def commonInvalidRequestResponse(): StandardRoute = serviceErrorResponse(EC_INVALID_REQUEST)
//
//  def response(apiResponse: ApiResponse): StandardRoute = complete(JsonHelper.writeValueAsString(apiResponse))

}
