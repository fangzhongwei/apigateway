import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success}

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

object FuturePromise extends App {

  import scala.concurrent.duration._
  import akka.actor._
  import akka.pattern.ask
  import akka.util.Timeout

  //create the system and actor
  val system = ActorSystem("AskDemoSystem")
  val myActor = system.actorOf(Props[TestActor], name="myActor")

  // (1) this is one way to "ask" another actor for information
  implicit val timeout = Timeout(5 seconds)
  println("start send f")
  val future = myActor ? AskNameMessage
  println("start send f5")
  val future5 = myActor ? AskNameMessage
  val result = Await.result(future, timeout.duration).asInstanceOf[String]
  println(result)

  // (2) a slightly different way to ask another actor for information
  val future2: Future[String] = ask(myActor, AskNameMessage).mapTo[String]
  val result2 = Await.result(future2, 1 second)
  println(result2)

//  system.shutdown


//  akka.pattern.Patterns.pipe(future, system.dispatcher()).to(myActor)

}
