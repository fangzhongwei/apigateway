package com.lawsofnature.apigateway.request

import com.lawsofnature.apigateway.validate.Validator._
import com.lawsofnature.common.exception.ErrorCode._

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
case class RegisterRequest(@Validate(required = true, maxLength = 10, error = EC_INVALID_REQUEST)
                           lat: String,
                           @Validate(required = true, maxLength = 10, error = EC_INVALID_REQUEST)
                           lng: String,
                           ctry: String,
                           pro: String,
                           c: String,
                           cty: String,
                           addr: String,
                           @Validate(required = true, min = 1, max = 4, error = EC_INVALID_REQUEST)
                           dt: Int,
                           @Validate(required = true, maxLength = 128, error = EC_INVALID_REQUEST)
                           di: String,
                           @Validate(required = true, maxLength = 64, error = EC_UC_USERNAME_LENGTH_LIMIT)
                           un: String,
                           @Validate(required = true, min = 0, max = 2, error = EC_INVALID_REQUEST)
                           pid: Int,
                           @Validate(required = true, maxLength = 64, error = EC_UC_USERNAME_LENGTH_LIMIT)
                           i: String,
                           @Validate(required = true, minLength = 7, maxLength = 16, error = EC_UC_USERNAME_LENGTH_LIMIT)
                           pwd: String)