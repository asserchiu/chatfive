package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props }

class ChatManagerActor extends Actor with ActorLogging {
  import ChatManagerActor._

  def receive = {
    case SetupSystem =>
      log.info("In ChatManagerActor - receive case SetupSystem")
      val theUserActor = context.actorOf(UserActor.props, "theUserActor")
      theUserActor ! UserActor.Begin
    case Shutdown =>
      log.info("In ChatManagerActor - receive case Shutdown")
      context.system.shutdown()
    case UserActor.Speak(text: String) =>
      log.info("In ChatManagerActor - receive case UserActor.Speak(\"{}\")", text)
      context.system.shutdown()
  }
}

object ChatManagerActor {
  val props = Props[ChatManagerActor]
  case object SetupSystem
  case object Shutdown
}
