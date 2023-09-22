/*
Building a Simple Banking System with Akka

Objective: In this exercise, you'll build a simple banking system using Akka actors. The system will consist of multiple bank accounts represented as actors, and clients can deposit and withdraw money from their accounts.

Requirements:

    AccountActor: Create an AccountActor class that represents a bank account. Each account should have a unique account number and an initial balance.

    ClientActor: Create a ClientActor class that represents a client. Each client should have a name and an account number.

    BankActor: Create a BankActor class that manages all the bank accounts. It should be able to create new accounts, look up account balances, and facilitate deposits and withdrawals.

    Implement the following messages for actors:
        CreateAccount(accountNumber: String, initialBalance: Double): A message sent to the BankActor to create a new account with the given account number and initial balance.
        Deposit(accountNumber: String, amount: Double): A message sent by clients to deposit money into their accounts.
        Withdraw(accountNumber: String, amount: Double): A message sent by clients to withdraw money from their accounts.
        CheckBalance(accountNumber: String): A message sent by clients to check their account balances.
        TransactionCompleted(message: String): A message sent by the BankActor to the client to confirm the success of a transaction.

    Implement a main program that:
        Creates a BankActor and a few client actors.
        Sends messages to create bank accounts and perform deposit and withdrawal operations.
        Prints the final account balances to the console.

Hints:

    Use actor hierarchies to organize your actors. For example, you can have a parent actor create child AccountActor and ClientActor actors.

    Implement the necessary logic in the AccountActor and BankActor to handle deposit, withdrawal, and balance check operations.

    Ensure that the system handles concurrency correctly, especially when multiple clients try to access their accounts simultaneously.

Learning Objectives:

This exercise will help you practice and understand more advanced Akka concepts, including:

    Creating multiple actors and organizing them hierarchically.
    Implementing more complex message handling logic.
    Managing state and concurrency in an actor system.
    Handling transactions and ensuring data consistency.
*/

package SelfPracticeProblems

import akka.actor.{Actor, ActorSystem, Props}

object ProblemTwo {

  val actorSystem = ActorSystem()

  object AccountActor {

  }

  class AccountActor extends Actor {
    import BankActor._
    var balance = 0.0
    override def receive: Receive = {
      case Deposit(accNo, amt) =>
        println(s"$amt deposited in $accNo.")
        balance += amt
      case Withdraw(accNo, amt) =>
        println(s"$amt withdrawn from $accNo.")
        balance -= amt
      case CheckBalance(accNo) => println(s"$accNo currently have $balance")
    }
  }

  class ClientActor extends Actor {
    import BankActor._
    override def receive: Receive = {
      case CreateAccount => ???
    }
  }

  object BankActor {
    case object CreateAccount

    case object BankAccountNumbers

    case class Deposit(accNo: String, amt: Double)

    case class Withdraw(accNo: String, amt: Double)

    case class CheckBalance(accNo: String)
  }

  class BankActor extends Actor {

    import BankActor._

    val list: List[String] = List()

    override def receive: Receive = {
      case CreateAccount =>
        val acc_no = () => scala.util.Random.alphanumeric.filter(_.isLetterOrDigit).take(10).mkString
        while list.contains(acc_no) do acc_no()
        list :+ acc_no
      case BankAccountNumbers => list.foreach(println)

    }

  }

  val diwash = actorSystem.actorOf(Props(ClientActor()), "diwash")
  val utsav = actorSystem.actorOf(Props(ClientActor()), "utsav")
  val sushank = actorSystem.actorOf(Props(ClientActor()), "sushank")
  val aakash = actorSystem.actorOf(Props(ClientActor()), "aakash")
  val aadyatan = actorSystem.actorOf(Props(ClientActor()), "aadyatan")
}
