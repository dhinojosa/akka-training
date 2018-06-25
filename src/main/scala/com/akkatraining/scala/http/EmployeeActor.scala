package com.akkatraining.scala.http

import akka.actor.Actor
import akka.event.Logging

class EmployeeActor extends Actor {
  val log = Logging(context.system, this)

  val contents =
    scala.collection.mutable.ArrayBuffer(
      Employee("Ozzy", "Osbourne"),
      Employee("Frank", "Sinatra"),
      Employee("Neil", "Diamond"),
      Employee("Joni", "Mitchell"))

  override def receive: PartialFunction[Any, Unit] = {
    case x: Int =>
      //GET
      log.debug("Recieved message, and looking up {} in a datastore", x)
      Thread.sleep(1000)
      if (x > contents.size - 1) sender ! None
      else sender ! Some(contents(x))
    case x: Employee =>
      //POST
      contents += x
    case (id:Int, e:Employee) =>
      //PUT
      contents(id) = e
    case u =>
      log.debug("Didn't understand message, sending message to dead letters")
      unhandled(u)
  }
}
