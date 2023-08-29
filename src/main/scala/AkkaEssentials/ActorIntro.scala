package AkkaEssentials

import akka.actor.{Actor, ActorSystem, Props}

object ActorIntro extends App{
  // Part-1 Actor System
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  // Part-2 Create actors
  // word count actor
  class WordCountActor extends Actor {
    // Internal Data
    var totalWords = 0

    // Behaviour
    override def receive: PartialFunction[Any, Unit] = {
      case message: String =>
        println(s"I have received $message")
        totalWords + message.split(" ").length
      case _ => println("I cannot understand what you are telling me")
    }
  }

  // Part-3 Instantiate Actor
  val actor = actorSystem.actorOf(Props(WordCountActor()), "wordCounter")
  val actor1 = actorSystem.actorOf(Props(WordCountActor()), "wordCounter1")

  actor ! "Hello my name is Diwash Mainali" // tell
  actor1 ! "Hello my name is Utsav Mainali"
  // asynchronous

  // Instantiate actor with constructor parameters
  class Person(name: String) extends Actor {
    // Receive is a PartialFunction from Any to Unit i.e [Any,Unit]
    override def receive: Receive = {
      case "hi" => println(s"Hi my name is $name")
      case _ =>
    }
  }

  val constructorActor = actorSystem.actorOf(Props(Person("Diwash"))) // This method is discouraged
  constructorActor ! "hi"

  // Encouraged method & best practice
  object Person {
    def props(name: String) = Props(Person(name))
  }
  val constructorActor1 = actorSystem.actorOf(Person.props("Mainali"))
  constructorActor1 ! "hi"
}
