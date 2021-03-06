package com.akkatraining.scala.mailboxes

import akka.actor.Actor
import akka.dispatch.{BoundedMessageQueueSemantics, RequiresMessageQueue}
import akka.event.Logging



class MailboxActor extends Actor {
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case o => log.info("Received: {}", o.toString)
  }
}
