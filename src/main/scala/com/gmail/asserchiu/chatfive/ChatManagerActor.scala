package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props, FSM }

class ChatManagerActor extends Actor with ActorLogging with FSM[ChatManagerActorState.State, Int] {
  import ChatManagerActor._
  import ChatManagerActorState._

  startWith(Offline, 0)

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
    case Event(UserActor.Speak(text: String), _) =>
      log.info("In ChatManagerActor - receive case UserActor.Speak(\"{}\")", text)
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
}

object ChatManagerActorState {
  sealed trait State
  case object Online extends State
  case object Offline extends State
}
