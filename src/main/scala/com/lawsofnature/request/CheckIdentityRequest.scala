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
case class CheckIdentityRequest(ti: String, i: String, pid: Int) {
  def validate(): Option[ServiceErrorCode] = {
    var error: Option[ServiceErrorCode] = None
    if (StringUtils.isBlank(ti)) {
      error = Some(EC_INVALID_REQUEST)
    } else if (StringUtils.isBlank(i)) {
      error = Some(EC_INVALID_REQUEST)
    }
    error
  }
}