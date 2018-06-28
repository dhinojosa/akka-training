package com.akkatraining.scala.router

import java.time.LocalDateTime

import akka.actor.{ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration._

object RouterRunner {
  def main(args:Array[String]):Unit = {
    val actorSystem = ActorSystem("actorSystem") //Making an implicit thread pool from actor system
    import actorSystem.dispatcher

    val bossActor = actorSystem.actorOf(Props[BossActor], "bossActor")

    val cancellable = actorSystem.scheduler.schedule(0 milliseconds,
      50 milliseconds, () => bossActor ! LocalDateTime.now)

    println("Press Enter to terminate")
    scala.io.StdIn.readLine()
    Await.ready(actorSystem.terminate(), 10 seconds)
  }
}