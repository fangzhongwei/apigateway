// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.jxjxgo.apigateway.domain.http.req.sendcode

import com.jxjxgo.apigateway.validate.Validator._
import com.jxjxgo.common.exception.ErrorCode._

@SerialVersionUID(0L)
final case class SendLoginVerificationCodeReq(
                                               @Validate(required = true, min = 1, max = 4, error = EC_INVALID_REQUEST)
                                               deviceType: Int = 0,
                                               @Validate(required = true, maxLength = 128, error = EC_INVALID_REQUEST)
                                               fingerPrint: String = "",
                                               @Validate(required = true, mask = "^[1]([3][0-9]{1}|([4][7]{1})|([5][0-3|5-9]{1})|([8][0-9]{1}))[0-9]{8}$", error = EC_UC_INVALID_MOBILE)
                                               mobile: String = "",
                                               resend: Boolean = false,
                                               lastChannel: Int = 0,
                                               ext1: String = "",
                                               ext2: String = "",
                                               ext3: String = "",
                                               ext4: String = "",
                                               ext5: String = ""
                                             ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[SendLoginVerificationCodeReq] with com.trueaccord.lenses.Updatable[SendLoginVerificationCodeReq] {
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
    if (mobile != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, mobile)
    }
    if (resend != false) {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(4, resend)
    }
    if (lastChannel != 0) {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(5, lastChannel)
    }
    if (ext1 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(6, ext1)
    }
    if (ext2 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(7, ext2)
    }
    if (ext3 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(8, ext3)
    }
    if (ext4 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(9, ext4)
    }
    if (ext5 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(10, ext5)
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
      val __v = mobile
      if (__v != "") {
        _output__.writeString(3, __v)
      }
    };
    {
      val __v = resend
      if (__v != false) {
        _output__.writeBool(4, __v)
      }
    };
    {
      val __v = lastChannel
      if (__v != 0) {
        _output__.writeInt32(5, __v)
      }
    };
    {
      val __v = ext1
      if (__v != "") {
        _output__.writeString(6, __v)
      }
    };
    {
      val __v = ext2
      if (__v != "") {
        _output__.writeString(7, __v)
      }
    };
    {
      val __v = ext3
      if (__v != "") {
        _output__.writeString(8, __v)
      }
    };
    {
      val __v = ext4
      if (__v != "") {
        _output__.writeString(9, __v)
      }
    };
    {
      val __v = ext5
      if (__v != "") {
        _output__.writeString(10, __v)
      }
    };
  }

  def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq = {
    var __deviceType = this.deviceType
    var __fingerPrint = this.fingerPrint
    var __mobile = this.mobile
    var __resend = this.resend
    var __lastChannel = this.lastChannel
    var __ext1 = this.ext1
    var __ext2 = this.ext2
    var __ext3 = this.ext3
    var __ext4 = this.ext4
    var __ext5 = this.ext5
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
          __mobile = _input__.readString()
        case 32 =>
          __resend = _input__.readBool()
        case 40 =>
          __lastChannel = _input__.readInt32()
        case 50 =>
          __ext1 = _input__.readString()
        case 58 =>
          __ext2 = _input__.readString()
        case 66 =>
          __ext3 = _input__.readString()
        case 74 =>
          __ext4 = _input__.readString()
        case 82 =>
          __ext5 = _input__.readString()
        case tag => _input__.skipField(tag)
      }
    }
    com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq(
      deviceType = __deviceType,
      fingerPrint = __fingerPrint,
      mobile = __mobile,
      resend = __resend,
      lastChannel = __lastChannel,
      ext1 = __ext1,
      ext2 = __ext2,
      ext3 = __ext3,
      ext4 = __ext4,
      ext5 = __ext5
    )
  }

  def withDeviceType(__v: Int): SendLoginVerificationCodeReq = copy(deviceType = __v)

  def withFingerPrint(__v: String): SendLoginVerificationCodeReq = copy(fingerPrint = __v)

  def withMobile(__v: String): SendLoginVerificationCodeReq = copy(mobile = __v)

  def withResend(__v: Boolean): SendLoginVerificationCodeReq = copy(resend = __v)

  def withLastChannel(__v: Int): SendLoginVerificationCodeReq = copy(lastChannel = __v)

  def withExt1(__v: String): SendLoginVerificationCodeReq = copy(ext1 = __v)

  def withExt2(__v: String): SendLoginVerificationCodeReq = copy(ext2 = __v)

  def withExt3(__v: String): SendLoginVerificationCodeReq = copy(ext3 = __v)

  def withExt4(__v: String): SendLoginVerificationCodeReq = copy(ext4 = __v)

  def withExt5(__v: String): SendLoginVerificationCodeReq = copy(ext5 = __v)

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
        val __t = mobile
        if (__t != "") __t else null
      }
      case 4 => {
        val __t = resend
        if (__t != false) __t else null
      }
      case 5 => {
        val __t = lastChannel
        if (__t != 0) __t else null
      }
      case 6 => {
        val __t = ext1
        if (__t != "") __t else null
      }
      case 7 => {
        val __t = ext2
        if (__t != "") __t else null
      }
      case 8 => {
        val __t = ext3
        if (__t != "") __t else null
      }
      case 9 => {
        val __t = ext4
        if (__t != "") __t else null
      }
      case 10 => {
        val __t = ext5
        if (__t != "") __t else null
      }
    }
  }

  override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)

  def companion = com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq
}

object SendLoginVerificationCodeReq extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq] = this

  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq = {
    require(__fieldsMap.keys.forall(_.getContainingType() == descriptor), "FieldDescriptor does not match message type.")
    val __fields = descriptor.getFields
    com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq(
      __fieldsMap.getOrElse(__fields.get(0), 0).asInstanceOf[Int],
      __fieldsMap.getOrElse(__fields.get(1), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(2), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(3), false).asInstanceOf[Boolean],
      __fieldsMap.getOrElse(__fields.get(4), 0).asInstanceOf[Int],
      __fieldsMap.getOrElse(__fields.get(5), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(6), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(7), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(8), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(9), "").asInstanceOf[String]
    )
  }

  def descriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SendcodeProto.descriptor.getMessageTypes.get(0)

  def messageCompanionForField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__field)

  def enumCompanionForField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__field)

  lazy val defaultInstance = com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq(
  )

  implicit class SendLoginVerificationCodeReqLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq](_l) {
    def deviceType: _root_.com.trueaccord.lenses.Lens[UpperPB, Int] = field(_.deviceType)((c_, f_) => c_.copy(deviceType = f_))

    def fingerPrint: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.fingerPrint)((c_, f_) => c_.copy(fingerPrint = f_))

    def mobile: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.mobile)((c_, f_) => c_.copy(mobile = f_))

    def resend: _root_.com.trueaccord.lenses.Lens[UpperPB, Boolean] = field(_.resend)((c_, f_) => c_.copy(resend = f_))

    def lastChannel: _root_.com.trueaccord.lenses.Lens[UpperPB, Int] = field(_.lastChannel)((c_, f_) => c_.copy(lastChannel = f_))

    def ext1: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext1)((c_, f_) => c_.copy(ext1 = f_))

    def ext2: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext2)((c_, f_) => c_.copy(ext2 = f_))

    def ext3: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext3)((c_, f_) => c_.copy(ext3 = f_))

    def ext4: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext4)((c_, f_) => c_.copy(ext4 = f_))

    def ext5: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext5)((c_, f_) => c_.copy(ext5 = f_))
  }

  final val DEVICETYPE_FIELD_NUMBER = 1
  final val FINGERPRINT_FIELD_NUMBER = 2
  final val MOBILE_FIELD_NUMBER = 3
  final val RESEND_FIELD_NUMBER = 4
  final val LASTCHANNEL_FIELD_NUMBER = 5
  final val EXT1_FIELD_NUMBER = 6
  final val EXT2_FIELD_NUMBER = 7
  final val EXT3_FIELD_NUMBER = 8
  final val EXT4_FIELD_NUMBER = 9
  final val EXT5_FIELD_NUMBER = 10
}