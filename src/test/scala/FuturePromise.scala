
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.apigateway.helper.Constant
import com.lawsofnature.apigateway.request.{CheckIdentityRequest, RegisterRequest}
import com.lawsofnature.common.helper.JsonHelper

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {
  println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15881126718qqq", 1)), Constant.defaultDesKey))
//  println(DESUtils.encrypt(JsonHelper.writeValueAsString(RegisterRequest("123.235874","89.458762","中国","四川","成都","高新区","天府广场1号", 2, "81fjklduuiujjsjsjs", "天华路2",1,"15987654323","ko890890")), Constant.defaultDesKey))
  println(DESUtils.decrypt("3FDF117D9E5A6AB77B6F88B90D35B0BDA98DF0FAF1A480F0F29D7152252728EBC259F1B61B1BA36DD800B4AB6D17C3E372362835DE9A8AF315312059E8E7E6DD80116338C71AD3DD077F6693DFDA3E8591FF7F3163725D17CBBB0891F98E65BD", Constant.defaultDesKey))
}
