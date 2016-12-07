package com.lawsofnature.apigateway.request

import com.lawsofnature.apigateway.validate.Validator._
import com.lawsofnature.common.exception.ErrorCode._

/**
  * Created by fangzhongwei on 2016/12/7.
  */
class CheckUsernameRequest (@Validate(required = true, maxLength = 64, error = EC_INVALID_REQUEST)
                            var un: String)
