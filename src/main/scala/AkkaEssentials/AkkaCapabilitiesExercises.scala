package AkkaEssentials

import AkkaEssentials.AkkaCapabilitiesExercises.Person.LiveTheLive
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
 * Exercises
 *
 * 1. Create a counter actor
 *  - Increment
 *  - Decrement
 *  - Print
 *
 * 2. A Bank account as an actor
 *  receives
 *  - Deposit an amount
 *  - Withdraw an amount
 *  - Statement
 *    replies
 *  - Success
 *  - Failure
 *
 *  Interact with some other kind of actor*/
object AkkaCapabilitiesExercises extends App {

  val actorSystem = ActorSystem()

  // Exercise 1

  // Domain of the counter
  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }
  class Counter extends Actor {
    import Counter._  // Importing counter companion object
    var counter = 0
    override def receive: Receive = {
      case Increment => counter += 1
      case Decrement => counter -= 1
      case Print => println(s"The value of counter is $counter.")

      /*case "Increment" => counter += 1
      case "Decrement" => counter -= 1
      case "Print" => println(s"The value of counter is $counter.")*/
      case _ => println("Invalid Input")
    }
  }

  val counter = actorSystem.actorOf(Props(Counter()), "counterActor")
  (1 to 5).foreach(_ => counter ! Counter.Increment)
  counter ! Counter.Print
  (1 to 3).foreach(_ => counter ! Counter.Decrement)
  counter ! Counter.Print
//  counter ! "Increment"
//  counter ! "Print"
//  counter ! "Decrement"
//  counter ! "Print"


// Exercise 2

  object BankAccount {
    case class Deposit(amount: Double)
    case class Withdraw(amount: Double)
    case object Statement
    case class DisplayMessage(args: String)
  }
  class BankAccount extends Actor {
    import BankAccount._
    var amount: Double = 10000.0
    override def receive: Receive = {
      case Deposit(amount) =>
        this.amount += amount
        sender() ! DisplayMessage("Success: Deposit successful")
      case Withdraw(amount) =>
        if amount <= 0 then self ! DisplayMessage("Failure: Invalid deposit amount") else
          if this.amount >= amount then
            this.amount -= amount
            sender() ! DisplayMessage("Success: Withdrawal successful")
          else
            sender() ! DisplayMessage(s"Failure: Because your current balance is ${this.amount} and you are trying to withdraw $amount")
      case Statement => sender() ! s"The current amount is $amount."

      /*case "Deposit" =>
        println("Enter the amount to deposit:")
        val input = scala.io.StdIn.readDouble()
        amount += input
      case "Withdraw" =>
        println("Enter the amount to withdraw:")
        val input = scala.io.StdIn.readDouble()
        if amount >= input then
          amount -= input
          self ! DisplayMessage("Success")
        else
          self ! DisplayMessage("Failure")*/
      case DisplayMessage(args) => println(args)
      case _ => println("Invalid Input")
    }
  }

  object Person {
    case class LiveTheLive(account: ActorRef)
  }

  class Person extends Actor {
    import Person._
    import BankAccount._

    override def receive: Receive = {
      case LiveTheLive(account) =>
        account ! Deposit(10000)
        account ! Withdraw(30000)
        account ! Withdraw(19000.5)
        account ! Statement
      case message => println(message.toString)
    }
  }

  val bankAccount = actorSystem.actorOf(Props(BankAccount()), "bankAccount")
  val person = actorSystem.actorOf(Props(Person()), "person")

  person ! LiveTheLive(bankAccount)
}
