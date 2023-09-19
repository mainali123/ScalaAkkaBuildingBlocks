package AkkaEssentials.AkkaActors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}


object ActorCapabilities extends App {

  val actorSystem = ActorSystem("akkaCapabilities")

  class SimpleActor extends Actor {
    override def receive: Receive = {
      case "Hi" => context.sender() ! "Hello there" // replying to message
      case message: String => println(s"[simple actor]: ${self.path} I have received $message.")
      case number: Int => println(s"[simple actor]: I have received a $number number.")
      case SpecialMessage(contents) => println(s"[simple actor]: I have received special message which is $contents")
      case SendMessageToYourself(contents) => self ! contents // self == context.self
      case SayHiTo(ref) => ref ! "Hi" // alice is being passed as the sender
      case WirelessPhoneMessage(contents, ref) => ref forward (contents + "s") // I keep the original sender of the WPM
    }
  }

  val simpleActor = actorSystem.actorOf(Props(SimpleActor()), "simpleActor")
  simpleActor ! "Hey! How are you doing?"

  // 1- messages can be of any type
  // Conditions:
  // a) Messages must be IMMUTABLE
  // b) Messages must be SERIALIZABLE
  // In practice use case classes and case objects
  simpleActor ! 55

  case class SpecialMessage(context: String)

  simpleActor ! SpecialMessage("Some especial Contents")


  // 2- actors have information about their contents and about themselves (`context` keyword)
  // context.self => `this` keyword in OOP

  case class SendMessageToYourself(context: String)

  simpleActor ! SendMessageToYourself("I am an actor and I am sending message to myself.")

  // 3- actors can REPLY to messages
  val alice = actorSystem.actorOf(Props(SimpleActor()), "Alice")
  val bob = actorSystem.actorOf(Props(SimpleActor()), "Bob")

  case class SayHiTo(ref: ActorRef)

  alice ! SayHiTo(bob)

  // 4 - dead letters
  alice ! "Hi!"

  // 5 - forwarding messages
  // forwarding = sending a message with the ORIGINAL sender
  case class WirelessPhoneMessage(contents: String, forward: ActorRef)

  alice ! WirelessPhoneMessage("Hi", bob) // no sender


  /**
   * Exercises
   *
   * 1. a Counter actor
   *  - Increment
   *  - Decrement
   *    -Print
   *
   * 2. a bank account as an actor
   * Receives:
   *  - Deposit an amount
   *  - Withdraw an amount
   *  - Statement
   *    Replies:
   *    -Success or failure of each operation
   *
   * Interact with some other kind of actor
   * */

  // Exercise 1
  // Domain of the counter
  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }
  class Counter extends Actor {
    import Counter._
    var count = 0
    override def receive: Receive = {
      case Increment => count += 1
      case Decrement => count -= 1
      case Print => println(s"The current count is $count.")
    }
  }

  val counterActor = actorSystem.actorOf(Props(Counter()), "counterActor")
  counterActor ! Counter.Increment
  counterActor ! Counter.Increment
  counterActor ! Counter.Increment
  counterActor ! Counter.Increment
  counterActor ! Counter.Increment
  counterActor ! Counter.Decrement

  counterActor ! Counter.Print


  // Exercise 2
  class Message extends Actor {
    override def receive: Receive = {
      case "Success" => println("Success")
      case "Failure" => println("Failure")
      case int: Int => println(s"The current balance is $int.")
    }
  }
  class BankAccount extends Actor {
    var bankBalance = 0
    override def receive: Receive = {
      case Deposit(amt) =>
        bankBalance += amt
        message ! "Success"
      case Withdraw(amt) =>
        if (bankBalance < amt) then message ! "Failure" else
          bankBalance -= amt
          message ! "Success"
      case "Statement" => message ! bankBalance
    }
  }

  case class Deposit(amount: Int)
  case class Withdraw(amount: Int)


  val bankAccountActor = actorSystem.actorOf(Props(BankAccount()), "bankAccountActor")
  val message = actorSystem.actorOf((Props(Message())), "Message")

  bankAccountActor ! Deposit(10000)
  bankAccountActor ! Withdraw(20000)
  bankAccountActor ! Withdraw(5000)
  bankAccountActor ! "Statement"

  actorSystem.terminate()
}
