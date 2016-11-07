import akka.actor.Actor
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.helper.Constant

/**
  * Created by fangzhongwei on 2016/10/25.
  */

object FuturePromise extends App {

  println(DESUtils.encrypt("{\"ti\":\"aaabbbccc\",\"dt\":1,\"di\":\"2\",\"un\":\"fangzhongwei12\",\"pid\":1,\"m\":\"158811267111\",\"e\":\"\",\"pwd\":\"a123456\"}", Constant.defaultDesKey))

}
