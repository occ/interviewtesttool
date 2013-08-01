package interfaces

trait Question {
  val title: String
  val description: String
  val id: String
  val mainClass: String
  val template: String

  val tests: Array[Class[_] => Unit]
}
