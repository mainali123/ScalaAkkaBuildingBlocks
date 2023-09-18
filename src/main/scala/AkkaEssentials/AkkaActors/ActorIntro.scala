package AkkaEssentials.AkkaActors

import akka.actor.{Actor, ActorSystem, Props}

object ActorIntro extends App {
    // Part1- actor system
    val actorSystem = ActorSystem("firstActorSystem")
    println(actorSystem.name)

    // part2 - create actors
    // Word count actor
    class WordCountActor extends Actor {
      // Internal Data
      var totalWords = 0
      // behaviour
      override def receive: Receive = {
        case message: String =>
          println(s"[word counter] I have received: $message")
          totalWords += (message.split(" ")).length
        case msg => println(s"[word counter] I cannot understand ${msg.toString}")
      }
    }

    // part3 - instantiate our actor
    val wordCounter = actorSystem.actorOf(Props(WordCountActor()), "wordCounter")
    val anotherWordCounter = actorSystem.actorOf(Props(WordCountActor()), "anotherWordCounter")

    // part-4 communicate!
    wordCounter ! "I am learning AKKA and it's going to be fun."  //"tell"
    anotherWordCounter ! "I am going to learn AKKA and use it to build applications."
    // asynchronous!


  // Actor with parameters
  object Person {
    def props(name: String) = Props(Person(name))
  }
  class Person(name: String) extends Actor {
    override def receive: Receive = {
      case "hi" => println(s"Hi, my name is $name.")
      case _ =>
    }
  }

//  val person = actorSystem.actorOf(Props(Person("Bob")))  // Discouraged method
  val person = actorSystem.actorOf(Person.props("Bob"))
  person ! "hi"
}
