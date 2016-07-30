package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props }
import io.StdIn._

class ConsoleActor extends Actor with ActorLogging {
  import ConsoleActor._

  def receive = {
    case EnableConsole =>
      log.info("In ConsoleActor - receive case EnableConsole")
      sender() ! MessageFromConsole(readLine("Input message: "))
  }
}

object ConsoleActor {
  val props = Props[ConsoleActor]
  case object EnableConsole
  case class MessageFromConsole(text: String)
}
