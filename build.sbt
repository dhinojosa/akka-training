name := "akka_training"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.6"

val akkaVersion = "2.5.12"

scalacOptions ++= Seq("-deprecation","-feature","-language:postfixOps")

javacOptions ++= Seq("-Xlint:deprecation","-Xlint:unchecked")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion
     withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
     withSources() withJavadoc(),
  "org.slf4j" % "slf4j-simple" % "1.7.7"
     withSources() withJavadoc(),
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
     withSources() withJavadoc(),
  "com.novocode" % "junit-interface" % "0.10" % "test",
  "junit" % "junit" % "4.12" % "test"
)
