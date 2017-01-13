package com.lawsofnature.apigateway.action

import javax.inject.Inject

import RpcI18N.{PullResourceRequest, ResourceResponse}
import com.lawsofnature.apigateway.annotations.{ApiMapping, Param}
import com.lawsofnature.apigateway.domain.http.req.pullresource.PullResourceReq
import com.lawsofnature.apigateway.domain.http.req.simple.SimpleReq
import com.lawsofnature.apigateway.domain.http.resp.resource.{Resource, ResourceResp}
import com.lawsofnature.apigateway.enumerate.ParamSource
import com.lawsofnature.apigateway.service.I18NService
import com.lawsofnature.common.exception.ErrorCode
import org.apache.commons.lang.StringUtils

/**
  * Created by fangzhongwei on 2017/1/13.
  */
trait I18NAction {
  def getLatest(traceId: String, req: SimpleReq): ResourceResp

  def pullLatest(traceId: String, req: PullResourceReq): ResourceResp
}

class I18NActionImpl @Inject()(i18NService: I18NService) extends I18NAction {
  implicit def convert2Proto(response: ResourceResponse): ResourceResp = {
    val resourceList: Array[RpcI18N.Resource] = response.resourceList
    (resourceList != null && !resourceList.isEmpty) match {
      case true =>
        ResourceResp(latestVersion = response.latestVersion)
      case false =>
        val seq: Seq[Resource] = resourceList.map {
          r => Resource(version = r.version, resourceType = r.`type`, code = r.code, lan = r.lan, desc = r.desc)
        }
        ResourceResp(code = "0", latestVersion = response.latestVersion, list = seq)
    }
  }

  implicit def convert2Rpc(req: PullResourceReq): PullResourceRequest = {
    new PullResourceRequest(req.version, req.lan)
  }

  @ApiMapping(id = 1006, ignoreSession = true)
  override def getLatest(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                         traceId: String,
                         @Param(required = true, source = ParamSource.BODY)
                         req: SimpleReq): ResourceResp = {
    val lan: String = req.param0
    StringUtils.isBlank(lan) match {
      case true => ResourceResp(code = ErrorCode.EC_INVALID_REQUEST.getCode)
      case false => val resourceResponse: ResourceResponse = i18NService.getLatest(traceId, lan)
        resourceResponse.code match {
          case "0" => resourceResponse
          case _ => ResourceResp(code = resourceResponse.code)
        }
    }
  }

  @ApiMapping(id = 1007, ignoreSession = true)
  override def pullLatest(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                          traceId: String,
                          @Param(required = true, source = ParamSource.BODY)
                          req: PullResourceReq): ResourceResp = {
    val resourceResponse: ResourceResponse = i18NService.pullLatest(traceId, req)
    resourceResponse.code match {
      case "0" => resourceResponse
      case _ => ResourceResp(code = resourceResponse.code)
    }
  }
}
