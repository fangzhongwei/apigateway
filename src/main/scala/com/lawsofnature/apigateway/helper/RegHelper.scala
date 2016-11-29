package com.lawsofnature.apigateway.helper

/**
  * Created by fangzhongwei on 2016/10/10.
  */
object RegHelper {
  val MobileRegex = """^[1]([3][0-9]{1}|([4][7]{1})|([5][0-3|5-9]{1})|([8][0-9]{1}))[0-9]{8}$""".r
  val EmailRegex = """^(\w)+(\.\w+)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,4}$""".r

  def isMobile(str: String): Boolean = {
    MobileRegex.pattern.matcher(str).matches
  }

  def isEmail(str: String): Boolean = {
    EmailRegex.pattern.matcher(str).matches
  }
}
