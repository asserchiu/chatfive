package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props, FSM, ActorRef }
import akka.routing.{ ActorRefRoutee, BroadcastRoutingLogic, Router }
import scala.concurrent.Await
import scala.concurrent.duration._
import java.time._
import java.io.{ File, PrintWriter }

class ChatManagerActor extends Actor with ActorLogging with FSM[ChatManagerActorState.State, Int] {
  import ChatManagerActor._
  import ChatManagerActorState._

  startWith(Offline, 0)

  val theUserActor = context.actorOf(UserActor.props, "theUserActor")

  var chatHistory: List[String] = List()

  var router = {
    val routees = Vector.fill(5) {
      val theChatParticipantActor = context.actorOf(Props[ChatParticipantActor])
      context watch theChatParticipantActor
      ActorRefRoutee(theChatParticipantActor)
    }
    Router(BroadcastRoutingLogic(), routees)
  }

  onTransition {
    case Online -> Offline =>
      log.info("In ChatManagerActor - Online -> Offline")
      println("ChatManager now Offline")
      // Clear chat history
      chatHistory = List()
    case Offline -> Online =>
      log.info("In ChatManagerActor - Offline -> Online")
      println("ChatManager now Online")
  }

  when(Offline) {
    case Event(SetupSystem, _) =>
      log.info("In ChatManagerActor - receive Offline case SetupSystem")
      theUserActor ! UserActor.Begin
      stay()
    case Event(Shutdown, _) =>
      log.info("In ChatManagerActor - receive Offline case Shutdown")
      // TODO: delete file
      try {
        val fp = new File("chat.log")
        fp.delete()
      }
      context.system.terminate()
      stay()
    case Event(GoOnline, _) =>
      log.info("In ChatManagerActor - receive Offline case GoOnline")
      goto(Online)
  }

  when(Online) {
    case Event(Shutdown, _) =>
      log.info("In ChatManagerActor - receive Online case Shutdown")
      // TODO: Write chat history to file
      try {
        val writer = new PrintWriter(new File("chat.log"), "UTF-8")
        chatHistory.reverse.foreach((each: String) => writer.println(each))
        writer.close()
      }
      context.system.terminate()
      stay()
    case Event(GoOffline, _) =>
      log.info("In ChatManagerActor - receive Online case GoOffline")
      goto(Offline)
    case Event(AddChatParticipant, _) =>
      log.info("In ChatManagerActor - receive Online case AddChatParticipant")
      val theChatParticipantActor = context.actorOf(Props[ChatParticipantActor])
      context watch theChatParticipantActor
      router = router.addRoutee(theChatParticipantActor)
      stay()
    case Event(RemoveChatParticipant(ref: String), _) =>
      log.info("In ChatManagerActor - receive Online case RemoveChatParticipant(\"{}\")", ref)
      // TODO: Add error handling
      val targetActorSelection = context.actorSelection(ref)
      val targetActorRef = Await.result(targetActorSelection.resolveOne(1 seconds), 1 seconds)
      context.unwatch(targetActorRef)
      context.stop(targetActorRef)
      router = router.removeRoutee(ActorRefRoutee(targetActorRef))
      stay()
    case Event(UserActor.Speak(text: String), _) =>
      log.info("In ChatManagerActor - receive Online case UserActor.Speak(\"{}\")", text)
      val msg = List(ZonedDateTime.now(ZoneId.of("UTC")), sender().toString(), text)
      println(msg.mkString(", "))
      chatHistory = msg.mkString(", ") :: chatHistory
      router.route(UserActor.Speak(text), context.self)
      stay()
    case Event(ChatParticipantActor.Reply(text: String), _) =>
      log.info("In ChatManagerActor - receive Online case ChatParticipantActor.Reply(\"{}\")", text)
      val msg = List(ZonedDateTime.now(ZoneId.of("UTC")), sender().toString(), text)
      println(msg.mkString(", "))
      chatHistory = msg.mkString(", ") :: chatHistory
      theUserActor ! ChatParticipantActor.Reply(text)
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
