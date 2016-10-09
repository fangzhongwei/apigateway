package com.lawsofnature.response


/**
  * Created by fangzhongwei on 2016/10/10.
  */
case class ApiResponse(code: String, msg: String, data: Option[String] = None)
