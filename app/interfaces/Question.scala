package interfaces

trait Question {
  val title: String
  val description: String
  val id: String
  val mainClass: String
  val template: String

  val tests: Array[Any => Unit]

  def test(obj: Any) = tests.foreach(test => test(obj))
}
