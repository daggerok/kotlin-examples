package daggerok

import org.slf4j.LoggerFactory
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

private val log = LoggerFactory.getLogger("Main")

class SocketClient {
  companion object {
    fun request(payload: String = "hello"): String {
      Socket("127.0.0.1", 8080).use { sock ->
        sock.getOutputStream().use {
          DataOutputStream(it).use {
            it.writeUTF(payload)
            DataInputStream(sock.getInputStream()).use {
              val response = it.readUTF()
              log.debug("received response: $response")
              return response
            }
          }
        }
      }
    }
  }
}

fun main(args: Array<String>) {
  SocketServer.start()
  val payload = "o-la-la!"
  log.info("sending request with payload: '$payload'")
  val response = SocketClient.request(payload)
  log.info("received '$response'")
  SocketServer.stop()
}
