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
      val Pattern = "exec (.*)".r
      text match {
        case Pattern(command) =>
          log.info("In UserActor - command \"{}\" detected", command)
          command.toLowerCase match {
            case "shutdown" =>
              context.parent ! ChatManagerActor.Shutdown
            // case "GoOnline" =>
            //   context.parent ! ChatManagerActor.GoOnline
            // case "GoOffline" =>
            //   context.parent ! ChatManagerActor.GoOffline
            case _ =>
              log.info("In UserActor - command \"{}\" invalid.", command)
              context.system.shutdown()
          }
        case _ =>
          log.info("In UserActor - \"{}\" is a normal message", text)
          context.parent ! Speak(text)
      }

  }
}

object UserActor {
  val props = Props[UserActor]
  case object Begin
  case class Speak(text: String)
}
