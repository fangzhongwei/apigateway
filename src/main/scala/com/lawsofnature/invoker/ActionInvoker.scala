package com.lawsofnature.invoker

import java.lang.reflect.Method
import java.util

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.lawsofnature.annotations.ApiMapping
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.exception.{ServiceErrorCode, ServiceException}
import com.lawsofnature.helper.JsonHelper
import com.lawsofnature.response.ApiResponse
import com.lawsofnature.server.HttpService

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Created by fangzhongwei on 2016/11/28.
  */
object ActionInvoker {

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
  implicit val timeout = (90 seconds)

  def invoke(actionId: Int, ip: String, traceId: String, body: String, salt: String): Future[String] = {
    val promise: Promise[String] with Object = Promise[String]()
    Future {

      val maybeTuple: Option[(Method, Class[_], ApiMapping)] = apiMap.get(actionId)
      maybeTuple match {
        case Some(tuple) =>
          val method: Method = tuple._1
          val parseClass: Class[_] = tuple._2
          val apiMapping: ApiMapping = tuple._3
          val response: Future[ApiResponse] = method.invoke(HttpService.injector.getInstance(method.getDeclaringClass), traceId, ip, JsonHelper.read(DESUtils.decrypt(body, salt), parseClass)).asInstanceOf[Future[ApiResponse]]

          val result: ApiResponse = Await.result(response, timeout)

          promise.success(DESUtils.encrypt(JsonHelper.writeValueAsString(result), salt))
        case None => promise.failure(new ServiceException(ServiceErrorCode.EC_SYSTEM_ERROR))
      }
    }
    promise.future
  }
}
