
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaAkkaBuildingBlocks"
  )
val AkkaVersion = "2.8.5"

resolvers += "Akka library repository".at("https://repo.akka.io/maven")
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion