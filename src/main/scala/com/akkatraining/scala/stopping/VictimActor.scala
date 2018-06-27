package com.akkatraining.scala.stopping

import akka.actor.{Actor, ActorLogging, PoisonPill}

class VictimActor extends Actor with ActorLogging{

  override def preStart(): Unit = {
    log.info("VictimActor is about to be started")
    super.preStart()
  }

  override def postStop(): Unit = {
    log.info("VictimActor was stopped")
    super.postStop()
  }

  override def preRestart(reason: Throwable,
                          message: Option[Any]): Unit = {
    log.info("VictimActor is about to be restarted")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info("VictimActor was restarted")
    super.postRestart(reason)
  }

  override def receive: Receive = {
    case GracefulStopMessage =>
      //clean up any state
      //shutdown children gracefully
      log.info("I am taking my time, stopping, cleaning resources")
      //1/0
      //Thread.sleep(5000)
      self ! PoisonPill.getInstance

    case s:String => log.info("Received String: {}", s)
  }
}
