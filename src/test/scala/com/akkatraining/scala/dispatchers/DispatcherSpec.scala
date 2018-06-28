package com.akkatraining.scala.dispatchers

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class DispatcherSpec extends FunSuite with Matchers {
  test(
    """Case 1: Running into trouble by using a
      |  blocking actor with a the same dispatcher.
      |  This should cause a bottleneck behavior, but still run""".stripMargin) {

    val actorSystem = ActorSystem("My-System") //Standard Dispatcher
    val actor1 = actorSystem.actorOf(Props(new BlockingActor))
    val actor2 = actorSystem.actorOf(Props(new PrintActor))

    for (i ← 1 to 100) {
      actor1 ! i
      actor2 ! i
    }

    Thread.sleep(2 * 60 * 1000) //2 minutes
    Await.ready(actorSystem.terminate(), 10 seconds)
  }

  test(
    """Case 2: Running into trouble by using a
      |  blocking actor with a the same dispatcher, and the blocking actor
      |  this time will be using its own ExecutionContext and using a Future,
      |  and it seems to be worse!""".stripMargin) {

    val actorSystem = ActorSystem("My-System") //Standard Dispatcher
    val actor1 = actorSystem.actorOf(Props(new BlockingFutureActor))
    val actor2 = actorSystem.actorOf(Props(new PrintActor))

    for (i ← 1 to 100) {
      actor1 ! i
      actor2 ! i
    }

    Thread.sleep(2 * 60 * 1000) //2 minutes
    Await.ready(actorSystem.terminate(), 10 seconds)
  }


  test("""Case 3: Now here it is running in
      |  each it's separate dispatcher""".stripMargin) {

    val config = ConfigFactory.load()
    val actorSystem = ActorSystem("My-System",
      config.getConfig("custom-dispatcher-akka")
        .withFallback(config)) //Using a specialized dispatcher


    val actor1 = actorSystem.actorOf(Props(new SeparateDispatcherFutureActor))
    val actor2 = actorSystem.actorOf(Props(new PrintActor))

    for (i ← 1 to 100) {
      actor1 ! i
      actor2 ! i
    }

    Thread.sleep(2 * 60 * 1000) //2 minutes
    Await.ready(actorSystem.terminate(), 10 seconds)
  }
}
