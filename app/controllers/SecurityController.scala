package controllers

import play.api.mvc.{Action, Controller}
import java.util.UUID
import play.api.data._
import play.api.data.Forms._

object SecurityController extends Controller {

  val loginForm = Form(
    "email" -> nonEmptyText(5)
  )

  def showLoginForm = Action {
    implicit request =>
      session.get("email") match {
        // Already logged in?
        case _: Some[String] => Redirect(routes.Application.editor())
        case None => Ok(views.html.login())
      }
  }

  def login = Action {
    implicit request =>
      if (session.get("email") != None) {
        Redirect(routes.Application.editor())
      }
      else {
        loginForm.bindFromRequest.fold(
          formWithErrors => Ok(views.html.login()).flashing("login" -> "Please enter your email address"),
          value => Redirect(routes.Application.editor()).withSession("email" -> value)
        )
      }
  }
}
