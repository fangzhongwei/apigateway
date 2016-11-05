import akka.actor.Actor
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.helper.Constant

/**
  * Created by fangzhongwei on 2016/10/25.
  */

case object AskNameMessage

class TestActor extends Actor {
  def receive = {
    case AskNameMessage => // respond to the 'ask' request
      println("receive one ...")
      Thread.sleep(5000)
      sender ! "Fred"
    case _ => println("that was unexpected")
  }
}

case class ApiResponse2(code: String, msg: AnyRef, data: Option[String] = None)

object FuturePromise extends App {

  println(DESUtils.encrypt("{\"dt\":1,\"di\":\"2\",\"un\":\"fangzhongwei12\",\"pid\":1,\"m\":\"15881126711\",\"e\":\"\",\"pwd\":\"123456\"}", Constant.defaultDesKey))

}
