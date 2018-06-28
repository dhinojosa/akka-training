package com.akkatraining.scala.cluster

import akka.actor.{ActorSystem, Props}
import akka.cluster.client.ClusterClientReceptionist
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory

object SimpleClusterRunner extends App {

  val rootConfig = ConfigFactory.load()
  val clusterConfig = ConfigFactory.load()
     .getConfig("clustering-akka")
     .withFallback(rootConfig)

  val seqPorts = Seq("2551", "2552", 0, 0)

  seqPorts.foreach{p =>
    val config = ConfigFactory
      .parseString(s"akka.remote.netty.tcp.port=$p")
      .withFallback(clusterConfig)

    val actorSystem = ActorSystem("My-Cluster", config)
    val actorRef = actorSystem.actorOf(Props[SimpleClusterListener], name="clusterListener")
    val router = actorSystem.actorOf(FromConfig.props(Props[SimpleActor]), name="simpleRouter")
    ClusterClientReceptionist(actorSystem).registerService(router)
  }
}
