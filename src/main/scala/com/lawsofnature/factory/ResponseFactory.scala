package com.lawsofnature.factory

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute
import com.lawsofnature.common.exception.ServiceErrorCode
import com.lawsofnature.common.exception.ServiceErrorCode._
import com.lawsofnature.enumeration.SuccessResponse
import com.lawsofnature.helper.JsonHelper
import com.lawsofnature.response.ApiResponse

/**
  * cache response, avoid create every time
  * Created by fangzhongwei on 2016/11/3.
  */
object ResponseFactory {
  private val successResponseCode = "00"

  //error code cache
  private val successConstResponseMap = scala.collection.mutable.HashMap[String, StandardRoute]()
  SuccessResponse.values.foreach(v => successConstResponseMap += (v.id.toString -> complete(JsonHelper.writeValueAsString(ApiResponse(successResponseCode, v.toString)))))

  //const response cache
  private  val serviceErrorResponseMap = scala.collection.mutable.HashMap[String, StandardRoute]()
  ServiceErrorCode.values.foreach(v => serviceErrorResponseMap += (v.id.toString -> complete(JsonHelper.writeValueAsString(ApiResponse(v.id.toString, v.toString)))))

  def successConstResponse(response: SuccessResponse.SuccessResponse): StandardRoute = successConstResponseMap(response.id.toString)

  def serviceErrorResponse(errorCode: ServiceErrorCode.ServiceErrorCode): StandardRoute = serviceErrorResponseMap(errorCode.id.toString)

  def commonErrorResponse(): StandardRoute = serviceErrorResponse(EC_SYSTEM_ERROR)

  def response(apiResponse: ApiResponse):StandardRoute = complete(JsonHelper.writeValueAsString(apiResponse))
}
