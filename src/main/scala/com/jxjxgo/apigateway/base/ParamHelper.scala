package com.jxjxgo.apigateway.base

import akka.http.scaladsl.model.HttpHeader
import com.jxjxgo.apigateway.base.ApiConfigContext.ActionMethodParamAttribute
import com.jxjxgo.apigateway.domain.http.req.depositrequest.DepositReq
import com.jxjxgo.apigateway.domain.http.req.login.LoginReq
import com.jxjxgo.apigateway.domain.http.req.loginbytoken.LoginByTokenReq
import com.jxjxgo.apigateway.domain.http.req.pullresource.PullResourceReq
import com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq
import com.jxjxgo.apigateway.domain.http.req.simple.SimpleReq
import com.jxjxgo.apigateway.domain.http.req.updatenickname.UpdateNickNameReq
import com.jxjxgo.apigateway.enumerate.ParamSource
import com.jxjxgo.apigateway.validate.Validator
import com.jxjxgo.common.edecrypt.DESUtils
import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import com.jxjxgo.common.helper.{GZipHelper, RegHelper}
import org.apache.commons.lang.StringUtils
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.immutable.Seq
import scala.collection.mutable

/**
  * Created by fangzhongwei on 2017/2/18.
  */
object ParamHelper {
  private[this] val logger: Logger = LoggerFactory.getLogger(getClass)

  def extractValueFromHeaders(name: String, headers: Seq[HttpHeader]): Option[String] = {
    headers.filter(h => h.name().equals(name)).headOption match {
      case Some(header) => Some(header.value())
      case None => None
    }
  }

  private[this] val EMPTY = ""

  def obtainParamValues(actionMethodParamAttributeSeq: mutable.Seq[ActionMethodParamAttribute], headers: Seq[HttpHeader], parameterMap: Map[String, String], bodyArray: Array[Byte], salt: String, parseClass: Option[Class[_]]): Array[AnyRef] = {
    var strValue: String = null
    var intValue: Int = 0
    var longValue: Long = 0L
    var minLength = -1
    var maxLength = -1
    var min = -1
    var max = -1

    var list: scala.collection.mutable.ListBuffer[AnyRef] = scala.collection.mutable.ListBuffer[AnyRef]()

    actionMethodParamAttributeSeq.foreach {
      attr =>
        val anyRef: AnyRef = {
          val name: String = attr.name
          fieldType(attr.classType) match {
            case 1 =>
              strValue = attr.source match {
                case ParamSource.HEADER => extractValueFromHeaders(name, headers) match {
                  case Some(value) => value
                  case None => EMPTY
                }
                case ParamSource.PARAM => parameterMap.get(name) match {
                  case Some(value) => value
                  case None => EMPTY
                }
                case _ => throw ServiceException.make(attr.errorCode)
              }
              attr.mask match {
                case "" =>
                  if (attr.required && StringUtils.isBlank(strValue)) throw ServiceException.make(attr.errorCode)
                  minLength = attr.minLength
                  if (minLength != -1 && minLength > strValue.length) throw ServiceException.make(attr.errorCode)
                  maxLength = attr.maxLength
                  if (maxLength != -1 && maxLength < strValue.length) throw ServiceException.make(attr.errorCode)
                case _ =>
                  if (attr.required && StringUtils.isBlank(strValue)) throw ServiceException.make(attr.errorCode)
                  if (StringUtils.isNotBlank(strValue) && !RegHelper.isMatched(strValue, attr.mask)) throw ServiceException.make(attr.errorCode)
              }
              strValue
            case 2 =>
              intValue = attr.source match {
                case ParamSource.HEADER =>
                  extractValueFromHeaders(name, headers) match {
                    case Some(value) => value.toInt
                    case None => 0
                  }
                case ParamSource.PARAM => parameterMap.get(name) match {
                  case Some(value) => value.toInt
                  case None => 0
                }
                case _ => throw ServiceException.make(attr.errorCode)
              }
              min = attr.min
              if (min > intValue) throw ServiceException.make(attr.errorCode)
              max = attr.max
              if (max < intValue) throw ServiceException.make(attr.errorCode)
              intValue.asInstanceOf[AnyRef]
            case 3 =>
              longValue = attr.source match {
                case ParamSource.HEADER =>
                  extractValueFromHeaders(name, headers) match {
                    case Some(value) => value.toLong
                    case None => 0L
                  }
                case ParamSource.PARAM => parameterMap.get(name) match {
                  case Some(value) => value.toLong
                  case None => 0L
                }
                case _ => throw ServiceException.make(attr.errorCode)
              }
              min = attr.min
              if (min > longValue) throw ServiceException.make(attr.errorCode)
              max = attr.max
              if (max < longValue) throw ServiceException.make(attr.errorCode)
              longValue.asInstanceOf[AnyRef]
            case _ =>
              val bodyIsNull: Boolean = bodyArray == null || bodyArray.length == 0
              if (attr.required && bodyIsNull) throw ServiceException.make(attr.errorCode)
              val request: AnyRef = if (!bodyIsNull) parseRequest(bodyArray, salt, parseClass) else null
              if (request != null) {
                Validator.validate(request) match {
                  case Some(errorCode) => throw ServiceException.make(errorCode)
                  case None =>
                }
              }
              request
          }
        }
        list += anyRef
    }
    list.toArray
  }

  private val STRING: String = "string"
  private val INT: String = "int"
  private val LONG: String = "long"

  private def fieldType(clazz: Class[_]): Int = {
    val name: String = clazz.getName.toLowerCase
    if (name.contains(STRING)) 1
    else if (name.contains(INT)) 2
    else if (name.contains(LONG)) 3
    else 99
  }

  def parseRequest(bodyArray: Array[Byte], salt: String, parseClass: Option[Class[_]]): AnyRef = {
    val protoArray: Array[Byte] = GZipHelper.uncompress(DESUtils.decrypt(bodyArray, salt))

    val clazz: Class[_] = parseClass match {
      case Some(c) => c
      case None => throw ServiceException.make(ErrorCode.EC_SYSTEM_ERROR)
    }

    logger.info("body class name:" + clazz.getName)

    clazz.getName match {
      case "com.jxjxgo.apigateway.domain.http.req.depositrequest.DepositReq" =>
        DepositReq.parseFrom(protoArray)
      case "com.jxjxgo.apigateway.domain.http.req.login.LoginReq" =>
        LoginReq.parseFrom(protoArray)
      case "com.jxjxgo.apigateway.domain.http.req.loginbytoken.LoginByTokenReq" =>
        LoginByTokenReq.parseFrom(protoArray)
      case "com.jxjxgo.apigateway.domain.http.req.pullresource.PullResourceReq" =>
        PullResourceReq.parseFrom(protoArray)
      case "com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq" =>
        SendLoginVerificationCodeReq.parseFrom(protoArray)
      case "com.jxjxgo.apigateway.domain.http.req.simple.SimpleReq" =>
        SimpleReq.parseFrom(protoArray)
      case "com.jxjxgo.apigateway.domain.http.req.updatenickname.UpdateNickNameReq" =>
        UpdateNickNameReq.parseFrom(protoArray)
      case _ =>
        logger.error(s"cass: ${clazz.getName} not config")
        throw ServiceException.make(ErrorCode.EC_SYSTEM_ERROR)
    }
  }
}
