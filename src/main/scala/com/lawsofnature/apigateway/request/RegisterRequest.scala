package com.lawsofnature.apigateway.request

import com.lawsofnature.common.exception.ErrorCode
import com.lawsofnature.common.exception.ErrorCode._
import com.lawsofnature.common.helper.RegHelper
import org.apache.commons.lang.StringUtils

/**
  * ti traceId
  * lat lat
  * lng lng
  * dt deviceType
  * di deviceIdentity
  * un username
  * pid platform type
  * i identity
  * pwd password
  * Created by fangzhongwei on 2016/10/10.
  */
case class RegisterRequest(lat: String, lng: String, ctry: String, pro: String, c: String, cty: String, addr: String, dt: Int, di: String, un: String, pid: Int, i: String, pwd: String) {


  //  var lat: String = _
  //  var lng: String = _
  //  var ctry: String = _
  //  var pro: String = _
  //  var c: String = _
  //  var cty: String = _
  //  var addr: String = _
  //  var dt: Int = _
  //  var di: String = _
  //  var un: String = _
  //  var pid: Int = _
  //  var i: String = _
  //  var pwd: String = _

  def validate(): Option[ErrorCode] = {
    var error: Option[ErrorCode] = None
    if (StringUtils.isBlank(lat)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (StringUtils.isBlank(lng)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (1 != dt && 2 != dt) {
      error = Some(EC_INVALID_REQUEST)
    } else if (StringUtils.isBlank(di) || di.length > 256) {
      error = Some(EC_INVALID_REQUEST)
    } else if (StringUtils.isBlank(un)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (un.length > 64) {
      error = Some(EC_UC_USERNAME_LENGTH_LIMIT)
    } else if (1 != pid && 2 != pid) {
      error = Some(EC_INVALID_REQUEST)
    } else if (1 == pid && !RegHelper.isMobile(i)) {
      error = Some(EC_UC_INVALID_MOBILE)
    } else if (2 == pid && !RegHelper.isEmail(i)) {
      error = Some(EC_UC_INVALID_EMAIL)
    } else if (2 == pid && i.length > 64) {
      error = Some(EC_UC_EMAIL_LENGTH_LIMIT)
    } else if (StringUtils.isBlank(pwd)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (pwd.length < 7 || pwd.length > 16) {
      error = Some(EC_UC_PASSWORD_LENGTH_LIMIT)
    }
    error
  }
}