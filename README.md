chatfive
=========================

A simple chatroom(-like) application.

Built with [Scala](http://www.scala-lang.org) and [Akka](http://akka.io).


How to use?
-------------------------

Run with `sbt run`.

Commit `exec <command>` in the command prompt to execute command.

or

Anything else as a message.


Supported Commands
-------------------------

* Shutdown/Exit
* GoOnline/Online/On
* GoOffline/Offline/Off
* AddChatParticipant/Add
* RemoveChatParticipant/Remove/Kill <ActorPath>

All commands are case insensitive.
