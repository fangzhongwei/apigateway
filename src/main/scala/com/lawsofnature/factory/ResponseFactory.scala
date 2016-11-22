package com.lawsofnature.factory

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.exception.ServiceErrorCode
import com.lawsofnature.common.exception.ServiceErrorCode._
import com.lawsofnature.enumeration.SuccessResponse
import com.lawsofnature.helper.{Constant, JsonHelper}
import com.lawsofnature.response.ApiResponse

/**
  * cache response, avoid create every time
  * Created by fangzhongwei on 2016/11/3.
  */
object ResponseFactory {
  private val successResponseCode = 0

  //error code cache
  private val successConstResponseMap = scala.collection.mutable.HashMap[SuccessResponse.SuccessResponse, StandardRoute]()
  SuccessResponse.values.foreach(v => successConstResponseMap += (v -> complete(DESUtils.encrypt(JsonHelper.writeValueAsString(ApiResponse(successResponseCode, v.toString)), Constant.defaultDesKey))))

  //const response cache
  private val serviceErrorResponseMap = scala.collection.mutable.HashMap[ServiceErrorCode.ServiceErrorCode, StandardRoute]()
  ServiceErrorCode.values.foreach(v => serviceErrorResponseMap += (v -> complete(DESUtils.encrypt(JsonHelper.writeValueAsString(ApiResponse(v.id, v.toString)), Constant.defaultDesKey))))

  def successConstResponse(response: SuccessResponse.SuccessResponse): StandardRoute = successConstResponseMap(response)

  def serviceErrorResponse(errorCode: ServiceErrorCode.ServiceErrorCode): StandardRoute = serviceErrorResponseMap(errorCode)

  def commonErrorResponse(): StandardRoute = serviceErrorResponse(EC_SYSTEM_ERROR)

  def commonInvalidRequestResponse(): StandardRoute = serviceErrorResponse(EC_INVALID_REQUEST)

  def response(apiResponse: ApiResponse): StandardRoute = complete(DESUtils.encrypt(JsonHelper.writeValueAsString(apiResponse), Constant.defaultDesKey))


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
