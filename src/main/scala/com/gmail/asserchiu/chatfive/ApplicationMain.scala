package com.gmail.asserchiu.chatfive

import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory

object ApplicationMain extends App {
  if (args.isEmpty || args.head.toLowerCase == "userconsole") {
    println("Start UserConsoleSystem")
    ActorSystem("UserConsoleSystem", ConfigFactory.load("UserConsoleSetting"))
  }
  if (args.isEmpty || args.head.toLowerCase == "chatmanager") {
    println("Start ChatFiveSystem")
    val system = ActorSystem("ChatFiveSystem", ConfigFactory.load("ChatManagerSetting"))
    val theSystemBootActor = system.actorOf(Props[SystemBootActor], "theSystemBootActor")
    theSystemBootActor ! SystemBootActor.Initialize
  }
}
