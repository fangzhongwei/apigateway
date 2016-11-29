package com.lawsofnature.apigateway.request

import com.lawsofnature.common.exception.ErrorCode
import com.lawsofnature.common.exception.ErrorCode._
import org.apache.commons.lang.StringUtils

/**
  * ti - trace id
  * ci - client id
  * dt - device type
  * di - device id
  * lat
  * lng
  * ctry - country
  * pro - province
  * c - city
  * cty - county
  * addr - address
  * i - identity
  * p - password
  * Created by fangzhongwei on 2016/11/23.
  */
case class AppLoginRequest(ti: String, ci:Int, dt: Int, di: String, lat: String, lng: String, ctry: String, pro: String, c: String, cty: String, addr: String, i: String, pwd: String) {
  def validate(): Option[ErrorCode] = {
    var error: Option[ErrorCode] = None
    if (StringUtils.isBlank(ti)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (StringUtils.isBlank(lat)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (StringUtils.isBlank(lng)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (1 != dt && 2 != dt) {
      error = Some(EC_INVALID_REQUEST)
    } else if (StringUtils.isBlank(di) || di.length > 256) {
      error = Some(EC_INVALID_REQUEST)
    } else if (StringUtils.isBlank(i)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (i.length > 64) {
      error = Some(EC_UC_USERNAME_LENGTH_LIMIT)
    } else if (StringUtils.isBlank(pwd)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (pwd.length < 7 || pwd.length > 16) {
      error = Some(EC_UC_PASSWORD_LENGTH_LIMIT)
    }
    error
  }
}