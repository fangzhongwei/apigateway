package com.lawsofnature.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.lawsofnature.request.RegisterRequest
import com.lawsofnature.response.ApiResponse
import spray.json.{DefaultJsonProtocol, JsFalse, JsNumber, JsString, JsTrue, JsValue, JsonFormat}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

trait JsonHelper extends SprayJsonSupport with DefaultJsonProtocol {

//  implicit object AnyJsonFormat extends JsonFormat[Any] {
//    def write(x: Any) = x match {
//      case n: Int => JsNumber(n)
//      case s: String => JsString(s)
//      case b: Boolean if b == true => JsTrue
//      case b: Boolean if b == false => JsFalse
//    }
//    def read(value: JsValue) = value match {
//      case JsNumber(n) => n.intValue()
//      case JsString(s) => s
//      case JsTrue => true
//      case JsFalse => false
//    }
//  }

  implicit val registerRequestFormat = jsonFormat8(RegisterRequest.apply)
  implicit val apiResponseFormat = jsonFormat3(ApiResponse.apply)

}
