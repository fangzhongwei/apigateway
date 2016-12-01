
import com.lawsofnature.apigateway.helper.Constant
import com.lawsofnature.apigateway.response.{ApiResponse, SuccessResponse}
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.helper.JsonHelper

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {
  //  println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15881126718qqq", 0)), Constant.defaultDesKey))
  //  println(DESUtils.encrypt(JsonHelper.writeValueAsString(RegisterRequest("123.235874","89.458762","中国","四川","成都","高新区","天府广场1号", 2, "81fjklduuiujjsjsjs", "天华路2",1,"15987654323","ko890890")), Constant.defaultDesKey))
    println(DESUtils.decrypt("3FDF117D9E5A6AB705F541CDE2DFE91708741207E866D5E29514B1F8EE9404051DCF9F2548424AEF380905EFCFD0033D38275F83D9399F043363D11DA4EB8D62C5CB4146DE9DC4E18101D57531A9342FB0B13BB7C28512CC", Constant.defaultDesKey))


//  println("null test")
//  for (i <- 1 to 10000) {
//    val millis: Long = System.currentTimeMillis()
//    println(JsonHelper.writeValueAsString(ApiResponse.makeSuccessResponse(SuccessResponse.SUCCESS)))
//    println("cost:" + (System.currentTimeMillis() - millis))
//  }

}
