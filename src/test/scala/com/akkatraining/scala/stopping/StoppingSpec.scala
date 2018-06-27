package com.akkatraining.scala.stopping

import akka.actor.{ActorSystem, Kill, PoisonPill, Props}
import org.scalatest.{FunSuite, Matchers}

import scala.util.{Failure, Success}

class StoppingSpec extends FunSuite with Matchers {
  test(
    """Case 1: Create an ActorSystem called actorSystem, create a
      |  DeathListenerActor in actorSystem
      |  Create VictimActor in actorSystem
      |  Register VictimActor with DeathListenerActor
      |  Stop VictimActor using stop methodology
      |  Place code that shuts down the ActorSystem with
      |  Use a thread sleep""".stripMargin) {

    val actorSystem = ActorSystem("my-actor-system")
    val deathListenerActor = actorSystem.actorOf(Props[DeathListenerActor])
    val victimActor = actorSystem.actorOf(Props[VictimActor])

    deathListenerActor ! victimActor

    for (i <- 0 to 4) victimActor ! s"Message: $i"
    actorSystem.stop(victimActor)
    for (i <- 5 to 8) victimActor ! s"Message: $i"

    Thread.sleep(15000) //last item
  }


  test(
    """Case 2: Create an ActorSystem called actorSystem, create a
      |  DeathListenerActor in actorSystem
      |  Create VictimActor in actorSystem
      |  Register VictimActor with DeathListenerActor
      |  Stop VictimActor using a poison pill
      |  Place code that shuts down the ActorSystem with
      |  Use a thread sleep""".stripMargin) {

    val actorSystem = ActorSystem("my-actor-system")
    val deathListenerActor = actorSystem.actorOf(Props[DeathListenerActor])
    val victimActor = actorSystem.actorOf(Props[VictimActor])

    deathListenerActor ! victimActor

    for (i <- 0 to 4) victimActor ! s"Message: $i"
    victimActor ! PoisonPill.getInstance
    for (i <- 5 to 8) victimActor ! s"Message: $i"

    Thread.sleep(15000) //last item
  }

  test(
    """Case 3: Create an ActorSystem called actorSystem, create a
      |  DeathListenerActor in actorSystem
      |  Create VictimActor in actorSystem
      |  Register VictimActor with DeathListenerActor
      |  Stop VictimActor using kill
      |  Place code that shuts down the ActorSystem with
      |  Use a thread sleep""".stripMargin) {

    val actorSystem = ActorSystem("my-actor-system")
    val deathListenerActor = actorSystem.actorOf(Props[DeathListenerActor])
    val victimActor = actorSystem.actorOf(Props[VictimActor])

    deathListenerActor ! victimActor

    for (i <- 0 to 4) victimActor ! s"Message: $i"
    victimActor ! Kill
    for (i <- 5 to 8) victimActor ! s"Message: $i"

    Thread.sleep(15000) //last item
  }

  test(
    """Case 4: Create an ActorSystem called actorSystem, create a
      |  DeathListenerActor in actorSystem
      |  Create VictimActor in actorSystem
      |  Register VictimActor with DeathListenerActor
      |  Stop VictimActor using Graceful Stop
      |  Place code that shuts down the ActorSystem with
      |  Use a thread sleep""".stripMargin) {

    import akka.pattern.gracefulStop
    import scala.concurrent.duration._

    val actorSystem = ActorSystem("my-actor-system")
    import actorSystem.dispatcher

    val deathListenerActor = actorSystem.actorOf(Props[DeathListenerActor])
    val victimActor = actorSystem.actorOf(Props[VictimActor])

    deathListenerActor ! victimActor

    for (i <- 0 to 4) victimActor ! s"Message: $i"

    val eventualBoolean = gracefulStop(victimActor, 4 seconds, GracefulStopMessage)
    eventualBoolean.onComplete {
      case Success(x) if x => println("Successful future, graceful shutdown timely")
      case Success(y) if !y => println("Successful future, but graceful shutdown not timely")
      case Failure(_) => println("Failed future")
    }

    for (i <- 5 to 8) victimActor ! s"Message: $i"

    Thread.sleep(15000) //last item
  }
}
