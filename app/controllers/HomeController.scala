package controllers

import javax.inject._

import actions.LoggingAction
import play.api._
import dal.UserRepository
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(User: UserRepository)(val messagesApi: MessagesApi) extends Controller with I18nSupport{

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def template = Action.async {
    User.list map { users =>
      val title = Messages("home.users")
      Ok(views.html.users(title, users))
    }
  }

  def action = LoggingAction {
    Ok(views.html.index("Action composition"))
  }

  def redirect = Action {
    Redirect(routes.HomeController.index)
  }
}
