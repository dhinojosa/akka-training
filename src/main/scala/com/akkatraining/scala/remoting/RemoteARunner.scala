package com.akkatraining.scala.remoting

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

object RemoteARunner extends App {
    val rootConfig = ConfigFactory.load()
    val actorSystem = ActorSystem("remote-a-actor-system",
        rootConfig.getConfig("remote-a").withFallback(rootConfig))

    println("Press Enter to terminate")
    scala.io.StdIn.readLine()
    Await.ready(actorSystem.terminate(), 10 seconds)
}
