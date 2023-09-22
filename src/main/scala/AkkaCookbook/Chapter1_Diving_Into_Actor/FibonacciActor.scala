package AkkaCookbook.Chapter1_Diving_Into_Actor

import akka.actor.Actor
import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.*
import scala.language.postfixOps

object FibonacciActorApp extends App {

  class FibonacciActor extends Actor {
    override def receive: Receive = {
      case num: Int => val fibonacciNumber = fib(num)
        sender() ! fibonacciNumber
    }

    def fib(n: Int): Int = n match
      case 0 | 1 => n
      case _ => fib(n - 1) + fib(n - 2)
  }


  implicit val timeout: Timeout = Timeout(10 seconds)
  val actorSystem = ActorSystem("HelloAkka")
  val actor = actorSystem.actorOf(Props(FibonacciActor()))

  // Asking for result from actor
  val future = (actor ? 5).mapTo[Int]
  val fibonacciNumber = Await.result(future, 10 seconds)
  println(fibonacciNumber)

}
