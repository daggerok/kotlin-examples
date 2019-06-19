package daggerok

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
  SocketServer.waitForSecond()
  SocketClient.publish("ping")
  SocketClient.publish("hola!")
  SocketServer.waitForSecond()
  SocketServer.stop()
}
