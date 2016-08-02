name := """chatfive"""

version := "0.1"

scalaVersion := "2.11.8"
lazy val akkaVersion = "2.4.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test")

mainClass in Compile := Some("com.gmail.asserchiu.chatfive.ApplicationMain")
