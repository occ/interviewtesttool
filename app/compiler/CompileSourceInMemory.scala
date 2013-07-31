package compiler

import javax.tools.{Diagnostic, JavaFileObject, DiagnosticCollector, ToolProvider}
import java.io.{PrintWriter, StringWriter}
import java.util
import org.apache.commons.lang3.StringUtils

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

    var errors = List[CompilerError]()
    for (diagnostic <- diagnostics.getDiagnostics.toArray[Diagnostic[JavaFileObject]](new Array[Diagnostic[JavaFileObject]](0))) {
      val line = StringUtils.countMatches(code.substring(0, diagnostic.getPosition.toInt), "\n")
      errors = CompilerError(
        diagnostic.getCode, diagnostic.getKind.toString, line, diagnostic.getPosition, diagnostic.getStartPosition,
        diagnostic.getEndPosition, diagnostic.getMessage(null)
      ) :: errors
    }

    (success, errors)
  }
}
