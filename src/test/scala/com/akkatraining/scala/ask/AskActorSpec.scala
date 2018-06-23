package com.akkatraining.scala.ask

import org.scalatest.{FunSuite, Matchers}

class AskActorSpec extends FunSuite with Matchers {
  test("""Lab 1: Create an actor in the com.akka.training.scala.ask
      |  package call AskActor that will take in a messages that are
      |  cases classes called Add and Subtract that take two Int
      |  parameters. Use the ask pattern to return a Future of the
      |  computation using either Add or Subtract case classes""".stripMargin) {
    pending
  }

  test("""Lab 2: Call AskActor with Add and Subtract using arbitrary numbers
      |  with the ask pattern and use the Future composition we discussed in a
      |  previous section to combine the results of Add and Subtract and
      |  multiply them""".stripMargin) {
    pending
  }

  test("""Lab 3: Do the above using a for-comprehension after
      |  obtaining a future""".stripMargin) {
    pending
  }
}
