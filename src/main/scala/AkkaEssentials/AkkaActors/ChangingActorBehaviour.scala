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

  class StatelessFussyKid extends Actor {
    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive)// change my receive handler to sadReceive
      case Food(CHOCOLATE) => // stay happy
      case Ask => sender() ! KidAccept
    }
    def sadReceive: Receive = {
      case Food(VEGETABLE) => // stay sad
      case Food(CHOCOLATE) => context.become(happyReceive)  // Change my receive handler to happyReceive
      case Ask => sender() ! KidReject
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
  val statelessFussyKid = actorSystem.actorOf(Props(StatelessFussyKid()), "StatelessFussyKid")


  mom ! Mom.MomStart(statelessFussyKid)

  actorSystem.terminate()
}
