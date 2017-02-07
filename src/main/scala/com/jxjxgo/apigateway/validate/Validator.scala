package com.jxjxgo.apigateway.validate

import java.lang.reflect.Field

import com.jxjxgo.apigateway.annotations.FieldValidate
import com.jxjxgo.common.exception.ErrorCode
import com.jxjxgo.common.helper.RegHelper
import org.apache.commons.lang.StringUtils

import scala.annotation.meta.field
import scala.util.control.Breaks

/**
  * Created by fangzhongwei on 2016/11/29.
  */
object Validator {
  type Validate = FieldValidate@field

  private val STRING: String = "string"
  private val INT: String = "int"
  private val LONG: String = "long"

  def validate(request: AnyRef): Option[ErrorCode] = {
    var errorCode: Option[ErrorCode] = None

    val declaredFields: Array[Field] = request.getClass.getDeclaredFields

    var i = 0
    var field: Field = null

    //for String
    var strValue: String = null
    var mask: String = null
    var minLength: Int = -1
    var maxLength: Int = -1

    //for Int, Long
    var intValue = 0
    var longValue = 0L
    var min = -1
    var max = -1

    val loop = new Breaks;

    def doBreak(fieldValidate: FieldValidate): Nothing = {
      errorCode = Some(fieldValidate.error())
      loop.break()
    }

    loop.breakable {
      while (i < declaredFields.length) {
        field = declaredFields(i)
        i = i + 1
        field.setAccessible(true)
        val fieldValidate: FieldValidate = field.getAnnotation[FieldValidate](classOf[FieldValidate])
        if (fieldValidate != null) {
          fieldType(field.getType) match {
            case 1 =>
              strValue = field.get(request).asInstanceOf[String]
              mask = fieldValidate.mask()
              mask match {
                case "" =>
                  // required
                  if (fieldValidate.required() && StringUtils.isBlank(strValue)) doBreak(fieldValidate)
                  //minLength
                  minLength = fieldValidate.minLength()
                  if (minLength != -1 && minLength > strValue.length) doBreak(fieldValidate)
                  //maxLength
                  maxLength = fieldValidate.maxLength()
                  if (maxLength != -1 && maxLength < strValue.length) doBreak(fieldValidate)
                case _ =>
                  if (fieldValidate.required && StringUtils.isBlank(strValue)) throw doBreak(fieldValidate)
                  if (StringUtils.isNotBlank(strValue) && !RegHelper.isMatched(strValue, mask)) doBreak(fieldValidate)
              }
            case 2 =>
              intValue = field.get(request).asInstanceOf[Int]
              min = fieldValidate.min()
              if (min > intValue) doBreak(fieldValidate)
              max = fieldValidate.max()
              if (max < intValue) doBreak(fieldValidate)
            case 3 =>
              longValue = field.get(request).asInstanceOf[Long]
              min = fieldValidate.min()
              if (min > longValue) doBreak(fieldValidate)
              max = fieldValidate.max()
              if (max < longValue) doBreak(fieldValidate)
            case _ =>
              val v: AnyRef = field.get(request)
              if (fieldValidate.required() && v == null) doBreak(fieldValidate)
          }
        }
      }
    }
    errorCode
  }

  private def fieldType(clazz: Class[_]): Int = {
    val name: String = clazz.getName.toLowerCase
    if (name.contains(STRING)) 1
    else if (name.contains(INT)) 2
    else if (name.contains(LONG)) 3
    else 99
  }
}
