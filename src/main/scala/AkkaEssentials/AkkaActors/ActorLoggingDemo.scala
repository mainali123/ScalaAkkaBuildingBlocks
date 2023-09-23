package AkkaEssentials.AkkaActors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.Logging

object ActorLoggingDemo extends App {

  //#1 - explicit logging
  class SimpleActorWithExplicitLogger extends Actor {
    val logger = Logging(context.system, this)
    override def receive: Receive = {
      /*
      1 - Debug
      2 - Info
      3 - Warning/Warn
      4 - Error
      */
      case message => logger.info(message.toString)// LOG it
    }
  }
  val system = ActorSystem()
  val actor = system.actorOf(Props(SimpleActorWithExplicitLogger()))

  actor ! "Logging simple message"

  // #2 - ActorLogging
  class ActorWithLogging extends Actor with ActorLogging {
    override def receive: Receive = {
      case (a, b) => log.info(("Two things: {} and {}"), a, b)  // The things: 2 and 3
      case message => log.info(message.toString)
    }
  }

  val simplerActor = system.actorOf(Props(ActorWithLogging()))
  simplerActor ! "Logging a simple message by extending a trait"
  simplerActor ! (42, 61)
}
