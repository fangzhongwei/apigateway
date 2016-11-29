import akka.actor.Actor
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.helper.{Constant, JsonHelper}
import com.lawsofnature.request.CheckIdentityRequest

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {

  println(DESUtils.encrypt(JsonHelper.writeValueAsString(CheckIdentityRequest("15881126718", 1)), Constant.defaultDesKey))
  println(DESUtils.decrypt("3FDF117D9E5A6AB79810FB47BB31B71FB6BFF37221A9E47B622A28626B371FE6BA6EB8AA7EDCE7F95019F86EE7DE434971646C77707C35BFE9C753BD9C1DD5263F92C982C0E12F48FC0025463923F7F889A09EB2752B9D21", Constant.defaultDesKey))

}
