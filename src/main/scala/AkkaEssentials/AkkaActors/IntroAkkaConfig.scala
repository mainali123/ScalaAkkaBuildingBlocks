package AkkaEssentials.AkkaActors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object IntroAkkaConfig extends App {

  class SimpleLoggingActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }

  /**
   * 1- Inline configuration
   * */
  val confString =
    """
      |akka {
      | loglevel = "DEBUG"
      |}
      |""".stripMargin
  val config = ConfigFactory.parseString(confString)
  val system = ActorSystem("ConfigurationDemo", ConfigFactory.load(config))

  val actor = system.actorOf(Props(SimpleLoggingActor()))
  actor ! "A message to remember"

  val defaultConfigFileSystem = ActorSystem("DefaultCOnfigFileDemo")
  val defaultOCnfigActor = defaultConfigFileSystem.actorOf(Props(SimpleLoggingActor()))
  actor ! "Remember me"

}
