package com.akkatraining.scala.router

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

class BossActor extends Actor with ActorLogging{
  var router = {
    val vector = Vector.fill(5) {
      val worker = context.actorOf(Props[WorkerActor])
      context watch worker
      ActorRefRoutee(worker)
    }
    Router(RoundRobinRoutingLogic(), vector)
  }

  override def receive: Receive = {
    case Terminated(routeeRef) =>
      router = router.removeRoutee(routeeRef)
      val worker = context.actorOf(Props[WorkerActor])
      context watch worker
      router = router.addRoutee(ActorRefRoutee(worker))
    case o =>
      router.route(o, sender)
  }
}
