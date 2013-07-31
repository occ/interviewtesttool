package compiler

import javax.tools.SimpleJavaFileObject
import java.net.URI
import javax.tools.JavaFileObject.Kind

class JavaSourceFromString(val name: String, val code: String) extends
  SimpleJavaFileObject(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE) {

  override def getCharContent(ignoreEncodingErrors: Boolean) = code
}
