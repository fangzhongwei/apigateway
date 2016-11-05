package com.lawsofnature.helper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
  * Created by fangzhongwei on 2016/11/3.
  */
object JsonHelper {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  def writeValueAsString(anyRef: AnyRef): String = {
    mapper.writeValueAsString(anyRef)
  }

  def read[T](json: String, c: Class[T]): T = {
    mapper.readValue(json, c)
  }
}
