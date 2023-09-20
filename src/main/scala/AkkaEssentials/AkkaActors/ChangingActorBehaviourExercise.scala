package AkkaEssentials.AkkaActors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ChangingActorBehaviourExercise extends App {
  val actorSystem = ActorSystem("Exercise")

  /**
   * 1 - recreate the counter actor with context.become and NO MUTABLE STATE
   * */
  object WordCountActor {
    case object Increment
    case object Decrement
    case object Print
  }
  class WordCountActor extends Actor {
    import WordCountActor._
    override def receive: Receive = countReceive(0)

    def countReceive(i: Int): Receive = {
      case Increment =>
        println(s"[${i}] Incrementing")
        context.become(countReceive(i + 1))
      case Decrement =>
        println(s"[${i}] Decrementing")
        context.become(countReceive(i - 1))
      case Print => println(s"[counter]: My current count is ${i}.")
    }
  }

  val counter = actorSystem.actorOf(Props(WordCountActor()))
  (1 to 5).foreach(_ => counter ! WordCountActor.Increment)
  (1 to 3).foreach(_ => counter ! WordCountActor.Decrement)
  counter ! WordCountActor.Print


  /**
   * 2 - simplified voting system
   * */

  case class Vote(candidate: String)

  case object VoteStatusRequest

  case class VoteStatusReply(candidate: Option[String])

  class Citizen extends Actor {
    var candidate: Option[String] = None
    override def receive: Receive = ???
  }

  case class AggregateVotes(citizens: Set[ActorRef])

  class VoteAggregator extends Actor {
    override def receive: Receive = ???
  }

  val alice = actorSystem.actorOf(Props(Citizen()))
  val bob = actorSystem.actorOf(Props(Citizen()))
  val charlie = actorSystem.actorOf(Props(Citizen()))
  val diwash = actorSystem.actorOf(Props(Citizen()))

  alice ! Vote("Martin")
  bob ! Vote("Jonas")
  charlie ! Vote("Roland")
  diwash ! Vote("Roland")

  val voteAggregator = actorSystem.actorOf(Props(VoteAggregator()))
  voteAggregator ! AggregateVotes(Set(alice, bob, charlie, diwash))

  /*
  * Print the status of the votes
  *
  * Martin ->1
  * Jonas -> 1
  * Ronald ->2
  * */
}
