package SelfPracticeProblems

import akka.actor.{Actor, ActorSystem, Props}

/*
Problem: Creating a Simple Counter with Akka

Objective: Build a basic Akka application that uses actors to create a simple counter. This exercise will introduce you to the actor model and how to work with actors in Akka.

Requirements:

    Create an actor called CounterActor that represents a counter.
    The CounterActor should start at 0.
    Implement two message types that the CounterActor can handle:
        Increment: This message should increase the counter by 1.
        GetValue: This message should return the current value of the counter to the sender.
    Create a main application that:
        Creates an instance of the CounterActor.
        Sends multiple Increment messages to the CounterActor.
        Sends a GetValue message to the CounterActor to retrieve the final value.
        Prints the final value to the console.

Example Interaction:

Increment
Increment
Increment
GetValue
Final Value: 3

Implementation Steps:

    Define the CounterActor class that extends akka.actor.Actor.

    Implement the logic in the CounterActor to handle Increment and GetValue messages, updating the counter accordingly.

    Create a main application or driver program.

    In the main program, create an instance of the CounterActor using the Akka actor system.

    Send multiple Increment messages to the CounterActor to simulate increasing the counter.

    Send a GetValue message to the CounterActor to retrieve the final value.

    Print the final value to the console.

Learning Objectives:

This simplified exercise will help beginners learn the following Akka concepts:

    Creating and defining actors.
    Sending and receiving messages between actors.
    Actor state management.
    Using the actor system to manage actors.
*/

object ProblemOne extends App {

  val actorSystem = ActorSystem()

  object CounterActor {
    case object Increment
    case object GetValue
  }
  class CounterActor extends Actor {
    import CounterActor._
    var counter = 0
    override def receive: Receive = {
      case Increment => counter += 1
      case GetValue => println(s"The current counter is $counter.")
    }
  }

  val counterActor = actorSystem.actorOf(Props(CounterActor()))
  counterActor ! CounterActor.Increment
  counterActor ! CounterActor.Increment
  counterActor ! CounterActor.Increment
  counterActor ! CounterActor.GetValue
}
