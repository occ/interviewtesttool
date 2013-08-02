package compiler

import play.api.libs.json.Json

case class CompilerIssue(code: String,
                         kind: String,
                         startLine: Long,
                         startColumn: Long,
                         endLine: Long,
                         endColumn: Long,
                         message: String)

object CompilerIssue {
  implicit val compilerErrorReads = Json.reads[CompilerIssue]
  implicit val compilerErrorWrites = Json.writes[CompilerIssue]
}