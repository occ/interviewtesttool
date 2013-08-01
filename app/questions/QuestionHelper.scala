package questions

import org.reflections.Reflections
import interfaces.Question
import scala.collection.JavaConverters._

object QuestionHelper {
  def FindQuestion(id: String): Option[Question] = {
    val reflections = new Reflections("questions")
    val questions= reflections.getSubTypesOf[Question](classOf[Question]).asScala
    val question = for (q <- questions if q.getField("id").get(null).asInstanceOf[String] == id) yield q

    question.headOption.map(q => q.newInstance)
  }
}
