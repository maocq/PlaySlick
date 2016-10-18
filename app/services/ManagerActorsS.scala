package services

import javax.inject.{Inject, Singleton}

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import play.api.libs.json.JsValue


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

  var users =  Map[ActorRef, String]()

  def receive = {
    case a: SubscribeActor =>
      users += sender -> a.user
      context watch sender
    case m: MessageManager =>
      users.foreach {
        case (socket, user) =>
          socket ! MessageSocket(m.event, m.message, m.from)
      }
    case Terminated(user) =>
      users -= user
  }
}

/**
  * Messages
  */
case class SubscribeActor(user: String)
case class MessageManager(event: Option[String], message: Option[JsValue], from: Option[String], to: Option[String])
case class MessageSocket(event: Option[String], message: Option[JsValue], from: Option[String])