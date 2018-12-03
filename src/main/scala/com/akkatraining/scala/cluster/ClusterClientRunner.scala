package com.akkatraining.scala.cluster

import akka.actor.{ActorPath, ActorRef, ActorSystem}
import akka.cluster.client.{ClusterClient, ClusterClientSettings}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.io.StdIn
import scala.concurrent.duration._

object ClusterClientRunner extends App {

  val rootConfig = ConfigFactory.load()
  val actorSystem = ActorSystem("Non-Member-Actor-System",
    rootConfig.getConfig("client")
    .withFallback(rootConfig))

  val initialContacts = Set(
    ActorPath.fromString("akka.tcp://My-Cluster@127.0.0.1:2552/system/receptionist"),
    ActorPath.fromString("akka.tcp://My-Cluster@127.0.0.1:2551/system/receptionist"))

  val c: ActorRef = actorSystem.actorOf(
    ClusterClient.props(ClusterClientSettings(actorSystem)
      .withInitialContacts(initialContacts)))

 for (i <- 1 to 1000) {
   c ! ClusterClient.Send("/user/simpleRouter", s"Hello $i!", false)
 }
  println("Press ENTER to continue")
  StdIn.readLine()

  Await.ready(actorSystem.terminate(), 10 seconds)
}