package AkkaEssentials.AkkaActors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ChildActors extends App {
  val actorSystem = ActorSystem()

  object Parent {
    case class CreateChild(name: String)

    case class TellChild(message: String)
  }

  class Parent extends Actor {

    import Parent._

    override def receive: Receive = {
      case CreateChild(name) =>
        println(s"${self.path} creating child...")
        // create a new actor right here
        val childref = context.actorOf(Props(Child()), name)
        context.become(withChild(childref))
    }

    def withChild(childRef: ActorRef): Receive = {
      case TellChild(messsage) => childRef forward (messsage)
    }
  }

  class Child extends Actor {
    override def receive: Receive = {
      case message => println(s"${self.path} I got $message.")
    }
  }
  val parent = actorSystem.actorOf(Props(Parent()), "parent")
  parent ! Parent.CreateChild("child")
  parent ! Parent.TellChild("Hey Kid...")

  // actor hierarchy
  // parent -> child -> grandchild
  //        -> child2 ->

  /*
  * Guardian actor (top-level)
    - / => the root guardian (manages system and user guardian)  `MAIN ACTOR`
    -  /system => system guardian
    -  /user => user-level guardian (every parent actor we create)
   */

  /**
   * Actor Selection
   * */
  val childSelection = actorSystem.actorSelection("/user/parent/child")
  childSelection ! "Until I found you"

  /**
   * Danger!
   * NEVER PASS MUTABLE ACTOR STATE, OR THE `THIS`  REFERENCE, TO CHILD ACTORS.
   *
   * NEVER IN YOUR LIFE.
   * */

  object NaiveBanKAccount {
    case class Deposit(amt: Int)
    case class Withdraw(amt: Int)
    case object InitializeAccount
  }
  class NaiveBanKAccount extends Actor {
    import NaiveBanKAccount._
    import CreditCard._
    var amount = 0
    override def receive: Receive = {
      case InitializeAccount =>
        val creditCardref = context.actorOf(Props(CreditCard()), "card")
        creditCardref !  AttachedToAccount(this)  // !!
      case Deposit(funds) => deposit(funds)
      case Withdraw(funds) => withdraw(funds)

    }
    def deposit(funds: Int) =
      println(s"${self.path}: depositing $funds on top of $amount.")
      amount += funds
    def withdraw(funds: Int) =
      println(s"${self.path}: withdrawing $funds on top of $amount.")
      amount -= funds
  }

  object CreditCard {
    case class AttachedToAccount(naiveBanKAccount: NaiveBanKAccount)  // !!
    case object CheckStatus
  }
  class CreditCard extends Actor {
    import CreditCard._
    override def receive: Receive = {
      case AttachedToAccount(account) => context.become(attachedTo(account))
    }
    def attachedTo(account: NaiveBanKAccount): Receive = {
      case CheckStatus =>
        println(s"${self.path} your message has been processed.")
        // benign
        account.withdraw(1) // because I can
    }
  }

  import NaiveBanKAccount._
  import CreditCard._

  val banKAccountRef = actorSystem.actorOf(Props(NaiveBanKAccount()), "account")
  banKAccountRef ! InitializeAccount
  banKAccountRef ! Deposit(100)

  Thread.sleep(500)
  val ccSelection = actorSystem.actorSelection("/user/account/card")
  ccSelection ! CheckStatus

  // WRONG!!

}
