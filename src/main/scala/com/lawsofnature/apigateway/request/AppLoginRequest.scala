package com.lawsofnature.apigateway.request

import com.lawsofnature.apigateway.validate.Validator._
import com.lawsofnature.common.exception.ErrorCode._

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