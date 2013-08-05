package security

import play.api.mvc._
import play.api.mvc.Security.Authenticated
import controllers.routes

trait Security {
  def username(request: RequestHeader) = request.session.get("email")

  def Unauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)

  def AuthenticatedAction(f: => String => Request[AnyContent] => Result) = {
    Authenticated(username, Unauthorized) {
      user =>
        Action(request => f(user)(request))
    }
  }
}
