package controllers

//import play.api._
import compiler.CompileSourceInMemory
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.json._
import java.util.UUID
import questions.QuestionHelper

object Application extends Controller {

  val codeForm = Form(
      "code" -> text
  )

  def index = Action { implicit request =>
    val uid = session.get("uid") match {
      case None => UUID.randomUUID.toString
      case u: Some[String] => u.get
    }

    val questionId = "000-Divisibility"
    val question = new questions.divisibility.Divisibility

    Ok(views.html.index(question.title, question.description, question.template)).withSession(
      session + ("uid" -> uid) + ("question", questionId)
    )
  }

  def compile = Action { implicit request =>
    val code = codeForm.bindFromRequest.get

    val question = new questions.divisibility.Divisibility

    val (success, errors) = CompileSourceInMemory.compile(question.mainClass, code)

    val resultObj = Json.obj(
      "status" -> (if (success) true else false),
      "errors" -> errors
    )
    Ok(resultObj)
  }
  
}