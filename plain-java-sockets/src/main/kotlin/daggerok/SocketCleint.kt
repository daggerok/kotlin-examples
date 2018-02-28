package daggerok

import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.net.Socket
import java.nio.charset.StandardCharsets.UTF_8

class SocketCleint {
  companion object {

    private val log = LoggerFactory.getLogger(SocketCleint::class.java)
    private const val port = 8080

    private val client: Socket by lazy {
      Socket("127.0.0.1", port)
    }

    private fun subscribe(): String {
      val baos = ByteArrayOutputStream()
      client.getInputStream().use {
        var container = ByteArray(1024)
        val readBytes = it.read(container)
        if (readBytes != -1)
          baos.write(container, 0, readBytes)
      }
      return baos.toString(UTF_8.name())
    }

    fun publish(data: String = "hello") {

      val client: Socket by lazy {
        Socket("127.0.0.1", port)
      }

/*
      val client = client.getOutputStream()
      client.write(data.toByteArray())
      client.flush()
*/
      client.getOutputStream().use {
        it.write(data.toByteArray())
      }
    }
  }
}
