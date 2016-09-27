package controllers

import javax.inject._
import actions.LoggingAction
import play.api._
import play.api.mvc._

@Singleton
class HomeController @Inject() extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def action = LoggingAction {
    Ok(views.html.index("Action composition"))
  }

  def redirect = Action {
    Redirect(routes.HomeController.index)
  }
}
