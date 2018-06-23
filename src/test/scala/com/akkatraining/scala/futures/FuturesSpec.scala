package com.akkatraining.scala.futures

import java.time.LocalDateTime
import java.util.concurrent.{Executors, TimeUnit}

import akka.actor.ActorSystem
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

class FuturesSpec extends FunSuite with Matchers {
  test("Case 1: Create a basic Future in Scala") {
    pending
  }

  test("Case 2: Creating a thread pool from Java Executor") {
    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContextExecutor =
      ExecutionContext.fromExecutor(executorService)

    val future = Future {
      Thread.sleep(1000)
      10 + 40
    }

    future.foreach(println)

    val eventualInt = Await.ready(future, Duration.apply(1, TimeUnit.SECONDS))
  }

  test(
    """Case 3: Using Await.ready to passthrough the Future
      |  so as to analyze the answer thereafter.  Await.ready will
      |  block.""".stripMargin) {

    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContextExecutor =
      ExecutionContext.fromExecutor(executorService)

    val future = Future {
      Thread.sleep(1000)
      10 + 40
    }

    Await.ready(future, Duration.apply(2, TimeUnit.SECONDS))
      .map(x => x + 10)
      .foreach(println)
  }

  test("Case 4: Await.result a future directy") {
    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContextExecutor =
      ExecutionContext.fromExecutor(executorService)

    val future = Future {
      Thread.sleep(1000)
      10 + 40
    }

    val result: Int = Await.result(future, Duration.apply(2, TimeUnit.SECONDS))

    result should be(50)
  }

  test(
    """Case 5: Future successful just directly says it
      |  is a success with a value""".stripMargin) {

    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContext =
      ExecutionContext.fromExecutor(executorService)

    val successfulFuture = Future.successful("Hello")
    successfulFuture.foreach(x => x should be("Hello"))

    Await.ready(successfulFuture, Duration(1, TimeUnit.SECONDS))
  }

  test("""Case 6: Future failed just directly is failure with a
      |  particular Throwable. Recover will recover the failure and return a
      |  different value of the same type or supertype""".stripMargin) {

    case class Employee(name: String, ssn: String)
    class Manager(name: String, ssn: String, val employees: List[Employee])
      extends Employee(name, ssn)

    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContext =
      ExecutionContext.fromExecutor(executorService)

    val eventualManager = Future.failed[Manager](
      new Throwable("Unable to complete"))
    val recoveredEmployee = eventualManager
      .recover { case e: Throwable => Employee("Sam Yalew", "123-99-1099") }

    Await.result(recoveredEmployee, Duration(1, TimeUnit.SECONDS)) should
      be(Employee("Sam Yalew", "123-99-1099"))
  }

  test("""Case 7: Future failed just directly is failure with a
      |  particular Throwable. Recover will recover the failure and return a
      |  different value of the same type or supertype, and in
      |  the following case if nothing is wrong then the future
      |  will continue as if nothing happened.""".stripMargin) {

    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContext =
      ExecutionContext.fromExecutor(executorService)

    val eventualInt = Future.successful(10).recover { case e: Throwable => -1 }

    Await.result(eventualInt, Duration(1, TimeUnit.SECONDS)) should be(10)
  }

  test("""Case 8: Future failed just directly is failure with a
      |  particular Throwable. Recover will recover the failure and return a
      |  different value of the same type or supertype, and in
      |  the following case if nothing is wrong then the future
      |  will continue as if nothing happened.""".stripMargin) {

    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContext =
      ExecutionContext.fromExecutor(executorService)

    val eventualRecoveryInt: Future[Int] = Future.successful(10)
      .recoverWith { case e: Throwable => Future.successful(-1) }

    Await.result(eventualRecoveryInt, Duration(1, TimeUnit.SECONDS)) should be(10)
  }

  test("Case 9: Proper parse with Try[T], in this case the T is an Int") {
    def parseNumber(s: String): Try[Int] = Try {
      Integer.parseInt(s)
    }

    val triedInt1 = parseNumber("x").map(x => x + 30)
    val triedInt2 = parseNumber("50").map(x => x + 30)

    triedInt1.getOrElse(-1) should be(-1)
    triedInt2.getOrElse(-1) should be(80)
  }

  test("Case 10: Using onComplete with a Scala Future and Try") {
    pending

    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContextExecutor =
      ExecutionContext.fromExecutor(executorService)

    val eventualString: Future[String] = Future {
      val num = scala.util.Random.nextInt(2)
      if (num == 1) "Awesome!"
      else throw new RuntimeException(s"Invalid number $num")
    }

    /* Use onComplete here on the future given what you know about the
       signature */
  }

  test("""Case 11: Creating a thread pool from ActorSystem and its dispatcher.
      |  Each actor system comes with a dispatcher,
      |  which is a thread pool internal to the system. All that is required
      |  is an import of the dispatcher and it includes an implicit thread
      |  pool""".stripMargin) {

    val actorSystem = ActorSystem("TestSystem")
    import actorSystem.dispatcher

    val future = Future {
      Thread.sleep(1000)
      10 + 40
    }

    Await.ready(future, Duration.apply(2, TimeUnit.SECONDS))
      .map{i => i should be (503); i}
      .flatMap(_ => actorSystem.terminate())
  }

  test("""Case 12: Using an implicit to concisely display the time using
      |  implicit trickery""".stripMargin) {

    pending

    val actorSystem = ActorSystem("TestSystem")
    import actorSystem.dispatcher


    val future = Future {
      Thread.sleep(1000)
      10 + 40
    }

    Await.ready(future, Duration.apply(2, TimeUnit.SECONDS)).foreach(println)
  }

  test("""Case 13: A Promise will be a manual answer to a Future.
      |  Do as follows:
      |  1. Create the Promise[T].
      |  2. Take the Future[T] from it.
      |  3. Process the Future[T] normally.
      |  4. Answer the Promise whenever you feel like it!""".stripMargin) {

    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContext = ExecutionContext
      .fromExecutor(executorService)

    val promise: Promise[Int] = Promise.apply()

    //This is what I will hand out to whoever
    val future = promise.future
    future.foreach(println)

    Thread.sleep(4000)
    promise.success(42)

    Await.ready(future, Duration.apply(10, TimeUnit.SECONDS))
  }

  test("""Case 14: Futures are monadic. But first, let's review a monad,
      |  first with Option and the combination of flatMap and map, and then as a
      |  for comprehension.""".stripMargin) {

    val m: Option[Int] = Some(12)
    val n: Option[Int] = Some(20)

    val o: Option[Int] = m.flatMap(x => n.map(y => x + y))
    o should contain(32)

    val p: Option[Int] = for (x <- m; y <- n) yield x + y
    p should contain(32)
  }

  test("""Case 15: Here is a monadic list. Again, first as a flatMap and map
      |  combination and then as a for comprehension.""".stripMargin) {

    val q = List(1, 2)
    val r = List(3, 4)

    val s = q.flatMap(x => r.map(y => x + y))
    val t = for (x <- q; y <- r) yield x + y

    s should contain allElementsOf List(4, 4, 5, 6)
    t should contain allElementsOf List(4, 4, 5, 6)
  }

  test("""Case 16: Since Futures are monadic we can then undergo
      |  the same principles in both flatMap and map
      |  combinations and as a for comprehension""".stripMargin) {

    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContext =
      ExecutionContext.fromExecutor(executorService)

    val f: Future[Int] = Future.successful(300)
    val g: Future[Int] = Future.successful(100)

    val result1: Future[Int] = f.flatMap(x => g.map(y => x + y))
    val result2: Future[Int] = for (x <- f; y <- g) yield x + y

    Await.result(result2, Duration.apply(1, TimeUnit.SECONDS)) should be(400)
  }

  test("""Case 17: Converting collection of futures into a
      |  future of collections""".stripMargin) {
    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext: ExecutionContext = ExecutionContext
      .fromExecutor(executorService)

    val futures: Seq[Future[LocalDateTime]] = List.fill(10) {
      Future {
        LocalDateTime.now()
      }
    }

    val eventualDates: Future[Seq[LocalDateTime]] = Future.sequence(futures)
    val eventualInt = eventualDates
      .map(dates =>
        dates.map(d =>
          d.getSecond).sum)

    Await.ready(eventualInt,Duration.apply(1, TimeUnit.SECONDS)).foreach(println)
  }

  test("""Case 18: Converting a collection of items into a
      |  Future with a function.  This is useful to use the
      |  elements of a list to be involved in a Future
      |  calculation""".stripMargin) {

    import ExecutionContext.Implicits.global

    val list = List(1, 2, 3)

    val eventualInts: Future[List[Int]] =
      Future.traverse(list)(i => Future.successful(i * 3))

    Await.ready(eventualInts, Duration.apply(1, TimeUnit.SECONDS)).foreach(xsf =>
      xsf should contain inOrder(3, 6, 9))
  }
}

