package daggerok

import kotlinx.coroutines.experimental.async
import org.slf4j.LoggerFactory
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.EOFException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.TimeUnit

class SocketServer {
  companion object {

    private val log = LoggerFactory.getLogger(SocketServer::class.java)
    private const val port = 8080

    private val server: ServerSocket by lazy {
      ServerSocket(port)
    }

    private val clients = mutableListOf<Socket>()

    fun start() {

      log.debug("starting...")

      async {
        while (true) {

          val client = server.accept()

          clients.add(client)
          log.debug("handling client request...")

          Thread({
            try {
              client.use { sock ->
                sock.getInputStream().use {
                  // security; validateRequest...
                  val request = DataInputStream(it).readUTF()
                  log.debug("requested data: $request")
                  DataOutputStream(sock.getOutputStream()).use {
                    it.writeUTF("ehlo: ${request.reversed()}")
                  }
                }
              }
            } catch (eof: EOFException) {
              log.debug("client closed connection.")
            }
          }).start().also {
            // cleanup client requested resources...
            clients.remove(client)
          }
        }
      }.apply {
        TimeUnit.SECONDS.sleep(1)
        log.info("listening port {}", port)
      }
    }

    fun stop() {

      log.info("shutdown....")

      clients
          .filter { it.isConnected }
          .filterNot { it.isClosed }
          .filterNot { it.isInputShutdown }
          .filterNot { it.isOutputShutdown }
          .forEach { it.close() }

      log.debug("server stopped.")
    }
  }
}
