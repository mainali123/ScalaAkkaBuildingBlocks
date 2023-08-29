package Chapter1

import akka.actor.ActorSystem
object HelloAkkaActorSystem extends App {
  val actorSystem = ActorSystem("HelloAkka")  // Creating an object of actor system with name HelloAkka
  println(actorSystem)
}
