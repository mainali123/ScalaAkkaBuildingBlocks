package AkkaEssentials.AkkaActors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ChangingActorBehaviour extends App {
  val actorSystem = ActorSystem("changingActorBehaviour")


  object FussyKid {
    case object KidAccept

    case object KidReject

    val HAPPY = "happy"
    val SAD = "sad"
  }

  class FussyKid extends Actor {

    import FussyKid._
    import Mom._

    var state = HAPPY

    override def receive: Receive = {
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask => if state == HAPPY then sender() ! KidAccept else sender() ! KidReject
    }
  }

  object Mom {
    case class MomStart(kidRef: ActorRef)

    case class Food(food: String)

    case class Ask(message: String) // do you want to play?

    val VEGETABLE = "vegetables"
    val CHOCOLATE = "chocolate"
  }

  class Mom extends Actor {

    import Mom._
    import FussyKid._

    override def receive: Receive = {
      case MomStart(kidRef) =>
        kidRef ! Food(VEGETABLE)
        kidRef ! Ask
        kidRef ! Food(CHOCOLATE)
        kidRef ! Ask
      case KidAccept => println("Kid is happy.")
      case KidReject => println("Kid is sad.")
    }
  }

  val mom = actorSystem.actorOf(Props(Mom()), "Mom")
  val kid = actorSystem.actorOf(Props(FussyKid()), "Kid")

  mom ! Mom.MomStart(kid)

  actorSystem.terminate()
}
