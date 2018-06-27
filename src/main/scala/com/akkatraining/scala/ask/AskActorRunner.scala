package com.akkatraining.scala.ask

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._

object AskActorRunner {
  def main(args:Array[String]):Unit = {
    val actorSystem = ActorSystem("actorSystem")
    import actorSystem.dispatcher //Making an implicit thread pool from actor system
    val props = Props[AskActor]
    val actorRef = actorSystem.actorOf(props, "askActor")
    implicit val timeout = Timeout(2 seconds)
    
    val future = actorRef ? Add(3, 4)
    future.mapTo[Int].onComplete {
      case Success(i) => println(s"Answer is: $i")
      case Failure(t) => println(s"Failure is: ${t.getMessage}")
    }

    println("Press Enter to terminate")
    scala.io.StdIn.readLine()
    Await.ready(actorSystem.terminate(), 10 seconds)
  }
}