package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.json._
import compiler.java.JavaCompiler
import compiler.TestIssue
import security.Security
import questions.QuestionHelper

object Application extends Controller with Security {

  val codeForm = Form(
      "code" -> text
  )

  def currentQuestionId(implicit request: RequestHeader) = {
    session.get("q") match {
      case None => "000-Divisibility"
      case q: Some[String] => q.get
    }
  }

  def currentQuestion(implicit request: RequestHeader) = QuestionHelper.getQuestion(currentQuestionId)

  def editor = AuthenticatedAction { username => implicit request =>
    val question = currentQuestion.get

    Ok(views.html.editor(question)).withSession(
    session + ("q" -> currentQuestionId)
    )
  }

  def compile = Action { implicit request =>
    val code = codeForm.bindFromRequest.get

    val question = currentQuestion.get

    val (success, issues, obj) = JavaCompiler.compile(question.mainClass, code)

    val resultObj = Json.obj(
      "success" -> (if (success) true else false),
      "issues" -> issues
    )
    Ok(resultObj)
  }

  def test = Action { implicit request =>
    val code = codeForm.bindFromRequest.get

    val question = currentQuestion.get

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