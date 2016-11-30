package com.lawsofnature.apigateway.request

import com.lawsofnature.apigateway.validate.Validator.Validate
import com.lawsofnature.common.exception.ErrorCode._

/**
  * ti trace id
  * i identity
  * pid pid
  * Created by fangzhongwei on 2016/11/22.
  */


case class CheckIdentityRequest(@Validate(required = true, maxLength = 128, error = EC_INVALID_REQUEST)
                                var i: String,
                                @Validate(required = true, min = 0, max = 2, error = EC_INVALID_REQUEST)
                                var pid: Int) {
}