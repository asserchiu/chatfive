package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props }
import io.StdIn._

class ConsoleActor extends Actor with ActorLogging {
  import ConsoleActor._

  def receive = {
    case EnableConsole =>
      log.info("In ConsoleActor - receive case EnableConsole")
      val input = readLine(
        "Supported Commands: (Case insensitive)\n"
          + "* Shutdown/Exit \n"
          + "* GoOnline/Online/On \n"
          + "* GoOffline/Offline/Off \n"
          + "* AddChatParticipant/Add \n"
          + "* RemoveChatParticipant/Remove/Kill <ActorPath> \n"
          + "Input message or `exec <command>`: ")
      self ! MessageFromConsole(input)
    case MessageFromConsole(text: String) =>
      context.parent ! MessageFromConsole(text)
      self ! EnableConsole
  }
}

object ConsoleActor {
  val props = Props[ConsoleActor]
  case object EnableConsole
  case class MessageFromConsole(text: String)
}
