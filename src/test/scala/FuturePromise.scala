
import com.jxjxgo.apigateway.domain.http.req.login.LoginReq
import com.jxjxgo.apigateway.domain.http.req.sendcode.SendLoginVerificationCodeReq
import com.jxjxgo.common.edecrypt.DESUtils
import com.jxjxgo.common.helper.{GZipHelper, IPv4Helper}

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {
  //    println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckUsernameRequest("winner1")), Constants.defaultDesKey))
  //    println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15987654321", 1)), Constants.defaultDesKey))
  //  println(DESUtils.encrypt(JsonHelper.writeValueAsString(RegisterRequest("123.235874", "89.333762", "中国", "四川", "成都", "高新区", null, 1, "1AAAAAAAABBBBBBBB", "winner", 1, "15987654320", "ko890890")), Constants.defaultDesKey))
  //  private val string: String = JsonHelper.writeValueAsString(AppLoginRequest(1, 1, "1AAAAAAAABBBBBBBB", "123.235874", "89.458762", "中国", "四川", "成都", "高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区高新区", null, "winner", "ko890890"))
  //  private val bytes1: Array[Byte] = string.getBytes(StandardCharsets.UTF_8)
  //  println(bytes1.length)
  //  private val compress: Array[Byte] = GZipHelper.compress(bytes1)
  //  println(compress.length)
  //  private val bytes: Array[Byte] = DESUtils.encrypt(compress, Constants.defaultDesKey)
  //  println(bytes.length)
  //
  ////  println(DESUtils.decrypt("3FDF117D9E5A6AB705F541CDE2DFE91708741207E866D5E29514B1F8EE9404051DCF9F2548424AEF380905EFCFD0033D38275F83D9399F043363D11DA4EB8D62C5CB4146DE9DC4E18101D57531A9342FB0B13BB7C28512CC", Constants.defaultDesKey))
  ////  println(DESUtils.decrypt("3FDF117D9E5A6AB711FCFFA85D5ADC2CC8AC962F9F7D5296D4783FFA322A14E84C3C21FF5DCC2535B663BC81CD1A0D3860A54053B0AA9F915BC9C5E7312053675B6D2B6537BEB872C411EE5957392435478B9BFBA9E281BF8425B5A15542BA3BF8B50E6166625668D30EE30BA80D8BB62A5472792AAFD5DEBAADBEFB5AAEDDDC2257C5DEDA7F3EE63E6136D47069A825553DF78222E8577B02C90FEF86263DAD750B9B3A55BF203127499FFE3863CD5911BC6BD58BEBB708", Constants.defaultDesKey))
  ////  println(DESUtils.decrypt("DCEB873376E5CE572A404E8820E8D1ABF0683EE0726B977FBD715E83372571C9A288287FBC6E6B1658F73D56CB7F7621B09A9885FD97F97F87468450CFFFA373", "8C34E776"))
  //
  //  val bos = new BufferedOutputStream(new FileOutputStream("d:\\gzip_en.dat"))
  //  Stream.continually(bos.write(bytes))
  //  bos.flush()
  //  bos.close() // You may end up with 0 bytes file if not calling close.
  //
  //
  //  def constructHttpData(json:String, key:String, ignoreEncrypt:Boolean): Array[Byte] = {
  //    ignoreEncrypt match {
  //      case true => GZipHelper.compress(json.getBytes(StandardCharsets.UTF_8))
  //      case false => DESUtils.encrypt(GZipHelper.compress(json.getBytes(StandardCharsets.UTF_8)), Constants.defaultDesKey)
  //    }
  //  }
  //
  //  private val millis: Long = System.currentTimeMillis()
  //  for (i <- 1 to 100000) {
  //    val string1: String = JsonHelper.writeValueAsString(AppLoginRequest(1, 1, "abc", "123.235874", "89.458762", "中国", "123456", "成都", "11", null, "winner", "ko890890"))
  //    println(string1.getBytes(StandardCharsets.UTF_8).length)
  //    println(constructHttpData(string1, Constants.defaultDesKey, false).length)
  //  }
  //
  //  var cost = System.currentTimeMillis() - millis
  //  println("cost :" + cost)
  //  println("cost :" + cost/100000)


//  import java.nio.file.{Files, Paths}
//
//  val byteArray = Files.readAllBytes(Paths.get("C:\\Users\\fangzhongwei\\RiderProjects\\Proto3Cs4\\Proto3Cs4\\bin\\Debug\\LoginReq.dat"))
//
//  println(byteArray.length)
//
//  private val compress: Array[Byte] = GZipHelper.compress(byteArray)
//
//  println(compress.length)
//
//  private val encrypt: Array[Byte] = DESUtils.encrypt(compress, "1234ABCD")
//  println(encrypt.length)
//
//  private val from: LoginReq = LoginReq.parseFrom(byteArray)
//
//  println(from.fingerPrint)
//  println(from.clientId)


  import java.nio.file.{Files, Paths}

  val byteArray = Files.readAllBytes(Paths.get("C:\\Users\\fangzhongwei\\Desktop\\Persons.dat"))

  private val req: SendLoginVerificationCodeReq = SendLoginVerificationCodeReq.parseFrom(byteArray)

  println(req)

}
