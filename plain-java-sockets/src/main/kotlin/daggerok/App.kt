package daggerok

import kotlinx.coroutines.experimental.async
import java.util.concurrent.TimeUnit

/*
class AppKt {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      println("${args.toList()}")
    }
  }
}
*/

fun main(args: Array<String>) {
  SocketServer.start()
  SocketCleint.publish("ping")
  SocketCleint.publish("hola!")
  SocketServer.waitForReady()
  SocketServer.stop()
}
