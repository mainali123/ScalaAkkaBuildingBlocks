package AkkaEssentials

import akka.actor.{Actor, ActorRef, ActorSystem, Props, actorRef2Scala}

object ChangingActorBehaviourPractice1 extends App {

  object Utsav{
    val HAPPY = "I am happy."
    val SAD = "I am sad."
    case object Accept
    case object Reject
  }
  class Utsav extends Actor {
    import Utsav._
    import Diwash._
    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEG) => context.become(sadReceive)
      case Ask(message) => sender() ! Accept
    }

    def sadReceive: Receive = {
      case Food(MEAT) => context.become(happyReceive)
      case Ask(message) => sender() ! Reject
    }
  }

  object Diwash{
    case class Reference(vai: ActorRef)
    case class Ask(message: String)
    case class Food(food: String)
    val VEG = "Mushroom"
    val MEAT = "Mutton"

  }
  class Diwash extends Actor {
    import Diwash._
    import Utsav._

    override def receive: Receive = {
      case Reference(ref) =>
        ref ! Food(MEAT)
        ref ! Ask("Are you Happy?")
      case Accept => println("Yes I am happy.")
      case Reject => println("No I am not happy.")
    }
  }

  val actorSystem = ActorSystem()
  val dada = actorSystem.actorOf(Props(Diwash()))
  val vai = actorSystem.actorOf(Props(Utsav()))
  dada ! Diwash.Reference(vai)
}
