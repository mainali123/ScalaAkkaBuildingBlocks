package AkkaEssentials

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ChangingActorBehaviour extends App{

  object FussyKid{
    case object KidAccept
    case object KidReject
    val HAPPY = "happy"
    val SAD = "sad"
  }
  class FussyKid extends Actor {  // Bad way of changing state
    import FussyKid._
    import Mom._

    var state = HAPPY
    override def receive: Receive = {
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(message) =>
        if state == HAPPY then sender() ! KidAccept
        else sender() ! KidReject
    }
  }

  class StatelessFussyKid extends Actor { // good way to change
    import FussyKid._
    import Mom._
    override def receive: Receive = {
      happyReceive
    }

    def happyReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, false)// change my receive handler to sadReceive
      case Food(CHOCOLATE) => // stay happy
      case Ask(_) => sender() ! KidAccept
    }
    def sadReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, false) // stay sad
      case Food(CHOCOLATE) =>   context.unbecome()// Change my receive handler to happyReceive
      case Ask(_) => sender() ! KidReject
    }
  }

  object Mom {
    case class MomStart(kidRef: ActorRef)
    case class Food(food: String)
    case class Ask(message: String) // do you want to play?
    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate"
  }
  class Mom extends Actor {
    import Mom._
    import FussyKid._
    override def receive: Receive = {
      case MomStart(kidRef) =>
        // test our interaction
        kidRef ! Food(VEGETABLE)
        kidRef ! Ask("do you want to play?")
      case KidAccept => println("My kid is happy!")
      case KidReject => println("My kid is sad!")
    }
  }

  val actorSystem = ActorSystem("ChangingActorBehaviour")
  val fussyKid = actorSystem.actorOf(Props(FussyKid()))
  val statelessFussyKid = actorSystem.actorOf(Props(StatelessFussyKid()))
  val mom = actorSystem.actorOf(Props(Mom()))

  mom ! Mom.MomStart(statelessFussyKid)
//  mom ! Mom.MomStart(fussyKid)

/*
* mom receives MomStart
*   - kid receives Food(veg) -> kid will change the handler to sad receive
*   - kid receives Ask(play?) -> kid replies with the sadReceive handler
* mom receives KidReject*/

/*
context.become
  Food(veg) -> stack.push(sadReceive)
  Food(chocolate) -> stack.push(happyReceive)

  Stack:
  1. happyReceive
  2. sadReceive
  3. happyReceive
*/

/*
new behaviour(context.unbecome)
  Food(veg)
  Food(veg)
  Food(chocolate)

  Stack:
  1. sadReceive
  2. sadReceive
  3. happyReceive

 */
}
