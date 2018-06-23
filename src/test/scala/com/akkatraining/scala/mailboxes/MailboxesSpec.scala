package com.akkatraining.scala.mailboxes

import akka.actor.{ActorSystem, PoisonPill, Props}
import com.typesafe.config.ConfigFactory
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

class MailboxesSpec extends FunSuite with Matchers {

  test("Case 1: Using a default mailbox") {
    val config = ConfigFactory.load()
    val actorSystem = ActorSystem("actorSystem")
    val myActor = actorSystem.actorOf(Props[MailboxActor], "mailboxActor")
    for (i <- 1 to 50) myActor ! s"Ping $i"
    Thread.sleep(3000)
    Await.ready(actorSystem.terminate(), 3 seconds)
  }

  test(
    """Case 2: Using a different mailbox given a bounded mailbox,
      |  where configured.""".stripMargin) {
    val config = ConfigFactory.load()
    val actorSystem = ActorSystem("actorSystem", config
      .getConfig("custom-mailbox-akka").withFallback(config))
    val myActor = actorSystem.actorOf(Props[MailboxActor], "mailboxActor")
    for (i <- 1 to 50) myActor ! s"Ping $i"
    Thread.sleep(3000)
    Await.ready(actorSystem.terminate(), 3 seconds)
  }

  test(
    """Case 3: Using a different mailbox given a configuration
      |  but direct reference mailbox at creation time""".stripMargin) {
    val config = ConfigFactory.load()
    val actorSystem = ActorSystem("actorSystem", config
      .getConfig("custom-mailbox-akka").withFallback(config))
    val myActor = actorSystem.actorOf(Props[MailboxActor]
      .withMailbox("non-blocking-bounded"), "mailboxActor2")
    for (i <- 1 to 50) myActor ! s"Ping $i"
    Thread.sleep(3000)
    Await.ready(actorSystem.terminate(), 3 seconds)
  }

  test(
    """Case 4: Using a different mailbox given a
      |  contraint with the actor, so that you are
      |  forced to use a specific type""".stripMargin) {

    val config = ConfigFactory.load()
    val actorSystem = ActorSystem("actorSystem", config
      .getConfig("custom-mailbox-requirements-akka")
      .withFallback(config))
    val myActor = actorSystem
      .actorOf(Props[MailboxWithRequirementActor], "mailboxActor4")
    for (i <- 1 to 50) myActor ! s"Ping $i"
    Thread.sleep(3000)
    Await.ready(actorSystem.terminate(), 3 seconds)
  }

  test("""Case 5: Using a custom priority mailbox given and plugged
      |  in at configuration""".stripMargin) {
    val config = ConfigFactory.load()
    val actorSystem = ActorSystem("actorSystem", config
      .getConfig("custom-priority-mailbox-akka").withFallback(config))
    val myActor = actorSystem.actorOf(Props[MailboxActor]
      .withMailbox("custom-priority-mailbox"), "mailboxActor2")
    import actorSystem.dispatcher

    val messages = List(10, 12, "Hello", "Bon Jour",
      'highpriority, 'lowpriority, PoisonPill.getInstance)

    val f1 = Future {
      for (i <- 1 to 50) myActor ! messages(Random.nextInt(messages.length - 1))
    }
    val f2 = Future {
      for (i <- 1 to 50) myActor ! messages(Random.nextInt(messages.length - 1))
    }
    val f3 = Future {
      for (i <- 1 to 50) myActor ! messages(Random.nextInt(messages.length - 1))
    }

    Thread.sleep(3000)
    Await.ready(actorSystem.terminate(), 3 seconds)
  }
}
