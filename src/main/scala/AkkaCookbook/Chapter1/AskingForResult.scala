package AkkaCookbook.Chapter1

import akka.actor.{Actor, ActorSystem, Props, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps  // Import this to allow `10 seconds` syntax

object AskingForResult extends App {
  val actorSystem = ActorSystem()
  val actor = actorSystem.actorOf(Props(FibonacciActor()), "FibonacciActor")

  implicit val timeout: Timeout = Timeout(10 seconds)

  // asking for result from actor
  val future = (actor ? 5).mapTo[Int]
  val fibonacciNumber = Await.result(future, 10 seconds)
  println(fibonacciNumber)
}

class FibonacciActor extends Actor {
  override def receive: Receive = {

    case num: Int =>
      val senderRef = sender()
      val fibonacciNumber = fib(num)
      senderRef ! fibonacciNumber
  }
  def fib (n: Int): Int = n match
    case 0 | 1 => n
    case _ => fib(n-1) + fib(n-2)
}