
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.apigateway.helper.Constant
import com.lawsofnature.apigateway.request.{CheckIdentityRequest, RegisterRequest}
import com.lawsofnature.common.helper.JsonHelper

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {
  println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15881126718", 10)), Constant.defaultDesKey))
  println(DESUtils.encrypt(JsonHelper.writeValueAsString(RegisterRequest("123.235874","89.458762","中国","四川","成都","高新区","天府广场1号", 2, "81fjklduuiujjsjsjs", "天华路2",1,"15987654323","ko890890")), Constant.defaultDesKey))
  println(DESUtils.decrypt("3FDF117D9E5A6AB7529709DD819F38250D1AB38C58F0604F2F84B97B2FF7E3E65884DF71ED0B82A17FB60C6A6F86147B", Constant.defaultDesKey))
  println(DESUtils.decrypt("3FDF117D9E5A6AB7529709DD819F3825963CA9A20646F1E7BBD690960A2EA491", Constant.defaultDesKey))
}
