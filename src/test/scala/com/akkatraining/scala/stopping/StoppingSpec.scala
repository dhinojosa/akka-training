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

   pending
  }


  test(
    """Case 2: Create an ActorSystem called actorSystem, create a
      |  DeathListenerActor in actorSystem
      |  Create VictimActor in actorSystem
      |  Register VictimActor with DeathListenerActor
      |  Stop VictimActor using a poison pill
      |  Place code that shuts down the ActorSystem with
      |  Use a thread sleep""".stripMargin) {

    pending
  }

  test(
    """Case 3: Create an ActorSystem called actorSystem, create a
      |  DeathListenerActor in actorSystem
      |  Create VictimActor in actorSystem
      |  Register VictimActor with DeathListenerActor
      |  Stop VictimActor using kill
      |  Place code that shuts down the ActorSystem with
      |  Use a thread sleep""".stripMargin) {

    pending
  }

  test(
    """Case 4: Create an ActorSystem called actorSystem, create a
      |  DeathListenerActor in actorSystem
      |  Create VictimActor in actorSystem
      |  Register VictimActor with DeathListenerActor
      |  Stop VictimActor using Graceful Stop
      |  Place code that shuts down the ActorSystem with
      |  Use a thread sleep""".stripMargin) {

    pending
  }
}
