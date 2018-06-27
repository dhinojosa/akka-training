package com.akkatraining.scala.stopping

import akka.actor.{
  Actor, ActorLogging,
  ActorRef, Terminated
}

class DeathListenerActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case a: ActorRef =>
      context watch a
    case Terminated(actorRef) =>
      log.info(
        s"""${actorRef.path.toString}
           |  has been terminated""".stripMargin)
  }
}
