package com.akkatraining.scala.faulttolerance

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorLogging, AllForOneStrategy, OneForOneStrategy, Props, SupervisorStrategy}

import scala.concurrent.duration._

class OneForOneParentActor extends Actor with ActorLogging {

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1 second) {
      case _: IllegalArgumentException => Restart
      case _ => Escalate
    }

  override def receive: Receive = {
    case p:Props =>
      val seq = for (i <- 0 to 9) yield context.actorOf(p, s"child-$i")
      sender ! seq(scala.util.Random.nextInt(10))
  }
}
