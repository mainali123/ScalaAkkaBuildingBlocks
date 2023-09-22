package AkkaCookbook.Chapter1_Diving_Into_Actor

import akka.actor.{Actor, ActorSystem, Props}

object BehaviourAndState extends App {

  class SummingActor extends Actor {
    // state inside actor
    var sum = 0

    // behaviour which is applied on the state
    override def receive: Receive = {
      // receives message an integer
      case x: Int => sum += x
        println(s"My state as sum is $sum.")
      case _ => println("I don't know what are you talking about")
    }
  }

  val actorSystem = ActorSystem("HelloAkka")
  val actor = actorSystem.actorOf(Props(SummingActor()))
  println(actor.path) // Print actor path

  actor ! 1

  actor ! "Hey you bro"
}
