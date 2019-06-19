package daggerok.test

import kotlinx.coroutines.experimental.async
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.StandardCharsets.UTF_8

// single threaded
class TestSingleThreadedServer {
  companion object {
    @JvmStatic
    fun main(args: Array<String>): Unit = ServerSocket(8080).use { server ->

      val request = server.accept()

      request.getInputStream().use { i ->

        val requestData = ByteArray(1024)
        i.read(requestData)

        val inputData = String(requestData)
        println("requested data: $inputData")

        request.getOutputStream().use { o ->

          val outputData = inputData.toUpperCase()
          o.write(outputData.toByteArray(UTF_8))
        }
      }
    }
  }
}

// single threaded
class TestMultiThreadedServer {
  companion object {
    @JvmStatic
    fun main(args: Array<String>): Unit = ServerSocket(8080).use { server ->

      while (true) {
        val request = server.accept()

        async {
          request.getInputStream().use { i ->

            val requestData = ByteArray(1024)
            i.read(requestData)

            val inputData = String(requestData)
            println("requested data: $inputData")

            request.getOutputStream().use { o ->

              val outputData = inputData.toUpperCase()
              o.write(outputData.toByteArray(UTF_8))
            }
          }
        }
      }
    }
  }
}

class TestClient {

  companion object {

    private fun sendMessage(message: String) = Socket("127.0.0.1", 8080).use { socket ->

      socket.getOutputStream().use { o ->
        val requestData = message.toByteArray(UTF_8)
        o.write(requestData)

        socket.getInputStream().use { i ->
          val responseData = ByteArray(1024)
          i.read(responseData)

          val response = String(responseData)
          println("responded data: $response")
        }
      }
    }

    @JvmStatic
    fun main(args: Array<String>) {
      sendMessage("hello!")
    }
  }
}
