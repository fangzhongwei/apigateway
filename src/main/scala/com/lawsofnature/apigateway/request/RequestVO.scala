package com.lawsofnature.apigateway.request

/**
  * Created by fangzhongwei on 2016/12/8.
  */

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
                                var pid: Int)

case class CheckUsernameRequest(@Validate(required = true, maxLength = 64, error = EC_INVALID_REQUEST)
                                var un: String)

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
case class AppLoginRequest(@Validate(required = true, min = 1, max = 10, error = EC_INVALID_REQUEST)
                           ci: Int,
                           @Validate(required = true, min = 1, max = 4, error = EC_INVALID_REQUEST)
                           dt: Int,
                           @Validate(required = true, maxLength = 128, error = EC_INVALID_REQUEST)
                           di: String,
                           @Validate(required = true, maxLength = 10, error = EC_INVALID_REQUEST)
                           lat: String,
                           @Validate(required = true, maxLength = 10, error = EC_INVALID_REQUEST)
                           lng: String,
                           ctry: String,
                           pro: String,
                           c: String,
                           cty: String,
                           addr: String,
                           @Validate(required = true, maxLength = 64, error = EC_UC_USERNAME_LENGTH_LIMIT)
                           i: String,
                           @Validate(required = true, maxLength = 256, error = EC_INVALID_REQUEST)
                           pwd: String)