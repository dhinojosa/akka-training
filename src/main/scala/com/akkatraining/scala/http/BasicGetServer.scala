package com.akkatraining.scala.http

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.pattern._
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object BasicGetServer {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = system.dispatcher

    implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

    system.actorOf(Props[EmployeeActor], "EmployeeFinder")

    val route =
      path("employee" / IntNumber) { number =>
        get {
          val employeeFinder = system.actorSelection("/user/EmployeeFinder")
          val future = employeeFinder ? number
          val route1 = onSuccess(future) {
            case Some(Employee(fn, ln)) =>
              System.out.println("Received message")
              complete(s"$fn $ln")
            case None => complete(StatusCodes.NotFound)
          }
          println("this should appear quickly!")
          route1
        }
      }

    val bindingFuture = Http()
      .bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
