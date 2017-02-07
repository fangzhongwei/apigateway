package com.jxjxgo.apigateway.action

import javax.inject.Inject

import com.jxjxgo.apigateway.annotations.{ApiMapping, Param}
import com.jxjxgo.apigateway.domain.http.req.depositrequest.DepositReq
import com.jxjxgo.apigateway.domain.http.req.simple.SimpleReq
import com.jxjxgo.apigateway.domain.http.resp.SimpleApiResponse
import com.jxjxgo.apigateway.domain.http.resp.channellist.ChannelListResp
import com.jxjxgo.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp
import com.jxjxgo.apigateway.domain.http.resp.depositrequestresp.DepositRequestResp
import com.jxjxgo.apigateway.domain.http.resp.pricelist.PriceListResp
import com.jxjxgo.apigateway.enumerate.ParamSource
import com.jxjxgo.apigateway.service.AccountService
import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import org.apache.commons.lang.StringUtils

/**
  * Created by fangzhongwei on 2017/1/17.
  */
trait AccountAction {
  def queryDiamondAmount(traceId: String): SimpleApiResponse

  def getPriceList(traceId: String): PriceListResp

  def getChannelList(traceId: String): ChannelListResp

  def depositRequest(traceId: String, depositRequest: DepositReq): DepositRequestResp

  def queryDeposit(traceId: String, req: SimpleReq): DepositRecordResp
}

class AccountActionImpl @Inject()(accountService: AccountService) extends AccountAction with BaseAction {

  @ApiMapping(id = 2001)
  override def queryDiamondAmount(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                                  traceId: String): SimpleApiResponse = {
    try {
      accountService.queryDiamondBalance(traceId, getMemberId)
    } catch {
      case ex: ServiceException =>
        SimpleApiResponse(code = ex.getErrorCode.getCode)
      case ex: Exception =>
        SimpleApiResponse(code = ErrorCode.EC_SYSTEM_ERROR.getCode)
    }
  }

  @ApiMapping(id = 2002)
  override def getPriceList(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                            traceId: String): PriceListResp = {
    try {
      accountService.getPriceList(traceId)
    } catch {
      case ex: ServiceException =>
        PriceListResp(code = ex.getErrorCode.getCode)
      case ex: Exception =>
        PriceListResp(code = ErrorCode.EC_SYSTEM_ERROR.getCode)
    }
  }

  @ApiMapping(id = 2003)
  override def getChannelList(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                              traceId: String): ChannelListResp = {
    try {
      accountService.getChannelList(traceId)
    } catch {
      case ex: ServiceException =>
        ChannelListResp(code = ex.getErrorCode.getCode)
      case ex: Exception =>
        ChannelListResp(code = ErrorCode.EC_SYSTEM_ERROR.getCode)
    }
  }

  @ApiMapping(id = 2004)
  override def depositRequest(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                              traceId: String,
                              @Param(required = true, source = ParamSource.BODY)
                              depositRequest: DepositReq): DepositRequestResp = {
    try {
      accountService.depositRequest(traceId, getMemberId, depositRequest)
    } catch {
      case ex: ServiceException =>
        DepositRequestResp(code = ex.getErrorCode.getCode)
      case ex: Exception =>
        DepositRequestResp(code = ErrorCode.EC_SYSTEM_ERROR.getCode)
    }
  }

  @ApiMapping(id = 2005)
  override def queryDeposit(@Param(required = true, source = ParamSource.HEADER, name = "TI")
                            traceId: String,
                            @Param(required = true, source = ParamSource.BODY)
                            req: SimpleReq): DepositRecordResp = {
    try {
      val paymentVoucherNo: String = req.param0
      if (StringUtils.isBlank(paymentVoucherNo)) {
        throw ServiceException.make(ErrorCode.EC_INVALID_REQUEST)
      }
      accountService.queryDeposit(traceId, getMemberId, paymentVoucherNo)
    } catch {
      case ex: ServiceException =>
        DepositRecordResp(code = ex.getErrorCode.getCode)
      case ex: Exception =>
        DepositRecordResp(code = ErrorCode.EC_SYSTEM_ERROR.getCode)
    }
  }
}
