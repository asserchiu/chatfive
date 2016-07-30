package com.gmail.asserchiu.chatfive

import akka.actor.ActorSystem

object ApplicationMain extends App {
  val system = ActorSystem("ChatFiveSystem")
  val theSystemBootActor = system.actorOf(SystemBootActor.props, "theSystemBootActor")
  theSystemBootActor ! SystemBootActor.Initialize

  system.awaitTermination()
}
