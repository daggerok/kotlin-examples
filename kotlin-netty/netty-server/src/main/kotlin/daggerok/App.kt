package daggerok

import daggerok.NettyEchoServer.Companion.log

/**
 * Mapping args to properties map.
 */
fun Array<String>.toProps() = this
    .toList()
    .flatMap { it.split("\\s+".toRegex()) }
    .filter { it.startsWith("--") }
    .filter { it.contains("=") }
    .map { it.split("=") }
    .map { (it[0] to it[1]) }
    .map {

      val (prefix, value) = it
      val key = prefix.substring(2)
      val values = value.split(",", ", ", ";", "; ")

      (key to values)
    }
    .toMap()

fun Map<String, List<String>>.getOr(key: String, defaultValue: Int) =
  this.getOrDefault(key, listOf(defaultValue.toString()))
      .map { it.toInt() }
      .first()

fun main(args: Array<String>) {

  val props = args.toProps()
  val port = props.getOr("port", 8080)

  log.info("bootstrap server on port $port")
  NettyEchoServer(port).start()
}
