// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.lawsofnature.apigateway.domain.http.req

import com.lawsofnature.apigateway.validate.Validator.Validate
import com.lawsofnature.common.exception.ErrorCode._

@SerialVersionUID(0L)
final case class AutoLoginReq(
                               @Validate(required = true, min = 1, max = 4, error = EC_INVALID_REQUEST)
                               deviceType: Int = 0,
                               @Validate(required = true, maxLength = 128, error = EC_INVALID_REQUEST)
                               fingerPrint: String = "",
                               @Validate(required = true, maxLength = 32, error = EC_INVALID_REQUEST)
                               token: String = ""
                             ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[AutoLoginReq] with com.trueaccord.lenses.Updatable[AutoLoginReq] {
  @transient
  private[this] var __serializedSizeCachedValue: Int = 0

  private[this] def __computeSerializedValue(): Int = {
    var __size = 0
    if (deviceType != 0) {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, deviceType)
    }
    if (fingerPrint != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, fingerPrint)
    }
    if (token != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, token)
    }
    __size
  }

  final override def serializedSize: Int = {
    var read = __serializedSizeCachedValue
    if (read == 0) {
      read = __computeSerializedValue()
      __serializedSizeCachedValue = read
    }
    read
  }

  def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): Unit = {
    {
      val __v = deviceType
      if (__v != 0) {
        _output__.writeInt32(1, __v)
      }
    };
    {
      val __v = fingerPrint
      if (__v != "") {
        _output__.writeString(2, __v)
      }
    };
    {
      val __v = token
      if (__v != "") {
        _output__.writeString(3, __v)
      }
    };
  }

  def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.lawsofnature.apigateway.domain.http.req.AutoLoginReq = {
    var __deviceType = this.deviceType
    var __fingerPrint = this.fingerPrint
    var __token = this.token
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 8 =>
          __deviceType = _input__.readInt32()
        case 18 =>
          __fingerPrint = _input__.readString()
        case 26 =>
          __token = _input__.readString()
        case tag => _input__.skipField(tag)
      }
    }
    com.lawsofnature.apigateway.domain.http.req.AutoLoginReq(
      deviceType = __deviceType,
      fingerPrint = __fingerPrint,
      token = __token
    )
  }

  def withDeviceType(__v: Int): AutoLoginReq = copy(deviceType = __v)

  def withFingerPrint(__v: String): AutoLoginReq = copy(fingerPrint = __v)

  def withToken(__v: String): AutoLoginReq = copy(token = __v)

  def getField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): scala.Any = {
    __field.getNumber match {
      case 1 => {
        val __t = deviceType
        if (__t != 0) __t else null
      }
      case 2 => {
        val __t = fingerPrint
        if (__t != "") __t else null
      }
      case 3 => {
        val __t = token
        if (__t != "") __t else null
      }
    }
  }

  override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)

  def companion = com.lawsofnature.apigateway.domain.http.req.AutoLoginReq
}

object AutoLoginReq extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.lawsofnature.apigateway.domain.http.req.AutoLoginReq] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.lawsofnature.apigateway.domain.http.req.AutoLoginReq] = this

  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.lawsofnature.apigateway.domain.http.req.AutoLoginReq = {
    require(__fieldsMap.keys.forall(_.getContainingType() == descriptor), "FieldDescriptor does not match message type.")
    val __fields = descriptor.getFields
    com.lawsofnature.apigateway.domain.http.req.AutoLoginReq(
      __fieldsMap.getOrElse(__fields.get(0), 0).asInstanceOf[Int],
      __fieldsMap.getOrElse(__fields.get(1), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(2), "").asInstanceOf[String]
    )
  }

  def descriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ReqProto.descriptor.getMessageTypes.get(2)

  def messageCompanionForField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__field)

  def enumCompanionForField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__field)

  lazy val defaultInstance = com.lawsofnature.apigateway.domain.http.req.AutoLoginReq(
  )

  implicit class AutoLoginReqLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.lawsofnature.apigateway.domain.http.req.AutoLoginReq]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.lawsofnature.apigateway.domain.http.req.AutoLoginReq](_l) {
    def deviceType: _root_.com.trueaccord.lenses.Lens[UpperPB, Int] = field(_.deviceType)((c_, f_) => c_.copy(deviceType = f_))

    def fingerPrint: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.fingerPrint)((c_, f_) => c_.copy(fingerPrint = f_))

    def token: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.token)((c_, f_) => c_.copy(token = f_))
  }

  final val DEVICETYPE_FIELD_NUMBER = 1
  final val FINGERPRINT_FIELD_NUMBER = 2
  final val TOKEN_FIELD_NUMBER = 3
}
