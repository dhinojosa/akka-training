package com.akkatraining.scala.ask

import akka.actor.Actor
import akka.actor.ActorLogging

class AskActor extends Actor with ActorLogging {
   def receive:Receive = {
     case Add(a,b) => 
       log.info("Adding $a and $b")
       sender() ! a+b
     case Subtract(a,b) => 
       log.info("Subtract $a and $b")
       sender() ! a-b
     case x => unhandled(x)
   }
}