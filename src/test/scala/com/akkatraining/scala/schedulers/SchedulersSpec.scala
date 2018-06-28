package com.akkatraining.scala.schedulers

import akka.actor.{ActorSystem, Props}
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Await

class SchedulersSpec extends FunSuite with Matchers {
  test(
    """Lab 1: Create an ActorSystem called actorSystem,
      |  and create a SimpleActorScala inside of the
      |  ActorSystem. Send a ping message every 5 seconds
      |  and after 30 seconds cancel.""".stripMargin) {

    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global

    val actorSystem = ActorSystem("My-Actor-System")
    val simpleActorScalaRef = actorSystem.actorOf(Props[SimpleActorScala])

    val cancellable = actorSystem.scheduler.schedule(
      0 seconds,
      5 seconds,
      simpleActorScalaRef, "Ping")

    Thread.sleep(30000) //sleep for 30 sec on main thread
    cancellable.cancel()
    Thread.sleep(2000)

    Await.ready(actorSystem.terminate(), 1 second)
  }
}