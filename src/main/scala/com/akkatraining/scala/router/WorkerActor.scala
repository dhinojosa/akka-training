package com.akkatraining.scala.router

import akka.actor.Actor
import akka.event.Logging


class WorkerActor extends Actor {
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case "terminate" =>
      context.stop(self)
    case o =>
      log.info("Received: {} at {}", o.toString, self.path.toString)
  }
}
