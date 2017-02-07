// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.jxjxgo.apigateway.domain.http.resp

@SerialVersionUID(0L)
final case class SendLoginVerificationCodeResp(
                                                code: String = "",
                                                channel: Int = 0,
                                                ext1: String = "",
                                                ext2: String = "",
                                                ext3: String = "",
                                                ext4: String = "",
                                                ext5: String = ""
                                              ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[SendLoginVerificationCodeResp] with com.trueaccord.lenses.Updatable[SendLoginVerificationCodeResp] {
  @transient
  private[this] var __serializedSizeCachedValue: Int = 0

  private[this] def __computeSerializedValue(): Int = {
    var __size = 0
    if (code != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, code)
    }
    if (channel != 0) {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(2, channel)
    }
    if (ext1 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, ext1)
    }
    if (ext2 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, ext2)
    }
    if (ext3 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(5, ext3)
    }
    if (ext4 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(6, ext4)
    }
    if (ext5 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(7, ext5)
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
      val __v = code
      if (__v != "") {
        _output__.writeString(1, __v)
      }
    };
    {
      val __v = channel
      if (__v != 0) {
        _output__.writeInt32(2, __v)
      }
    };
    {
      val __v = ext1
      if (__v != "") {
        _output__.writeString(3, __v)
      }
    };
    {
      val __v = ext2
      if (__v != "") {
        _output__.writeString(4, __v)
      }
    };
    {
      val __v = ext3
      if (__v != "") {
        _output__.writeString(5, __v)
      }
    };
    {
      val __v = ext4
      if (__v != "") {
        _output__.writeString(6, __v)
      }
    };
    {
      val __v = ext5
      if (__v != "") {
        _output__.writeString(7, __v)
      }
    };
  }

  def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp = {
    var __code = this.code
    var __channel = this.channel
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
        case 10 =>
          __code = _input__.readString()
        case 16 =>
          __channel = _input__.readInt32()
        case 26 =>
          __ext1 = _input__.readString()
        case 34 =>
          __ext2 = _input__.readString()
        case 42 =>
          __ext3 = _input__.readString()
        case 50 =>
          __ext4 = _input__.readString()
        case 58 =>
          __ext5 = _input__.readString()
        case tag => _input__.skipField(tag)
      }
    }
    com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp(
      code = __code,
      channel = __channel,
      ext1 = __ext1,
      ext2 = __ext2,
      ext3 = __ext3,
      ext4 = __ext4,
      ext5 = __ext5
    )
  }

  def withCode(__v: String): SendLoginVerificationCodeResp = copy(code = __v)

  def withChannel(__v: Int): SendLoginVerificationCodeResp = copy(channel = __v)

  def withExt1(__v: String): SendLoginVerificationCodeResp = copy(ext1 = __v)

  def withExt2(__v: String): SendLoginVerificationCodeResp = copy(ext2 = __v)

  def withExt3(__v: String): SendLoginVerificationCodeResp = copy(ext3 = __v)

  def withExt4(__v: String): SendLoginVerificationCodeResp = copy(ext4 = __v)

  def withExt5(__v: String): SendLoginVerificationCodeResp = copy(ext5 = __v)

  def getField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): scala.Any = {
    __field.getNumber match {
      case 1 => {
        val __t = code
        if (__t != "") __t else null
      }
      case 2 => {
        val __t = channel
        if (__t != 0) __t else null
      }
      case 3 => {
        val __t = ext1
        if (__t != "") __t else null
      }
      case 4 => {
        val __t = ext2
        if (__t != "") __t else null
      }
      case 5 => {
        val __t = ext3
        if (__t != "") __t else null
      }
      case 6 => {
        val __t = ext4
        if (__t != "") __t else null
      }
      case 7 => {
        val __t = ext5
        if (__t != "") __t else null
      }
    }
  }

  override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)

  def companion = com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp
}

object SendLoginVerificationCodeResp extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp] = this

  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp = {
    require(__fieldsMap.keys.forall(_.getContainingType() == descriptor), "FieldDescriptor does not match message type.")
    val __fields = descriptor.getFields
    com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp(
      __fieldsMap.getOrElse(__fields.get(0), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(1), 0).asInstanceOf[Int],
      __fieldsMap.getOrElse(__fields.get(2), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(3), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(4), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(5), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(6), "").asInstanceOf[String]
    )
  }

  def descriptor: _root_.com.google.protobuf.Descriptors.Descriptor = RespProto.descriptor.getMessageTypes.get(1)

  def messageCompanionForField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__field)

  def enumCompanionForField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__field)

  lazy val defaultInstance = com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp(
  )

  implicit class SendLoginVerificationCodeRespLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.jxjxgo.apigateway.domain.http.resp.SendLoginVerificationCodeResp](_l) {
    def code: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.code)((c_, f_) => c_.copy(code = f_))

    def channel: _root_.com.trueaccord.lenses.Lens[UpperPB, Int] = field(_.channel)((c_, f_) => c_.copy(channel = f_))

    def ext1: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext1)((c_, f_) => c_.copy(ext1 = f_))

    def ext2: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext2)((c_, f_) => c_.copy(ext2 = f_))

    def ext3: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext3)((c_, f_) => c_.copy(ext3 = f_))

    def ext4: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext4)((c_, f_) => c_.copy(ext4 = f_))

    def ext5: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext5)((c_, f_) => c_.copy(ext5 = f_))
  }

  final val CODE_FIELD_NUMBER = 1
  final val CHANNEL_FIELD_NUMBER = 2
  final val EXT1_FIELD_NUMBER = 3
  final val EXT2_FIELD_NUMBER = 4
  final val EXT3_FIELD_NUMBER = 5
  final val EXT4_FIELD_NUMBER = 6
  final val EXT5_FIELD_NUMBER = 7
}
