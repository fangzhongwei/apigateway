package com.lawsofnature.apigateway.helper

import com.typesafe.config.ConfigFactory

/**
  * Created by fangzhongwei on 2016/11/5.
  */
object Constant {
  val defaultDesKey = ConfigFactory.load().getString("edecrypt.default.des.key")
}
