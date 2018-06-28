package com.akkatraining.scala.schedulers

import akka.actor.{Actor, ActorLogging}

class SimpleActorScala extends Actor with ActorLogging {
  override def receive: Receive = {
    case x => log.info(s"Received Message $x", x)
  }
}
