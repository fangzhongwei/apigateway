package com.lawsofnature.apigateway.request

import com.lawsofnature.apigateway.annotations.FieldValidate
import com.lawsofnature.common.exception.ErrorCode._

import scala.annotation.meta.field

/**
  * ti trace id
  * i identity
  * pid pid
  * Created by fangzhongwei on 2016/11/22.
  */
case class CheckIdentityRequest(@(FieldValidate@field)(required = true, maxLength = 128, error = EC_INVALID_REQUEST)
                               var i: String,
                                @(FieldValidate@field)(required = true, min = 0, max = 2, error = EC_UC_INVALID_EMAIL)
                               var pid: Int) {
}