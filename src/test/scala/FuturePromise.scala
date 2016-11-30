
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.apigateway.helper.Constant
import com.lawsofnature.apigateway.request.{CheckIdentityRequest, RegisterRequest}
import com.lawsofnature.common.helper.JsonHelper

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {
  println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15881126718qqq", 111)), Constant.defaultDesKey))
//  println(DESUtils.encrypt(JsonHelper.writeValueAsString(RegisterRequest("123.235874","89.458762","中国","四川","成都","高新区","天府广场1号", 2, "81fjklduuiujjsjsjs", "天华路2",1,"15987654323","ko890890")), Constant.defaultDesKey))
  println(DESUtils.decrypt("3FDF117D9E5A6AB716DAB79D994146A20258EC38B2FBE57F38119E1D48B7DF00A0F4FA82B7B62F846D0790B3986B11EFB02F83EF60E182803C1A57C1211D164ED8E783C75C49FC28C52D193EA6B5057802AB4B660CECD172A5F21D36700C7E96", Constant.defaultDesKey))
}
