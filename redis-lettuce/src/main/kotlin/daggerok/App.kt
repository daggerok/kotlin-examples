package daggerok

class App {

  companion object {

    @JvmStatic
    fun main(args: Array<String>) {

      EmbeddedRedis.start()
      LettuceService.subscribe()
      EmbeddedRedis.stop()
    }
  }
}
