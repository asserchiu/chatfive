package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props }

class UserActor extends Actor with ActorLogging {
  import UserActor._

  def receive = {
    case Begin =>
      log.info("In UserActor - receive case Begin")
      val theConsoleActor = context.actorOf(ConsoleActor.props, "theConsoleActor")
      theConsoleActor ! ConsoleActor.EnableConsole
    case ConsoleActor.MessageFromConsole(text) =>
      log.info("In UserActor - receive case ConsoleActor.MessageFromConsole(\"{}\")", text)
      context.parent ! ChatManagerActor.Shutdown
  }
}

object UserActor {
  val props = Props[UserActor]
  case object Begin
}
