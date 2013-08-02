package questions.divisibility

import interfaces.Question
import org.junit.Assert._

class Divisibility extends Question{
  val id: String = "000-Divisibility"

  val description: String = "Write a method that returns an integer array of integers between 1 and 10000 that are divisible by 3 or 5"

  val title: String = "Divisibility"

  val mainClass: String = "Divisibility"

  val template: String =
    """
      |public class Divisibility {
      |  public static Integer[] findNumbers() {
      |    // Implement this method
      |    return null;
      |  }
      |}
      """.stripMargin.trim

  val tests: Array[Any => Unit] = Array({
    c => {
      val r = getResults(c)
      val result = (for (i <- 3 to 10000 if i % 3 == 0 || i % 5 == 0) yield i.asInstanceOf[Integer]).toArray

      assert(r.deep == result.deep)
    }
  })

  def getResults(o: Any) = {
    val c = o.getClass
    val m = c.getMethod("findNumbers")
    assert(m != null, "Method findNumbers() was not found")
    assert(m.getReturnType == classOf[Array[Integer]], "Method findNumbers() doesn't return int[]")

    m.invoke(o).asInstanceOf[Array[Integer]]
  }
}
