package daggerok

import kotlinx.coroutines.experimental.async
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.*
import java.util.concurrent.TimeUnit

class SocketServer {
  companion object {

    private val log = LoggerFactory.getLogger(SocketServer::class.java)

    private const val port = 8080
    private var ready = false

    private val server: ServerSocket by lazy {
      val result = ServerSocket(port)
      ready = true
      return@lazy result
    }

    private val clients = mutableListOf<Socket>()
    private val db = mutableListOf<String>()

    fun start() {

      log.info("starting server...")

      val deferred = async {
        while (true) {

          val clientSocket = server.accept()

          clients.add(clientSocket)
          log.info("client connected")

          Thread({
            ready = false
            val baos = ByteArrayOutputStream()
            clientSocket.getInputStream().use {
              var container = ByteArray(1024)
              val readBytes = it.read(container)
              if (readBytes != -1)
                baos.write(container, 0, readBytes)
            }
            log.info(baos.toString(UTF_8.name()))
//            val output = PrintWriter(clientSocket.getOutputStream(), true)
//            val input = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
//            val request = input.readLine()
//            output.println(if (request.contains("ping")) "pong" else "hello")
            clientSocket.close()
            clients.remove(clientSocket)
            ready = true
          }).start()
        }
      }

      deferred.apply {
        TimeUnit.SECONDS.sleep(1)
        log.info("server is listening port {}", port)
      }
    }

    fun ready() = ready

    fun stop() {

      log.info("shutdown server....")

      clients
          .filter { it.isConnected }
          .filterNot { it.isClosed }
          .filterNot { it.isInputShutdown }
          .filterNot { it.isOutputShutdown }
          .forEach { it.close() }

      log.info("server stopped.")
    }

    fun waitForSecond() {
      while (!ready())
        TimeUnit.SECONDS.sleep(1)
    }
  }
}
