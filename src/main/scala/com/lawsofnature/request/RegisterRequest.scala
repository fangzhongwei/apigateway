package com.lawsofnature.request

import com.lawsofnature.helper.RegHelper

/**
  * Created by fangzhongwei on 2016/10/10.
  */
case class RegisterRequest(dt: Int, fp: String, un: String, pid: Int, m: String, e: String, pwd: String) {
  require(1 == dt || 2 == dt, "invalid device type")
  require(fp.length() <= 256, "finger print too long")
  require(un.length() <= 64, "username too long")
  require(1 == pid || 2 == pid, "invalid pid type")
  require(m.length > 0 || e.length > 0, "identity empty")
  require(pwd.length > 0, "identity empty")
}