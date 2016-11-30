package com.lawsofnature.apigateway.invoker

import java.lang.reflect.Method
import java.util

import com.lawsofnature.apigateway.annotations.ApiMapping
import com.lawsofnature.apigateway.response.ApiResponse
import com.lawsofnature.apigateway.server.HttpService
import com.lawsofnature.apigateway.validate.Validator
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.exception.{ErrorCode, ServiceException}
import com.lawsofnature.common.helper.JsonHelper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success}

/**
  * Created by fangzhongwei on 2016/11/28.
  */
object ActionInvoker {
  implicit val timeout = (90 seconds)

  private val classList: util.ArrayList[Class[_]] = HttpService.actionBeanClassList

  private val apiMap: scala.collection.mutable.Map[Int, (Method, Class[_], ApiMapping)] = scala.collection.mutable.Map()

  private val iterator: util.Iterator[Class[_]] = classList.iterator()
  var clazz: Class[_] = null
  while (iterator.hasNext) {
    clazz = iterator.next()
    val declaredMethods: Array[Method] = clazz.getDeclaredMethods
    for (i <- 0 to declaredMethods.length - 1) {
      val method: Method = declaredMethods(i)
      val apiMapping: ApiMapping = method.getAnnotation[ApiMapping](classOf[ApiMapping])
      if (apiMapping != null) {
        val types: Array[Class[_]] = method.getParameterTypes
        apiMap += (apiMapping.id() -> (method, types(types.length - 1), apiMapping))
      }
    }
  }

  def invoke(actionId: Int, ip: String, traceId: String, body: String, salt: String): Future[String] = {
    val promise: Promise[String] with Object = Promise[String]()
    Future {
      val maybeTuple: Option[(Method, Class[_], ApiMapping)] = apiMap.get(actionId)
      maybeTuple match {
        case Some(tuple) =>
          val method: Method = tuple._1
          val parseClass: Class[_] = tuple._2
          val apiMapping: ApiMapping = tuple._3
          val request: AnyRef = JsonHelper.read(DESUtils.decrypt(body, salt), parseClass)

          Validator.validate(request) match {
            case Some(errorCode) => promise.success(DESUtils.encrypt(JsonHelper.writeValueAsString(new ApiResponse(errorCode.getCode, errorCode.getMessage)), salt))
            case None =>
              val response: Future[ApiResponse] = method.invoke(HttpService.injector.getInstance(method.getDeclaringClass), traceId, ip, request).asInstanceOf[Future[ApiResponse]]
              response onComplete {
                case Success(response) =>
                  promise.success(DESUtils.encrypt(JsonHelper.writeValueAsString(response), salt))
                case Failure(ex) =>
                  ex match {
                    case e: ServiceException =>
                      promise.success(DESUtils.encrypt(JsonHelper.writeValueAsString(new ApiResponse(e.getErrorCode.getCode, e.getErrorCode.getMessage)), salt))
                    case _ => promise.success(DESUtils.encrypt(JsonHelper.writeValueAsString(new ApiResponse(ErrorCode.EC_SYSTEM_ERROR.getCode, ErrorCode.EC_SYSTEM_ERROR.getMessage)), salt))
                  }
              }
          }
        case None => promise.failure(ServiceException.make(ErrorCode.EC_SYSTEM_ERROR))
      }
    }
    promise.future
  }
}
