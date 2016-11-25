package com.lawsofnature.request

import com.lawsofnature.common.exception.ServiceErrorCode._
import com.lawsofnature.helper.RegHelper
import org.apache.commons.lang.StringUtils

/**
  * ti trace id
  * i identity
  * pid pid
  * Created by fangzhongwei on 2016/11/22.
  */
case class CheckIdentityRequest(i: String, pid: Int) {
  def validate(): Option[ServiceErrorCode] = {
    var error: Option[ServiceErrorCode] = None
    if (StringUtils.isBlank(i)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (1 != pid && 2 != pid) {
      error = Some(EC_INVALID_REQUEST)
    } else if (1 == pid && !RegHelper.isMobile(i)) {
      error = Some(EC_UC_INVALID_MOBILE)
    } else if (2 == pid && !RegHelper.isEmail(i)) {
      error = Some(EC_UC_INVALID_EMAIL)
    } else if (2 == pid && i.length > 64) {
      error = Some(EC_UC_EMAIL_LENGTH_LIMIT)
    }
    error
  }
}