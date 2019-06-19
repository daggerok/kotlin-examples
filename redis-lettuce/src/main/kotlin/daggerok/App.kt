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
  EmbeddedRedis.start()
  LettuceService.subscribe()
  EmbeddedRedis.stop()
}
