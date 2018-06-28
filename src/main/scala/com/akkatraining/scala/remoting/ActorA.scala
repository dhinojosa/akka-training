package com.akkatraining.scala.remoting

import akka.actor.Actor
import akka.event.Logging


class ActorA extends Actor {
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case o =>
      log.info("Received: {}", o.toString)
      sender() ! ("Hello to you", self)
      sender() ! "Hello to you"
  }
}
