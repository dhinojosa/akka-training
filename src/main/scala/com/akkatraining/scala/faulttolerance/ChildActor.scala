package com.akkatraining.scala.faulttolerance

import akka.actor.{Actor, ActorLogging}

trait ActorStandardOutput extends Actor {actor:ActorLogging =>
  abstract override def preStart(): Unit = {
    log.info(s"Child ${self.path.name} pre-start")
    super.preStart()
  }

  abstract override def preRestart(reason: Throwable,
                          message: Option[Any]): Unit = {
    log.info(s"Child ${self.path.name} pre-restart")
    super.preRestart(reason, message)
  }

  abstract override def postRestart(reason: Throwable): Unit = {
    log.info(s"Child ${self.path.name} post-restart")
    super.postRestart(reason)
  }

  abstract override def postStop(): Unit = {
    log.info(s"Child ${self.path.name} post-restart")
    super.postStop()
  }
}

class ChildActor extends Actor with ActorLogging with ActorStandardOutput {

  override def receive: Receive = {
    case "IllegalArgumentException" =>
      throw new IllegalArgumentException(s"oh no ${self.path.name}")
  }
}
