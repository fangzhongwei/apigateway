
import com.lawsofnature.apigateway.helper.Constants
import com.lawsofnature.apigateway.request.{AppLoginRequest, CheckIdentityRequest, RegisterRequest}
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.helper.JsonHelper

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {
    println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15987654312", 1)), Constants.defaultDesKey))
//  println(DESUtils.encrypt(JsonHelper.writeValueAsString(RegisterRequest("123.235874", "89.458762", "中国", "四川", "成都", "高新区", null, 1, "1AAAAAAAABBBBBBBB", "天华路4", 1, "15987654329", "ko890890")), Constants.defaultDesKey))
//  private val string: String = JsonHelper.writeValueAsString(AppLoginRequest(1, 1, "1AAAAAAAABBBBBBBB", "123.235874", "89.458762", "中国", "四川", "成都", "高新区", null, "15987654329", "ko890890"))
//  println(string)
//  println(DESUtils.encrypt(string, Constants.defaultDesKey))
  println(DESUtils.decrypt("3FDF117D9E5A6AB79810FB47BB31B71FB6BFF37221A9E47B622A28626B371FE6BA6EB8AA7EDCE7F95019F86EE7DE434971646C77707C35BFE9C753BD9C1DD526C043CEF596047949", Constants.defaultDesKey))


  //  println("null test")
  //  for (i <- 1 to 10000) {
  //    val millis: Long = System.currentTimeMillis()
  //    println(JsonHelper.writeValueAsString(ApiResponse.makeSuccessResponse(SuccessResponse.SUCCESS)))
  //    println("cost:" + (System.currentTimeMillis() - millis))
  //  }

}
