package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props }
import io.StdIn._

class ConsoleActor extends Actor with ActorLogging {
  import ConsoleActor._

  def receive = {
    case EnableConsole =>
      log.info("In ConsoleActor - receive case EnableConsole")
      println("Supported Commands: Shutdown, GoOnline, GoOffline (Case insensitive)")
      sender() ! MessageFromConsole(readLine("Input message or `exec <command>`: "))
  }
}

object ConsoleActor {
  val props = Props[ConsoleActor]
  case object EnableConsole
  case class MessageFromConsole(text: String)
}
