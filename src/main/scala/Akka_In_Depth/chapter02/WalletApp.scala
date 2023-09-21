package Akka_In_Depth.chapter02

import akka.actor.ActorSystem

object WalletApp extends App {
  val guardian = ActorSystem("wallet")

}
