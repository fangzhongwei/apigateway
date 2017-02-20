package com.jxjxgo.apigateway.action

import javax.inject.Inject

import com.jxjxgo.apigateway.annotations.{ApiMapping, Param}
import com.jxjxgo.apigateway.domain.http.req.updatenickname.UpdateNickNameReq
import com.jxjxgo.apigateway.domain.http.resp.SimpleApiResponse
import com.jxjxgo.apigateway.enumerate.ParamSource
import com.jxjxgo.apigateway.service.MemberService

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
    SimpleApiResponse(code = memberService.updateNickName(traceId, getMemberId, updateNickNameReq.nickName.trim).code)
  }
}
