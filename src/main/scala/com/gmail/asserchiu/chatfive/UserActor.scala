package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props }
import java.time._

class UserActor extends Actor with ActorLogging {
  import UserActor._

  def receive = {
    case Begin =>
      log.info("In UserActor - receive case Begin")
      val theConsoleActor = context.actorOf(ConsoleActor.props, "theConsoleActor")
      theConsoleActor ! ConsoleActor.EnableConsole
    case ConsoleActor.MessageFromConsole(text) =>
      log.info("In UserActor - receive case ConsoleActor.MessageFromConsole(\"{}\")", text)
      val CommandPattern = "exec (.*)".r
      val CommandPatternArg = "exec (.*) (.*)".r
      text match {
        case CommandPatternArg(command, arg) =>
          log.info("In UserActor - command \"{}\" detected", command)
          command.toLowerCase match {
            case "removechatparticipant" | "remove" | "kill" =>
              context.parent ! ChatManagerActor.RemoveChatParticipant(arg)
            case _ =>
              log.info("In UserActor - command \"{}\" invalid.", command)
          }
        case CommandPattern(command) =>
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
            case _ =>
              log.info("In UserActor - command \"{}\" invalid.", command)
          }
        case _ =>
          log.info("In UserActor - \"{}\" is a normal message", text)
          val msg = List(ZonedDateTime.now(ZoneId.of("UTC")), context.self.path.toStringWithoutAddress, text)
          println(msg.mkString(", "))
          context.parent ! Speak(text)
      }
    case ChatParticipantActor.Reply(text: String) =>
      log.info("In UserActor - receive case ChatParticipantActor.Reply(\"{}\")", text)
      val msg = List(ZonedDateTime.now(ZoneId.of("UTC")), sender().path.toStringWithoutAddress, text)
      println(msg.mkString(", "))
  }
}

object UserActor {
  val props = Props[UserActor]
  case object Begin
  case class Speak(text: String)
}
