package com.lawsofnature.request

/**
  * Created by fangzhongwei on 2016/10/10.
  */
case class RegisterRequest(dt: String, did: String, ip: String, un: String, pid: Int, m: Option[String], e: Option[String], pwd: String) {
  require(ip.length >= 8, "invalid ip address")
}

case class Robot(name: String, color: Option[String], amountOfArms: Int) {
  require(amountOfArms >= 0, "Robots cannot have a negative amount of arms!")
}