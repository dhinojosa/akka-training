package com.akkatraining.scala.dispatchers

import akka.actor.Actor

/**
  * Blocking Actor that sleeps 5 seconds
  * per reception of message
  * Source: https://doc.akka.io/docs/akka/current/dispatchers.html
  */
class BlockingActor extends Actor {
  def receive: Receive = {
    case i: Int ⇒
      Thread.sleep(5000) //block for 5 seconds, representing blocking I/O, etc
      println(s"Blocking operation finished: $i")
  }
}