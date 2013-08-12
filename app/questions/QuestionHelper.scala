package questions

import interfaces.Question
import questions.divisibility.Divisibility

object QuestionHelper {
  // FIXME: Nicer, reflection based way to do this?
  val questions = List(Divisibility)

  def getQuestion(id: String): Option[Question] = questions.find(_.id == id)
}
