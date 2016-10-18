package controllers

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.json.{JsValue, Json}
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket
import services.{ManagerSockets, MessageManager, MessageSocket, SubscribeActor}

@Singleton
class SocketController @Inject()(managerSockets: ManagerSockets)(implicit system: ActorSystem, materializer: Materializer) {
  import akka.actor._

  def socket(idSocket: Option[String]) = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef(out => WebSocketActor.props(out, idSocket))
  }

  /**
    * Websocket
    */
  object WebSocketActor {
    def props(out: ActorRef, idSocket: Option[String]) = Props(new WebSocketActor(out, idSocket))
  }
  class WebSocketActor(out:ActorRef, idSocket: Option[String]) extends Actor {
    override def preStart() = {
      managerSockets() ! SubscribeActor(idSocket getOrElse "")
    }
    def receive = {
      case json: JsValue =>
        val event = (json \ "event").asOpt[String]
        val message = (json \ "message").asOpt[JsValue]
        val to = (json \ "to").asOpt[String]
        managerSockets() ! MessageManager(event, message, idSocket, to)
      case m:MessageSocket =>
        implicit val messageSocketWrites = Json.writes[MessageSocket]
        out ! Json.toJson(m)
    }
  }

}
