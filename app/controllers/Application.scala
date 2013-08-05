package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.json._
import java.util.UUID
import compiler.java.JavaCompiler
import compiler.TestIssue
import security.Security

object Application extends Controller with Security {

  val codeForm = Form(
      "code" -> text
  )

  def editor = AuthenticatedAction { username => implicit request =>
    val uid = session.get("uid") match {
      case None => UUID.randomUUID.toString
      case u: Some[String] => u.get
    }

    val questionId = "000-Divisibility"
    val question = new questions.divisibility.Divisibility

    Ok(views.html.editor(question.title, question.description, question.template)).withSession(
      session + ("uid" -> uid) + ("question", questionId)
    )
  }

  def compile = Action { implicit request =>
    val code = codeForm.bindFromRequest.get

    val question = new questions.divisibility.Divisibility

    val (success, issues, obj) = JavaCompiler.compile(question.mainClass, code)

    val resultObj = Json.obj(
      "success" -> (if (success) true else false),
      "issues" -> issues
    )
    Ok(resultObj)
  }

  def test = Action { implicit request =>
    val code = codeForm.bindFromRequest.get

    val question = new questions.divisibility.Divisibility

    val (success, issues, obj) = JavaCompiler.compile(question.mainClass, code)

    if (!success) {
      Ok(Json.obj(
        "success" -> (if (success) true else false),
        "issues" -> issues
      ))
    } else {
      try {
        question.test(obj)
        Ok(Json.obj("success" -> true))
      } catch {
        case e: Throwable => Ok(Json.obj(
          "success" -> false,
          "issues" -> Array[TestIssue](TestIssue(e.getClass.getName, e.getMessage))
        ))
      }
    }
  }
}