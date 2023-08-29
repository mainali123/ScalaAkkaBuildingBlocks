package Chapter1

import akka.actor._

object BehaviourAndState extends App {
  val actorSystem = ActorSystem("HelloAkka")
  // Creating an actor inside the actor system
  val actor = actorSystem.actorOf(Props(SummingActor()), "SummoningActor")  // Creating an child actor with name "SummoningActor"
  // print actor path
  println(actor.path)


  while true do
    Thread.sleep(3000)
//    actor.tell("Hello") is equivalent to actor ! "Hello"
//    actor.ask(???) is equivalent to actor ? ???
    actor ! "Hello"

}

class SummingActor extends Actor {
  // state inside the actor
  var sum = 0

  // behaviour which is applied on the state
  override def receive: Receive = {
    // receives message an integer
    case x: Int => sum = sum + x
    println(s"my state as sum is $sum")
    // receives default message
    case _ => println("I don't know what are you talking about")
  }
}

// Actor that takes argument in the constructor
class SummingActorWithConstructor(initialSum: Int) extends Actor {
  // state inside the actor
  var sum = 0

  // behaviour which is applied on the state
  override def receive: Receive = {
    // Receives message an integer
    case x: Int => sum = initialSum + sum + x
      println(s"My state as sum is $sum")
      // receives default message
    case _ => println("I don't know what are you talking about")
  }
}

