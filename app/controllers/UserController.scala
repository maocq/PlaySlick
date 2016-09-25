package controllers

import javax.inject.{Inject, Singleton}

import models.User
import play.api.mvc.Controller
import play.api.mvc._
import dal.UserRepository
import play.api.Logger
import play.api.libs.json._
import utils.JsonFormat._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserController @Inject()(User: UserRepository) extends Controller {

  def all = Action.async {
    User.list map { user =>
      Ok(Json.toJson(user))
    }
  }

  def get(id: Long) = Action.async {
    User.get(id) map { user =>
      Ok(Json.toJson(user))
    }
  }

  def add = Action.async(parse.json) { request =>
    val userResult = request.body.validate[User]
    userResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" ->"ERROR", "message" -> JsError.toJson(errors))))
      },
      user => {
        Logger.info("Insertando ...")
        User.add(user) map { id =>
          Created(Json.obj("status" ->"OK", "message" -> ("User '"+ user.email +"' saved.") ))
        }
      }
    )
  }

  def edit(id: Long) = Action.async(parse.json) { request =>
    val userResult = request.body.validate[User]
    userResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" ->"ERROR", "message" -> JsError.toJson(errors))))
      },
      user => {
        User.edit(user) map { res =>
          Ok(Json.obj("status" ->"OK", "message" -> ("User '"+ user.email +"' edited.") ))
        }
      }
    )
  }

  def remove(id: Long) = Action.async { implicit request =>
    User.delete(id) map { result =>
      Ok(Json.obj("status" ->"OK", "message" -> ("User deleted.") ))
    }
  }

}