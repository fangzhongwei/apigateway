package com.lawsofnature.apigateway.action

import javax.inject.Inject

import RpcMember.MemberResponse
import com.lawsofnature.apigateway.annotations.Param
import com.lawsofnature.apigateway.enumerate.ParamSource
import com.lawsofnature.apigateway.response.ApiResponse
import com.lawsofnature.apigateway.service.MemberService
import com.lawsofnature.common.exception.ErrorCode
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/12/3.
  */
trait MemberAction extends BaseAction {
  def getProfile(traceId: String): ApiResponse
}

class MemberActionImpl @Inject()(memberService: MemberService) extends MemberAction {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  override def getProfile(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                          traceId: String): ApiResponse = {
    val memberResponse: MemberResponse = memberService.getMemberByMemberId(traceId, getMemberId)
    memberResponse.success match {
      case true => ApiResponse.make(data = memberResponse)
      case false => ApiResponse.makeErrorResponse(ErrorCode.get(memberResponse.code))
    }
  }
}
