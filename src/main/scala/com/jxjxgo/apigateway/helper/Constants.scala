package com.jxjxgo.apigateway.helper

import com.typesafe.config.ConfigFactory

/**
  * Created by fangzhongwei on 2016/11/5.
  */
object Constants {
  val defaultDesKey = ConfigFactory.load().getString("edecrypt.default.des.key")
  val HEADER_IP = "X-Real-IP"
}
