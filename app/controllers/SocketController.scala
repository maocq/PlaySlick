package controllers

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket
import services.{ManagerSockets, MessageSocket, SubscribeActor}


@Singleton
class SocketController @Inject()(managerSockets: ManagerSockets)(implicit system: ActorSystem, materializer: Materializer) {
  import akka.actor._

  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => WebSocketActor.props(out))
  }

  /**
    * Websocket
    */
  object WebSocketActor {
    def props(out: ActorRef) = Props(new WebSocketActor(out))
  }
  class WebSocketActor(out:ActorRef) extends Actor {
    override def preStart() = {
      managerSockets() ! SubscribeActor
    }
    def receive = {
      case x: String =>
        managerSockets() ! x
      case m:MessageSocket =>
        out ! m.msg
    }
  }

}
