package com.jxjxgo.apigateway.service

import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

import com.jxjxgo.account.rpc.domain._
import com.jxjxgo.apigateway.domain.http.req.depositrequest.DepositReq
import com.jxjxgo.apigateway.domain.http.resp.SimpleApiResponse
import com.jxjxgo.apigateway.domain.http.resp.channellist.{Channel, ChannelListResp}
import com.jxjxgo.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp
import com.jxjxgo.apigateway.domain.http.resp.depositrequestresp.DepositRequestResp
import com.jxjxgo.apigateway.domain.http.resp.pricelist.{Price, PriceListResp}
import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import com.twitter.util.{Await, Future}

/**
  * Created by fangzhongwei on 2017/1/17.
  */
trait AccountService {
  def queryDiamondBalance(traceId: String, memberId: Long, deviceType: Int): SimpleApiResponse

  def getPriceList(traceId: String, deviceType: Int): PriceListResp

  def getChannelList(traceId: String, deviceType: Int): ChannelListResp

  def depositRequest(traceId: String, memberId: Long, depositRequest: DepositReq): DepositRequestResp

  def queryDeposit(traceId: String, memberId: Long, paymentVoucherNo: String): DepositRecordResp
}

class AccountServiceImpl @Inject()(accountClientService: AccountEndpoint[Future]) extends AccountService {
  override def queryDiamondBalance(traceId: String, memberId: Long, deviceType: Int): SimpleApiResponse = {
    val account: DiamondAccountResponse = Await.result(accountClientService.getAccount(traceId, memberId, deviceType))
    account.code match {
      case "0" => SimpleApiResponse(code = "0", ext1 = account.amount.toString)
      case _ => throw ServiceException.make(ErrorCode.get(account.code))
    }
  }

  override def getPriceList(traceId: String, deviceType: Int): PriceListResp = {
    val response: PriceListResponse = Await.result(accountClientService.getPriceList(traceId, deviceType))
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

  override def getChannelList(traceId: String, deviceType: Int): ChannelListResp = {
    val response: ChannelListResponse = Await.result(accountClientService.getChannelList(traceId, deviceType))
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
    val request: DepositRequest = DepositRequest(memberId = memberId, priceCode = req.priceCode, channelCode = req.channelCode, price = req.price, diamondAmount = req.diamondAmount)
    val response: DepositRequestResponse = Await.result(accountClientService.deposit(traceId, request))
    response.code match {
      case "0" =>
        DepositRequestResp(code = "0", paymentVoucherNo = response.paymentVoucherNo, tradeStatus = response.tradeStatus, extUrl = response.extUrl)
      case _ => throw ServiceException.make(ErrorCode.get(response.code))
    }
  }

  override def queryDeposit(traceId: String, memberId: Long, paymentVoucherNo: String): DepositRecordResp = {
    val r: DepositResponse = Await.result(accountClientService.queryDepositOrder(traceId, paymentVoucherNo))
    r.code match {
      case "0" =>
        val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS")
        DepositRecordResp(code = "0", paymentVoucherNo = r.paymentVoucherNo, accountId = r.accountId, memberId = r.memberId, tradeType = r.tradeType, tradeStatus = r.tradeStatus,
          diamondAcount = r.diamondAcount, amount = r.amount, gmtCreate = format.format(new Date(r.gmtCreate)), gmtUpdate = format.format(new Date(r.gmtUpdate)))
      case _ => throw ServiceException.make(ErrorCode.get(r.code))
    }
  }
}
