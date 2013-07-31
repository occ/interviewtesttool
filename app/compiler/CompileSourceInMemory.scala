package compiler

import javax.tools.{Diagnostic, JavaFileObject, DiagnosticCollector, ToolProvider}
import java.io.{PrintWriter, StringWriter}
import java.util
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class InMemoryCompilerError(code: String,
                         kind: String,
                         position: Long,
                         startPosition: Long,
                         endPosition: Long,
                         message: String)
object InMemoryCompilerError {
  implicit val compilerErrorReads = Json.reads[InMemoryCompilerError]
  implicit val compilerErrorWrites = Json.writes[InMemoryCompilerError]
}

object CompileSourceInMemory {
  def compile(code: String) = {
    val compiler = ToolProvider.getSystemJavaCompiler
    val diagnostics: DiagnosticCollector[JavaFileObject] = new DiagnosticCollector[JavaFileObject]

    val writer = new StringWriter
    val out = new PrintWriter(writer)
    out.println(code)
    out.close()

    val file = new JavaSourceFromString("InterviewCode", writer.toString)

    val compilationUnits = util.Arrays.asList(file)
    val task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits)

    val success = task.call

    var errors = List[InMemoryCompilerError]()
    for (diagnostic <- diagnostics.getDiagnostics.toArray[Diagnostic[JavaFileObject]](new Array[Diagnostic[JavaFileObject]](0))) {
      errors = InMemoryCompilerError(
        diagnostic.getCode, diagnostic.getKind.toString, diagnostic.getPosition, diagnostic.getStartPosition,
        diagnostic.getEndPosition, diagnostic.getMessage(null)
      ) :: errors
    }

    (success, errors)
  }
}
