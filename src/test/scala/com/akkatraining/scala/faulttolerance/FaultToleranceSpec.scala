package com.akkatraining.scala.faulttolerance


import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import org.scalatest.{FunSuite, Matchers}
import akka.pattern.ask

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class FaultToleranceSpec extends FunSuite with Matchers {
  test("""Lab 1: Create an AllForOneParent and Child Actor""") {

    implicit val timeout: Timeout = Timeout(10 seconds)
    val actorSystem = ActorSystem("actor-system")
    import actorSystem.dispatcher
    val allForOneParentActorRef = actorSystem.actorOf(Props[AllForOneParentActor])
    val futureRef = allForOneParentActorRef ? Props[ChildActor]

    futureRef.mapTo[ActorRef].onComplete {
      case Success(childActorRef) => childActorRef ! "IllegalArgumentException"
      case Failure(t) => println(s"Failure: ${t.getMessage}")
    }

    Thread.sleep(15000)
  }

  test("""Lab 2: Create an OneForOneParent and Child Actor""") {
    implicit val timeout: Timeout = Timeout(10 seconds)
    val actorSystem = ActorSystem("actor-system")
    import actorSystem.dispatcher
    val allForOneParentActorRef = actorSystem.actorOf(Props[OneForOneParentActor])
    val futureRef = allForOneParentActorRef ? Props[ChildActor]

    futureRef.mapTo[ActorRef].onComplete {
      case Success(childActorRef) => childActorRef ! "IllegalArgumentException"
      case Failure(t) => println(s"Failure: ${t.getMessage}")
    }

    Thread.sleep(15000)
  }
}
