package com.akkatraining.scala.http
import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{FormData, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import akka.pattern._
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.io.StdIn

object BasicGetPostPutServer {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = system.dispatcher
    implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

    implicit val requestFormDataToEmployeeUnMarshaller: FromRequestUnmarshaller[Employee] =
      implicitly[FromRequestUnmarshaller[FormData]]
        .map(formData => {
          val fields: Query = formData.fields
          for (fn <- fields.get("firstName");
               ln <- fields.get("lastName")
          ) yield Employee(fn, ln)
        }).map(_.get)

    val employeeActor = system.actorOf(Props[EmployeeActor], "EmployeeFinder")

    val route: Route =
      path("employee" / IntNumber) { number =>
        get {
          val future = employeeActor ? number
          onSuccess(future) {
            case Some(Employee(fn, ln)) => complete(s"$fn $ln")
            case None => complete(StatusCodes.NotFound)
          }
        } ~ put {
          entity(as[Employee]) { data =>
            employeeActor ! (number, data)
            complete("Employee Updated")
          }
        }
      } ~
        path("employee") {
          post {
            entity(as[Employee]) { data =>
              employeeActor ! data
              complete("Employee Received")
            }
          }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8082)

    println(s"Server online at http://localhost:8082/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
