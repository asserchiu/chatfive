package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props }

class ChatParticipantActor extends Actor with ActorLogging {
  import ChatParticipantActor._

  def receive = {
    case UserActor.Speak(text) =>
      log.info("In ChatParticipantActor - receive case UserActor.Speak(\"{}\")", text)
      sender() ! Reply("Echo from "+context.toString())
  }
}

object ChatParticipantActor {
  val props = Props[ChatParticipantActor]
  case class Reply(text: String)
}
