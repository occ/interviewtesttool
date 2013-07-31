package compiler

import play.api.libs.json.Json

case class CompilerError(code: String,
                         kind: String,
                         line: Long,
                         position: Long,
                         startPosition: Long,
                         endPosition: Long,
                         message: String)

object CompilerError {
  implicit val compilerErrorReads = Json.reads[CompilerError]
  implicit val compilerErrorWrites = Json.writes[CompilerError]
}