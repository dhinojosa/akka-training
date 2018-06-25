name := "akka-training"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.6"

//fork := true

val akkaVersion = "2.5.12"

scalacOptions ++= Seq("-deprecation", "-feature", "-language:postfixOps")

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked")

EclipseKeys.withSource := true

EclipseKeys.withJavadoc := true

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion
    withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
    withSources() withJavadoc(),
  "org.slf4j" % "slf4j-simple" % "1.7.7"
    withSources() withJavadoc(),
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
    withSources() withJavadoc(),
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "junit" % "junit" % "4.12" % Test,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion
    withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion
    withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion
    withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.3",
  "com.typesafe.akka" %% "akka-http-xml" % "10.1.3"
)
