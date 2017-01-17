package com.lawsofnature.apigateway.service

import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

import RpcAccount._
import com.lawsofnature.account.client.AccountClientService
import com.lawsofnature.apigateway.domain.http.req.depositrequest.DepositReq
import com.lawsofnature.apigateway.domain.http.resp.SimpleApiResponse
import com.lawsofnature.apigateway.domain.http.resp.channellist.{Channel, ChannelListResp}
import com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp
import com.lawsofnature.apigateway.domain.http.resp.depositrequestresp.DepositRequestResp
import com.lawsofnature.apigateway.domain.http.resp.pricelist.{Price, PriceListResp}
import com.lawsofnature.common.exception.{ErrorCode, ServiceException}

/**
  * Created by fangzhongwei on 2017/1/17.
  */
trait AccountService {
  def queryDiamondBalance(traceId: String, memberId: Long): SimpleApiResponse

  def getPriceList(traceId: String): PriceListResp

  def getChannelList(traceId: String): ChannelListResp

  def depositRequest(traceId: String, memberId: Long, depositRequest: DepositReq): DepositRequestResp

  def queryDeposit(traceId: String, memberId: Long, paymentVoucherNo: String): DepositRecordResp
}

class AccountServiceImpl @Inject()(accountClientService: AccountClientService) extends AccountService {
  override def queryDiamondBalance(traceId: String, memberId: Long): SimpleApiResponse = {
    val account: DiamondAccountResponse = accountClientService.getAccount(traceId, memberId)
    account.code match {
      case "0" => SimpleApiResponse(code = "0", ext1 = account.amount.toString)
      case _ => throw ServiceException.make(ErrorCode.get(account.code))
    }
  }

  override def getPriceList(traceId: String): PriceListResp = {
    val response: PriceListResponse = accountClientService.getPriceList(traceId)
    response.code match {
      case "0" =>
        val seq: Seq[Price] = Seq[Price]()
        response.priceList.foreach {
          p => seq :+ Price(code = p.code, diamondAmount = p.diamondAmount, price = p.price)
        }
        PriceListResp(code = "0", list = seq)
      case _ => throw ServiceException.make(ErrorCode.get(response.code))
    }
  }

  override def getChannelList(traceId: String): ChannelListResp = {
    val response: ChannelListResponse = accountClientService.getChannelList(traceId)
    response.code match {
      case "0" =>
        val seq: Seq[Channel] = Seq[Channel]()
        response.channelList.foreach {
          c => seq :+ Channel(code = c.code, name = c.name)
        }
        ChannelListResp(code = "0", list = seq)
      case _ => throw ServiceException.make(ErrorCode.get(response.code))
    }
  }

  override def depositRequest(traceId: String, memberId: Long, req: DepositReq): DepositRequestResp = {
    val request: RpcAccount.DepositRequest = new RpcAccount.DepositRequest()
    request.memberId = memberId
    request.priceCode = req.priceCode
    request.channelCode = req.channelCode
    request.price = req.price
    request.diamondAmount = req.diamondAmount
    val response: DepositRequestResponse = accountClientService.deposit(traceId, request)
    response.code match {
      case "0" =>
        DepositRequestResp(code = "0", paymentVoucherNo = response.paymentVoucherNo, tradeStatus = response.tradeStatus, extUrl = response.extUrl)
      case _ => throw ServiceException.make(ErrorCode.get(response.code))
    }
  }

  override def queryDeposit(traceId: String, memberId: Long, paymentVoucherNo: String): DepositRecordResp = {
    val r: DepositResponse = accountClientService.queryDepositOrder(traceId, paymentVoucherNo)
    r.code match {
      case "0" =>
        val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS")
        DepositRecordResp(code = "0", paymentVoucherNo = r.paymentVoucherNo, accountId = r.accountId, memberId = r.memberId, tradeType = r.tradeType, tradeStatus = r.tradeStatus,
          diamondAcount = r.diamondAcount, amount = r.amount, gmtCreate = format.format(new Date(r.gmtCreate)), gmtUpdate = format.format(new Date(r.gmtUpdate)))
      case _ => throw ServiceException.make(ErrorCode.get(r.code))
    }
  }
}
