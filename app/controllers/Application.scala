package controllers

//import play.api._
import compiler.CompileSourceInMemory
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.json._
import java.util.UUID

object Application extends Controller {

  val codeForm = Form(
      "code" -> text
  )

  def index = Action { implicit request =>
    val uid = session.get("uid") match {
      case None => UUID.randomUUID.toString
      case u: Some[String] => u.get
    }

    Ok(views.html.index("Test." + uid)).withSession(
      session + ("uid" -> uid)
    )
  }

  def compile = Action { implicit request =>
    val code = codeForm.bindFromRequest.get

    val (success, errors) = CompileSourceInMemory.compile(code)

    val resultObj = Json.obj(
      "status" -> (if (success) true else false),
      "errors" -> errors
    )
    Ok(resultObj)
  }
  
}