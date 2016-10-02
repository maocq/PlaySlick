package services

import javax.inject.{Inject, Singleton}

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}


@Singleton
class ManagerSockets @Inject()(system: ActorSystem){
  lazy val actor = system.actorOf(ManagerActors.props, "managerSockets")
  def apply() = actor
}

/**
  * ManagerActors
  */
object ManagerActors {
  def props = Props[ManagerActors]
}
class ManagerActors extends Actor {

  var users = Set[ActorRef]()

  def receive = {
    case SubscribeActor => {
      users += sender
      context watch sender
    }
    case msg: String =>
      users map { user =>
        user ! MessageSocket(msg)
      }
    case Terminated(user) =>
      users -= user
  }
}

/**
  * Mensajes
  */
object SubscribeActor
case class MessageSocket(msg: String)

