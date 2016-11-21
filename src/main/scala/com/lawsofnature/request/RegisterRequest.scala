package com.lawsofnature.request

import com.lawsofnature.common.exception.ServiceErrorCode._
import com.lawsofnature.helper.RegHelper
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
case class RegisterRequest(ti: String, lat: String, lng: String, dt: Int, di: String, un: String, pid: Int, i: String, pwd: String) {
  def validate(): Option[ServiceErrorCode] = {
    var error: Option[ServiceErrorCode] = None
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