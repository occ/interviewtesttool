package compiler.java

import javax.tools.{Diagnostic, JavaFileObject, DiagnosticCollector, ToolProvider}
import java.io.{PrintWriter, StringWriter}
import java.util
import org.apache.commons.lang3.StringUtils
import compiler.{CompilerIssue}

object JavaCompiler {
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
    val diagnosticsResults = diagnostics.getDiagnostics.toArray(new Array[Diagnostic[JavaFileObject]](0))

    val issues = diagnosticsResults.map(issue => {
      val startChar = issue.getStartPosition.toInt
      var endChar = issue.getEndPosition.toInt

      if (startChar == endChar) endChar += 1

      val startPosition = offsetToLineColumn(code, startChar)
      val endPosition = offsetToLineColumn(code, endChar)

      CompilerIssue(issue.getCode, issue.getKind.toString,
        startPosition._1, startPosition._2,
        endPosition._1, endPosition._2,
        issue.getMessage(null))
    })

    (success, issues)
  }

  def offsetToLineColumn(code: String, position: Int) = {
    val codeSegment = code.substring(0, position)
    val line = codeSegment.count(_ == '\n')
    val lineBeginning = codeSegment.lastIndexOf('\n')
    val column = position - lineBeginning - 1

    (line, column)
  }
}
