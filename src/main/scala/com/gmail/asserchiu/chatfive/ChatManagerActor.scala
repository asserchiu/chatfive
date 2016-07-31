package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props, FSM, ActorRef }
import akka.routing.{ ActorRefRoutee, BroadcastRoutingLogic, Router }

class ChatManagerActor extends Actor with ActorLogging with FSM[ChatManagerActorState.State, Int] {
  import ChatManagerActor._
  import ChatManagerActorState._

  startWith(Offline, 0)

  var router = {
    val routees = Vector.fill(5) {
      val theChatParticipantActor = context.actorOf(Props[ChatParticipantActor])
      context watch theChatParticipantActor
      ActorRefRoutee(theChatParticipantActor)
    }
    Router(BroadcastRoutingLogic(), routees)
  }

  when(Offline) {
    case Event(SetupSystem, _) =>
      log.info("In ChatManagerActor - receive case SetupSystem")
      val theUserActor = context.actorOf(UserActor.props, "theUserActor")
      theUserActor ! UserActor.Begin
      stay()
    case Event(Shutdown, _) =>
      log.info("In ChatManagerActor - receive case Shutdown")
      context.system.terminate()
      stay()
    case Event(GoOnline, _) =>
      log.info("In ChatManagerActor - receive case GoOnline")
      goto(Online)
  }

  when(Online) {
    case Event(GoOffline, _) =>
      log.info("In ChatManagerActor - receive case GoOffline")
      goto(Offline)
    case Event(AddChatParticipant, _) =>
      log.info("In ChatManagerActor - receive case AddChatParticipant")
      val theChatParticipantActor = context.actorOf(Props[ChatParticipantActor])
      context watch theChatParticipantActor
      router = router.addRoutee(theChatParticipantActor)
      stay()
    case Event(RemoveChatParticipant(ref: String), _) =>
      log.info("In ChatManagerActor - receive case RemoveChatParticipant(\"{}\")", ref)
      router = router.removeRoutee(context.actorSelection(ref))
      stay()
    case Event(UserActor.Speak(text: String), _) =>
      log.info("In ChatManagerActor - receive case UserActor.Speak(\"{}\")", text)
      router.route(UserActor.Speak(text), sender())
      stay()
    case Event(ChatParticipantActor.Reply(text: String), _) =>
      // Skipped by ChatParticipantActor, message sent directly to UserActor
      log.info("In ChatManagerActor - receive case ChatParticipantActor.Reply(\"{}\")", text)
      stay()
  }

  initialize()
}

object ChatManagerActor {
  val props = Props[ChatManagerActor]
  case object SetupSystem
  case object Shutdown
  case object GoOnline
  case object GoOffline
  case object AddChatParticipant
  case class RemoveChatParticipant(ref: String)
}

object ChatManagerActorState {
  sealed trait State
  case object Online extends State
  case object Offline extends State
}
