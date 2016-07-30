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
            case "shutdown" | "exit" =>
              context.parent ! ChatManagerActor.Shutdown
            case "goonline" | "online" | "on" =>
              context.parent ! ChatManagerActor.GoOnline
            case "gooffline" | "offline" | "off" =>
              context.parent ! ChatManagerActor.GoOffline
            case "addchatparticipant" | "add" =>
              context.parent ! ChatManagerActor.AddChatParticipant
            // case "removechatparticipant" | "remove" | "kill" =>
            //   context.parent ! ChatManagerActor.RemoveChatParticipant(readLine("Input ChatParticipant child name: "))
            case _ =>
              log.info("In UserActor - command \"{}\" invalid.", command)
          }
        case _ =>
          log.info("In UserActor - \"{}\" is a normal message", text)
          context.parent ! Speak(text)
      }
    case ChatParticipantActor.Reply(text: String) =>
      log.info("In UserActor - receive case ChatParticipantActor.Reply(\"{}\")", text)
  }
}

object UserActor {
  val props = Props[UserActor]
  case object Begin
  case class Speak(text: String)
}
