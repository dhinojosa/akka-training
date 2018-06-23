package com.akkatraining.scala.mailboxes

import akka.actor.{ActorSystem, PoisonPill}
import akka.dispatch.{PriorityGenerator, UnboundedStablePriorityMailbox}
import com.typesafe.config.Config

/**
  * https://doc.akka.io/docs/akka/current/mailboxes.html#introduction
  * @param settings any actor system setting that can be queried
  * @param config any config settings that can be queried
  */
class MyPriorityMailbox(settings: ActorSystem.Settings, config: Config)
  extends UnboundedStablePriorityMailbox(
    // Create a new PriorityGenerator, lower prio means more important
    PriorityGenerator {
      // 'highpriority messages should be treated first if possible
      case 'highpriority ⇒ 0

      // 'lowpriority messages should be treated last if possible
      case 'lowpriority ⇒ 2

      // PoisonPill when no other left
      case PoisonPill ⇒ 3

      // We default to 1, which is in between high and low
      case otherwise ⇒ 1
    })
