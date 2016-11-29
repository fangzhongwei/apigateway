import akka.actor.Actor
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.helper.{Constant, JsonHelper}
import com.lawsofnature.request.CheckIdentityRequest

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {

  println(DESUtils.encrypt("{\"lat\":\"100.523523\",\"lng\":\"50.255474\",\"dt\":1,\"di\":\"2\",\"un\":\"fangzhongweikk\",\"pid\":1,\"i\":\"15881167999\",\"pwd\":\"a1234569\"}", Constant.defaultDesKey))
//  println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15881126718", 1)), Constant.defaultDesKey))
  println(DESUtils.decrypt("3FDF117D9E5A6AB7529709DD819F38250D1AB38C58F0604F2F84B97B2FF7E3E65884DF71ED0B82A17FB60C6A6F86147B", Constant.defaultDesKey))


}
