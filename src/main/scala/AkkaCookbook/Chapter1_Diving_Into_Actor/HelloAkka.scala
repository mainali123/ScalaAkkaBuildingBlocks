package AkkaCookbook.Chapter1_Diving_Into_Actor

import akka.actor.ActorSystem

object HelloAkkaSystems extends App {
  val actorSystem = ActorSystem("HelloAkka")
  println(actorSystem)
}
