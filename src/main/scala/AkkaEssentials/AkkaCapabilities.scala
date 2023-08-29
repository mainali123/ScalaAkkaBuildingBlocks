package AkkaEssentials

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object AkkaCapabilities extends App {
  class SimpleActor extends Actor {
    override def receive: Receive = {
      case "hi" => context.sender() ! "Hello There" // Replying to message
      case message: String => println(s"[${self}] I have received: $message.")
      case number: Int => println(s"[Simple Actor] I have received a number: $number.")
      case specialMessage: SpecialMessage => println(s"[Simple Actor] I have received something special: $specialMessage.")
      case SendMessageToYourself(context) => self ! context
      case SayHiTo(ref) => ref ! "hi" // Receiving message
      case WirelessPhoneMessage(context, ref) => ref forward(context + "s") // Keep the original sender of the message
    }
  }

  val system = ActorSystem("ActorCapabilities")
  val simpleActor = system.actorOf(Props(SimpleActor()), "simpleActor")
  simpleActor ! "Hello Actor"

  // 1- Messages can be of any type
  // a) Messages must be immutable
  // b) Messages must be SERIALIZABLE

  // In practice use case classes and case objects
  simpleActor ! 42

  case class SpecialMessage(contents: String)
  simpleActor ! SpecialMessage("Some Special Message")

  // 2- Actors have information about their context and about themselves
  // context.self === `this` keyword in OOP

  case class SendMessageToYourself(context: String)
  simpleActor ! SendMessageToYourself("I am an actor and I am sending message to myself")

  // 3- actors can REPLY to messages

  val alice = system.actorOf(Props(SimpleActor()), "alice")
  val bob = system.actorOf(Props(SimpleActor()), "bob")

  case class SayHiTo(ref: ActorRef)
  alice ! SayHiTo(bob)

  // 4- Dead letters
  alice ! "hi"

  // 5- Forwarding messages

  case class WirelessPhoneMessage(context: String, ref: ActorRef)
  alice ! WirelessPhoneMessage("Hi", bob) // NO sender
}
