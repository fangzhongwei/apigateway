package com.lawsofnature.action

import javax.inject.Inject

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.helper.{Constant, JsonHelper}
import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.service.MemberService
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/11/7.
  */
trait RegisterAction {
  def register(body: String): StandardRoute
}

class RegisterActionImpl @Inject()(memberService: MemberService) extends RegisterAction {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  def register(body: String): Route = {
    val registerRequest: RegisterRequest = JsonHelper.read[RegisterRequest](DESUtils.decrypt(body, Constant.defaultDesKey), classOf[RegisterRequest])
    registerRequest.validate() match {
      case Some(error) => ResponseFactory.serviceErrorResponse(error)
      case None => onSuccess(memberService.register(registerRequest)) {
        case Some(apiResponse) =>
          ResponseFactory.response(apiResponse)
        case None =>
          ResponseFactory.commonErrorResponse()
      }
    }
  }
}
