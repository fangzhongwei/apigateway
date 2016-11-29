package com.lawsofnature.apigateway.response

/**
  * Created by fangzhongwei on 2016/10/10.
  */
case class ApiResponse(code: Int, msg: String, data: AnyRef = None)
