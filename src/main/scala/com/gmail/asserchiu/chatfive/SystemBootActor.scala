package com.gmail.asserchiu.chatfive

import akka.actor.{ Actor, ActorLogging, Props }

class SystemBootActor extends Actor with ActorLogging {
  import SystemBootActor._

  def receive = {
    case Initialize =>
      log.info("In SystemBootActor - receive case Initialize")
      context.system.shutdown()
  }
}

object SystemBootActor {
  val props = Props[SystemBootActor]
  case object Initialize
}
