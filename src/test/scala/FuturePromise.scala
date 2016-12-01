
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.apigateway.helper.Constant
import com.lawsofnature.apigateway.request.{CheckIdentityRequest, RegisterRequest}
import com.lawsofnature.common.helper.JsonHelper

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {
  println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15881126718qqq", 0)), Constant.defaultDesKey))
//  println(DESUtils.encrypt(JsonHelper.writeValueAsString(RegisterRequest("123.235874","89.458762","中国","四川","成都","高新区","天府广场1号", 2, "81fjklduuiujjsjsjs", "天华路2",1,"15987654323","ko890890")), Constant.defaultDesKey))
  println(DESUtils.decrypt("3FDF117D9E5A6AB7529709DD819F3825B801645A0512D83E4DD6937D60F46B9E71AFBC7077D2D954", Constant.defaultDesKey))
}
