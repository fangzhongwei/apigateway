package com.lawsofnature.action

import javax.inject.Inject

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.exception.ServiceErrorCode
import com.lawsofnature.enumeration.SuccessResponse
import com.lawsofnature.factory.ResponseFactory
import com.lawsofnature.helper.{Constant, JsonHelper}
import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.service.MemberService
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/11/7.
  */
trait RegisterAction {
  def register(ip:String, body: String): StandardRoute
}

class RegisterActionImpl @Inject()(memberService: MemberService) extends RegisterAction {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  def register(ip:String, body: String): Route = {
    val registerRequest: RegisterRequest = JsonHelper.read[RegisterRequest](DESUtils.decrypt(body, Constant.defaultDesKey), classOf[RegisterRequest])
    registerRequest.validate() match {
      case Some(error) => ResponseFactory.serviceErrorResponse(error)
      case None => onSuccess(memberService.register(ip, registerRequest)) {
        case Some(response) =>
          response.success match {
            case true =>
              ResponseFactory.successConstResponse(SuccessResponse.SUCCESS_REGISTER)
            case false =>
              ResponseFactory.serviceErrorResponse(ServiceErrorCode.get(response.code))
          }
        case None =>
          ResponseFactory.commonErrorResponse()
      }
    }
  }
}
