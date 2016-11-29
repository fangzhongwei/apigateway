import akka.actor.Actor
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.helper.{Constant, JsonHelper}
import com.lawsofnature.request.CheckIdentityRequest

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {

  println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15881126718", 1)), Constant.defaultDesKey))
  println(DESUtils.decrypt("3FDF117D9E5A6AB7529709DD819F3825963CA9A20646F1E7BBD690960A2EA491", Constant.defaultDesKey))

}
