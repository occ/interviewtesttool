package compiler

import play.api.libs.json.Json

case class TestIssue(exceptionType: String, message: String)

object TestIssue {
  implicit val compilerErrorReads = Json.reads[TestIssue]
  implicit val compilerErrorWrites = Json.writes[TestIssue]
}