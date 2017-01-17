// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.lawsofnature.apigateway.domain.http.resp.depositrecordresp

@SerialVersionUID(0L)
final case class DepositRecordResp(
                                    code: String = "",
                                    paymentVoucherNo: String = "",
                                    accountId: Long = 0L,
                                    memberId: Long = 0L,
                                    tradeType: Int = 0,
                                    tradeStatus: Int = 0,
                                    diamondAcount: Int = 0,
                                    amount: String = "",
                                    gmtCreate: String = "",
                                    gmtUpdate: String = "",
                                    ext1: String = "",
                                    ext2: String = "",
                                    ext3: String = "",
                                    ext4: String = "",
                                    ext5: String = ""
                                  ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[DepositRecordResp] with com.trueaccord.lenses.Updatable[DepositRecordResp] {
  @transient
  private[this] var __serializedSizeCachedValue: Int = 0

  private[this] def __computeSerializedValue(): Int = {
    var __size = 0
    if (code != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, code)
    }
    if (paymentVoucherNo != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, paymentVoucherNo)
    }
    if (accountId != 0L) {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(3, accountId)
    }
    if (memberId != 0L) {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(4, memberId)
    }
    if (tradeType != 0) {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(5, tradeType)
    }
    if (tradeStatus != 0) {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(6, tradeStatus)
    }
    if (diamondAcount != 0) {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(7, diamondAcount)
    }
    if (amount != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(8, amount)
    }
    if (gmtCreate != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(9, gmtCreate)
    }
    if (gmtUpdate != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(10, gmtUpdate)
    }
    if (ext1 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(11, ext1)
    }
    if (ext2 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(12, ext2)
    }
    if (ext3 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(13, ext3)
    }
    if (ext4 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(14, ext4)
    }
    if (ext5 != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(15, ext5)
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
      val __v = paymentVoucherNo
      if (__v != "") {
        _output__.writeString(2, __v)
      }
    };
    {
      val __v = accountId
      if (__v != 0L) {
        _output__.writeInt64(3, __v)
      }
    };
    {
      val __v = memberId
      if (__v != 0L) {
        _output__.writeInt64(4, __v)
      }
    };
    {
      val __v = tradeType
      if (__v != 0) {
        _output__.writeInt32(5, __v)
      }
    };
    {
      val __v = tradeStatus
      if (__v != 0) {
        _output__.writeInt32(6, __v)
      }
    };
    {
      val __v = diamondAcount
      if (__v != 0) {
        _output__.writeInt32(7, __v)
      }
    };
    {
      val __v = amount
      if (__v != "") {
        _output__.writeString(8, __v)
      }
    };
    {
      val __v = gmtCreate
      if (__v != "") {
        _output__.writeString(9, __v)
      }
    };
    {
      val __v = gmtUpdate
      if (__v != "") {
        _output__.writeString(10, __v)
      }
    };
    {
      val __v = ext1
      if (__v != "") {
        _output__.writeString(11, __v)
      }
    };
    {
      val __v = ext2
      if (__v != "") {
        _output__.writeString(12, __v)
      }
    };
    {
      val __v = ext3
      if (__v != "") {
        _output__.writeString(13, __v)
      }
    };
    {
      val __v = ext4
      if (__v != "") {
        _output__.writeString(14, __v)
      }
    };
    {
      val __v = ext5
      if (__v != "") {
        _output__.writeString(15, __v)
      }
    };
  }

  def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp = {
    var __code = this.code
    var __paymentVoucherNo = this.paymentVoucherNo
    var __accountId = this.accountId
    var __memberId = this.memberId
    var __tradeType = this.tradeType
    var __tradeStatus = this.tradeStatus
    var __diamondAcount = this.diamondAcount
    var __amount = this.amount
    var __gmtCreate = this.gmtCreate
    var __gmtUpdate = this.gmtUpdate
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
        case 18 =>
          __paymentVoucherNo = _input__.readString()
        case 24 =>
          __accountId = _input__.readInt64()
        case 32 =>
          __memberId = _input__.readInt64()
        case 40 =>
          __tradeType = _input__.readInt32()
        case 48 =>
          __tradeStatus = _input__.readInt32()
        case 56 =>
          __diamondAcount = _input__.readInt32()
        case 66 =>
          __amount = _input__.readString()
        case 74 =>
          __gmtCreate = _input__.readString()
        case 82 =>
          __gmtUpdate = _input__.readString()
        case 90 =>
          __ext1 = _input__.readString()
        case 98 =>
          __ext2 = _input__.readString()
        case 106 =>
          __ext3 = _input__.readString()
        case 114 =>
          __ext4 = _input__.readString()
        case 122 =>
          __ext5 = _input__.readString()
        case tag => _input__.skipField(tag)
      }
    }
    com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp(
      code = __code,
      paymentVoucherNo = __paymentVoucherNo,
      accountId = __accountId,
      memberId = __memberId,
      tradeType = __tradeType,
      tradeStatus = __tradeStatus,
      diamondAcount = __diamondAcount,
      amount = __amount,
      gmtCreate = __gmtCreate,
      gmtUpdate = __gmtUpdate,
      ext1 = __ext1,
      ext2 = __ext2,
      ext3 = __ext3,
      ext4 = __ext4,
      ext5 = __ext5
    )
  }

  def withCode(__v: String): DepositRecordResp = copy(code = __v)

  def withPaymentVoucherNo(__v: String): DepositRecordResp = copy(paymentVoucherNo = __v)

  def withAccountId(__v: Long): DepositRecordResp = copy(accountId = __v)

  def withMemberId(__v: Long): DepositRecordResp = copy(memberId = __v)

  def withTradeType(__v: Int): DepositRecordResp = copy(tradeType = __v)

  def withTradeStatus(__v: Int): DepositRecordResp = copy(tradeStatus = __v)

  def withDiamondAcount(__v: Int): DepositRecordResp = copy(diamondAcount = __v)

  def withAmount(__v: String): DepositRecordResp = copy(amount = __v)

  def withGmtCreate(__v: String): DepositRecordResp = copy(gmtCreate = __v)

  def withGmtUpdate(__v: String): DepositRecordResp = copy(gmtUpdate = __v)

  def withExt1(__v: String): DepositRecordResp = copy(ext1 = __v)

  def withExt2(__v: String): DepositRecordResp = copy(ext2 = __v)

  def withExt3(__v: String): DepositRecordResp = copy(ext3 = __v)

  def withExt4(__v: String): DepositRecordResp = copy(ext4 = __v)

  def withExt5(__v: String): DepositRecordResp = copy(ext5 = __v)

  def getField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): scala.Any = {
    __field.getNumber match {
      case 1 => {
        val __t = code
        if (__t != "") __t else null
      }
      case 2 => {
        val __t = paymentVoucherNo
        if (__t != "") __t else null
      }
      case 3 => {
        val __t = accountId
        if (__t != 0L) __t else null
      }
      case 4 => {
        val __t = memberId
        if (__t != 0L) __t else null
      }
      case 5 => {
        val __t = tradeType
        if (__t != 0) __t else null
      }
      case 6 => {
        val __t = tradeStatus
        if (__t != 0) __t else null
      }
      case 7 => {
        val __t = diamondAcount
        if (__t != 0) __t else null
      }
      case 8 => {
        val __t = amount
        if (__t != "") __t else null
      }
      case 9 => {
        val __t = gmtCreate
        if (__t != "") __t else null
      }
      case 10 => {
        val __t = gmtUpdate
        if (__t != "") __t else null
      }
      case 11 => {
        val __t = ext1
        if (__t != "") __t else null
      }
      case 12 => {
        val __t = ext2
        if (__t != "") __t else null
      }
      case 13 => {
        val __t = ext3
        if (__t != "") __t else null
      }
      case 14 => {
        val __t = ext4
        if (__t != "") __t else null
      }
      case 15 => {
        val __t = ext5
        if (__t != "") __t else null
      }
    }
  }

  override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)

  def companion = com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp
}

object DepositRecordResp extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp] = this

  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp = {
    require(__fieldsMap.keys.forall(_.getContainingType() == descriptor), "FieldDescriptor does not match message type.")
    val __fields = descriptor.getFields
    com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp(
      __fieldsMap.getOrElse(__fields.get(0), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(1), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(2), 0L).asInstanceOf[Long],
      __fieldsMap.getOrElse(__fields.get(3), 0L).asInstanceOf[Long],
      __fieldsMap.getOrElse(__fields.get(4), 0).asInstanceOf[Int],
      __fieldsMap.getOrElse(__fields.get(5), 0).asInstanceOf[Int],
      __fieldsMap.getOrElse(__fields.get(6), 0).asInstanceOf[Int],
      __fieldsMap.getOrElse(__fields.get(7), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(8), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(9), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(10), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(11), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(12), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(13), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(14), "").asInstanceOf[String]
    )
  }

  def descriptor: _root_.com.google.protobuf.Descriptors.Descriptor = DepositrecordrespProto.descriptor.getMessageTypes.get(0)

  def messageCompanionForField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__field)

  def enumCompanionForField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__field)

  lazy val defaultInstance = com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp(
  )

  implicit class DepositRecordRespLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.lawsofnature.apigateway.domain.http.resp.depositrecordresp.DepositRecordResp](_l) {
    def code: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.code)((c_, f_) => c_.copy(code = f_))

    def paymentVoucherNo: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.paymentVoucherNo)((c_, f_) => c_.copy(paymentVoucherNo = f_))

    def accountId: _root_.com.trueaccord.lenses.Lens[UpperPB, Long] = field(_.accountId)((c_, f_) => c_.copy(accountId = f_))

    def memberId: _root_.com.trueaccord.lenses.Lens[UpperPB, Long] = field(_.memberId)((c_, f_) => c_.copy(memberId = f_))

    def tradeType: _root_.com.trueaccord.lenses.Lens[UpperPB, Int] = field(_.tradeType)((c_, f_) => c_.copy(tradeType = f_))

    def tradeStatus: _root_.com.trueaccord.lenses.Lens[UpperPB, Int] = field(_.tradeStatus)((c_, f_) => c_.copy(tradeStatus = f_))

    def diamondAcount: _root_.com.trueaccord.lenses.Lens[UpperPB, Int] = field(_.diamondAcount)((c_, f_) => c_.copy(diamondAcount = f_))

    def amount: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.amount)((c_, f_) => c_.copy(amount = f_))

    def gmtCreate: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.gmtCreate)((c_, f_) => c_.copy(gmtCreate = f_))

    def gmtUpdate: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.gmtUpdate)((c_, f_) => c_.copy(gmtUpdate = f_))

    def ext1: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext1)((c_, f_) => c_.copy(ext1 = f_))

    def ext2: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext2)((c_, f_) => c_.copy(ext2 = f_))

    def ext3: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext3)((c_, f_) => c_.copy(ext3 = f_))

    def ext4: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext4)((c_, f_) => c_.copy(ext4 = f_))

    def ext5: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.ext5)((c_, f_) => c_.copy(ext5 = f_))
  }

  final val CODE_FIELD_NUMBER = 1
  final val PAYMENTVOUCHERNO_FIELD_NUMBER = 2
  final val ACCOUNTID_FIELD_NUMBER = 3
  final val MEMBERID_FIELD_NUMBER = 4
  final val TRADETYPE_FIELD_NUMBER = 5
  final val TRADESTATUS_FIELD_NUMBER = 6
  final val DIAMONDACOUNT_FIELD_NUMBER = 7
  final val AMOUNT_FIELD_NUMBER = 8
  final val GMTCREATE_FIELD_NUMBER = 9
  final val GMTUPDATE_FIELD_NUMBER = 10
  final val EXT1_FIELD_NUMBER = 11
  final val EXT2_FIELD_NUMBER = 12
  final val EXT3_FIELD_NUMBER = 13
  final val EXT4_FIELD_NUMBER = 14
  final val EXT5_FIELD_NUMBER = 15
}
