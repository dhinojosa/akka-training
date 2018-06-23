package com.akkatraining.scala.config

import com.typesafe.config.{ConfigFactory, ConfigList}
import org.scalatest.{FunSuite, Matchers}

class ConfigSpec extends FunSuite with Matchers {
  test("Case 1: parse of configuration string with nested sections") {
    val str = """a{b = 4}"""
    val config = ConfigFactory.parseString(str)
    config.getInt("a.b") should be (4)
  }

  test("Case 2: parse of configuration string in a dot format") {
    val str = """a.b = 50"""
    val config = ConfigFactory.parseString(str)
    config.getInt("a.b") should be (50)
  }

  test("Case 3: Load a configuration from application.conf") {
    val config = ConfigFactory.load("application")
    val loggingFilter = config.getString("akka.logging-filter")
    loggingFilter should be ("akka.event.slf4j.Slf4jLoggingFilter")
  }

  test("Case 4: Load a configuration without an explicit file, " +
    "by default it will read the application.conf file and in this case " +
    "it will read a list") {
    val config = ConfigFactory.load()
    val loggingFilter = config.getString("akka.logging-filter")
    loggingFilter should be ("akka.event.slf4j.Slf4jLoggingFilter")
  }

  test("Case 5: Use a system property to override") {
    System.setProperty("a.b.c", "100")
    ConfigFactory.invalidateCaches()
    val config = ConfigFactory.load("sample1")
    config.getDouble("a.b.c") should be (100.0)
  }

  test("""Case 6: You can also bring up a context within a file to use
         |  different settings""".stripMargin) {
    val config = ConfigFactory.load("sample2").getConfig("context-b")
    config.getDouble("a.b.c") should be (10.0)
  }

  test("""Case 7: You can also bring up a context within a file to use
      |  different settings, and with a fallback so
      |  get a setting""".stripMargin) {
    val originalConfig = ConfigFactory.load("sample3")
    val config = originalConfig.getConfig("context-b").withFallback(originalConfig)
    originalConfig.getBoolean("a.b.d") should be (true)
  }

  test("""Case 8: Here we are you using the Config library to read in JSON""") {
    val originalConfig = ConfigFactory.load("sample5")
    originalConfig.getString("d.d2") should be ("World")
  }

  test("""Case 9: We are using an include to bring in values from
      |  another config file""".stripMargin) {
    val originalConfig = ConfigFactory.load("sample6")
    originalConfig.getString("a.b.e.e2") should be ("Monde")
  }

  test("""Case 10: We are using a substitution to replace values""".stripMargin) {
    val originalConfig = ConfigFactory.load("sample7")
    originalConfig.getBoolean("a.b.e") should be (true)
  }

  test("""Case 11: We can read in a list from a configuration""".stripMargin) {
    val originalConfig = ConfigFactory.load("sample1")
    val list: ConfigList = originalConfig.getList("a.b.d")
    list.unwrapped should contain theSameElementsAs
      List("Red", "Orange", "Green", "Blue")
  }

  test("""Lab 1: Using the Config API find the netty hostname
      |  in the remote-akka configuration in the application.conf""".stripMargin) {
    pending
  }
}
