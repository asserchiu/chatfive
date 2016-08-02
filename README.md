chatfive
=========================

A simple chatroom(-like) application.

Built with [Scala](http://www.scala-lang.org) and [Akka](http://akka.io).


How to run?
-------------------------

Run `sbt run` for single JVM mode.

or

Run `sbt "run UserConsole"` first, then run `sbt "run ChatManager"` in other console for multi JVM mode.


How to use?
-------------------------

Commit `exec <command>` in the command prompt to execute command.

or

Anything else as a message.


Supported Commands
-------------------------

* `Shutdown`/`Exit`
* `GoOnline`/`Online`/`On`
* `GoOffline`/`Offline`/`Off`
* `AddChatParticipant`/`Add`
* `RemoveChatParticipant <ActorPath>`/`Remove <ActorPath>`/`Kill <ActorPath>`

All commands are case insensitive.
