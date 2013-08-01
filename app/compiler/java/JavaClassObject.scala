package compiler.java

import javax.tools.{JavaFileObject, SimpleJavaFileObject}
import java.net.URI
import java.io.{ByteArrayOutputStream, OutputStream}


class JavaClassObject(name: String, kind: JavaFileObject.Kind)
  extends SimpleJavaFileObject(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind) {
  /**
   * Will be used by our file manager to get the byte code that
   * can be put into memory to instantiate our class
   *
   * @return compiled byte code
   */
  def getBytes: Array[Byte] = bos.toByteArray

  /**
   * Will provide the compiler with an output stream that leads
   * to our byte array. This way the compiler will write everything
   * into the byte array that we will instantiate later
   */
  override def openOutputStream: OutputStream = bos

  /**
   * Byte code created by the compiler will be stored in this
   * ByteArrayOutputStream so that we can later get the
   * byte array out of it
   * and put it in the memory as an instance of our class.
   */
  protected final val bos: ByteArrayOutputStream = new ByteArrayOutputStream
}