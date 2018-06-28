package com.akkatraining.scala.remoting

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

object RemoteBRunner extends App {
    val rootConfig = ConfigFactory.load()
    val actorSystem = ActorSystem("remote-b-actor-system",
        rootConfig.getConfig("remote-b").withFallback(rootConfig))

    println("Press Enter to terminate")
    scala.io.StdIn.readLine()
    Await.ready(actorSystem.terminate(), 10 seconds)
}
