package com.akkatraining.scala.cluster

import akka.actor.Actor
import akka.event.Logging


class SimpleActor extends Actor {
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case o =>
      log.info("Received: {} at {}", o.toString, self.path.toString)
  }
}
