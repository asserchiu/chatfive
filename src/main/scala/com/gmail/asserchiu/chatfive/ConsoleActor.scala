package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props }
import io.StdIn._

class ConsoleActor extends Actor with ActorLogging {
  import ConsoleActor._

  def receive = {
    case EnableConsole =>
      log.info("In ConsoleActor - receive case EnableConsole")
      while (true) {
        sender() ! MessageFromConsole(
          readLine(
            "Supported Commands: (Case insensitive)\n"
            + "* Shutdown/Exit \n"
            + "* GoOnline/Online/On \n"
            + "* GoOffline/Offline/Off \n"
            + "* AddChatParticipant/Add \n"
            + "Input message or `exec <command>`: "
          )
        )
      }
  }
}

object ConsoleActor {
  val props = Props[ConsoleActor]
  case object EnableConsole
  case class MessageFromConsole(text: String)
}
