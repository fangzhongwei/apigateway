package com.lawsofnature.apigateway.action

import javax.inject.Inject

import RpcMember.BaseResponse
import com.lawsofnature.apigateway.annotations.{ApiMapping, Param}
import com.lawsofnature.apigateway.domain.http.req.updatenickname.UpdateNickNameReq
import com.lawsofnature.apigateway.domain.http.resp.SimpleApiResponse
import com.lawsofnature.apigateway.enumerate.ParamSource
import com.lawsofnature.apigateway.service.MemberService

/**
  * Created by fangzhongwei on 2017/1/12.
  */
trait MemberAction {
  def updateNickName(traceId: String, updateNickNameReq: UpdateNickNameReq): SimpleApiResponse
}

class MemberActionImpl @Inject()(memberService: MemberService) extends MemberAction with BaseAction {

  @ApiMapping(id = 1005)
  override def updateNickName(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                              traceId: String,
                              @Param(required = true, source = ParamSource.BODY)
                              updateNickNameReq: UpdateNickNameReq): SimpleApiResponse = {
    SimpleApiResponse(code = memberService.updateNickName(traceId, getMemberId, updateNickNameReq.nickName).code)
  }
}
