package com.akkatraining.scala.remoting

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

object RemoteClientRunner extends App {
    val rootConfig = ConfigFactory.load()
    val actorSystem = ActorSystem("remote-client-runner",
        rootConfig.getConfig("remote-client").withFallback(rootConfig))

    val actorARef = actorSystem.actorOf(Props[ActorA], "actorA")
    val actorBRef = actorSystem.actorOf(Props[ActorB], "actorB")

    actorARef.!("Hello", actorBRef)
    actorARef.tell("Hello", actorBRef)

    actorARef ! "Cool"


    actorBRef ! "Zoom"

    println("Press Enter to terminate")
    scala.io.StdIn.readLine()
    Await.ready(actorSystem.terminate(), 10 seconds)
}
