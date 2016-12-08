
import com.lawsofnature.apigateway.helper.Constants
import com.lawsofnature.apigateway.request._
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.helper.JsonHelper

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {
//    println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckUsernameRequest("winner1")), Constants.defaultDesKey))
//    println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15987654321", 1)), Constants.defaultDesKey))
//  println(DESUtils.encrypt(JsonHelper.writeValueAsString(RegisterRequest("123.235874", "89.333762", "中国", "四川", "成都", "高新区", null, 1, "1AAAAAAAABBBBBBBB", "winner", 1, "15987654320", "ko890890")), Constants.defaultDesKey))
//  println(DESUtils.encrypt(JsonHelper.writeValueAsString(AppLoginRequest(1, 1, "1AAAAAAAABBBBBBBB", "123.235874", "89.458762", "中国", "四川", "成都", "高新区", null, "winner", "ko890890")), Constants.defaultDesKey))

//  println(DESUtils.decrypt("3FDF117D9E5A6AB705F541CDE2DFE91708741207E866D5E29514B1F8EE9404051DCF9F2548424AEF380905EFCFD0033D38275F83D9399F043363D11DA4EB8D62C5CB4146DE9DC4E18101D57531A9342FB0B13BB7C28512CC", Constants.defaultDesKey))
  println(DESUtils.decrypt("3FDF117D9E5A6AB7C22E1FFE4DEEFD1D83B79800BC26A2B790F33F67972746B96481EEB598DCCE6A0593F872864735EAE5D64C62577BBB907C05331890C6550D", Constants.defaultDesKey))
//  println(DESUtils.decrypt("3FDF117D9E5A6AB7C22E1FFE4DEEFD1D83B79800BC26A2B790F33F67972746B96481EEB598DCCE6A0593F872864735EAE5D64C62577BBB907C05331890C6550D", "C7FC2B8E"))


//  {"code":0,"data":{"success":true,"code":0,"token":"1477A03F40","salt":"C7FC2B8E","clientId":1,"memberId":19,"status":0,"gmtCreate":1481184131197,"lastAccessTime":1481184131197}}

  //  println("null test")
  //  for (i <- 1 to 10000) {
  //    val millis: Long = System.currentTimeMillis()
  //    println(JsonHelper.writeValueAsString(ApiResponse.makeSuccessResponse(SuccessResponse.SUCCESS)))
  //    println("cost:" + (System.currentTimeMillis() - millis))
  //  }

}
