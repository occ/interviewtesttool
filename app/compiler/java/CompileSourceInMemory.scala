package compiler.java

import javax.tools.{Diagnostic, JavaFileObject, DiagnosticCollector, ToolProvider}
import java.io.{PrintWriter, StringWriter}
import java.util
import org.apache.commons.lang3.StringUtils
import compiler.CompilerError

object CompileJavaSourceInMemory {
  def compile(className: String, code: String) = {
    val writer = new StringWriter
    val out = new PrintWriter(writer)
    out.println(code)
    out.close()

    val file = new CharSequenceJavaFileObject(className, code)
    val compilationUnits = util.Arrays.asList(file)

    val compiler = ToolProvider.getSystemJavaCompiler
    val fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null))

    val diagnostics = new DiagnosticCollector[JavaFileObject]
    val task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits)

    val success = task.call
    val compilerIssues = diagnostics.getDiagnostics.toArray(new Array[Diagnostic[JavaFileObject]](0))

    val errors = for (issue <- compilerIssues)
      yield CompilerError(
        issue.getCode,
        issue.getKind.toString,
        StringUtils.countMatches(code.substring(0, issue.getPosition.toInt), "\n"),
        issue.getPosition,
        issue.getStartPosition, issue.getEndPosition,
        issue.getMessage(null)
      )

    (success, errors)
  }
}
