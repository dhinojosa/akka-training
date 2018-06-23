package com.akkatraining.scala.mailboxes

import akka.actor.Actor
import akka.dispatch.{BoundedMessageQueueSemantics, RequiresMessageQueue}
import akka.dispatch
import akka.event.Logging


/**
  * RequiresMessageQueue trait to signal that an Actor requires a certain type of message queue semantics.
  *
  * The mailbox type will be looked up by mapping the type T via akka.actor.mailbox.requirements in the config,
  * to a mailbox configuration. If no mailbox is assigned on Props or in deployment config then this one will be used.
  *
  * The queue type of the created mailbox will be checked against the type T and actor creation will fail if it doesn't
  * fulfill the requirements.
  */
class MailboxWithRequirementActor extends Actor
  with RequiresMessageQueue[BoundedMessageQueueSemantics] {

  val log = Logging(context.system, this)
  override def receive: Receive = {
    case s:String => log.info("Received: {}", s)
    case o => unhandled(o)
  }
}
