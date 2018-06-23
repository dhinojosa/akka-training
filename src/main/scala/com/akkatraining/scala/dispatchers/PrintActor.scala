package com.akkatraining.scala.dispatchers

import akka.actor.Actor

class PrintActor extends Actor {
  def receive:Receive = {
    case i: Int ⇒
      println(s"PrintActor: $i")
  }
}