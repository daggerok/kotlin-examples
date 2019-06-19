package daggerok

import daggerok.NettyEchoClient.Companion.log

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

fun Map<String, List<String>>.getOr(key: String, defaultValue: String) =
  this.getOrDefault(key, listOf(defaultValue))
      .first()

fun Map<String, List<String>>.getOr(key: String, defaultValue: Int) =
  this.getOrDefault(key, listOf(defaultValue.toString()))
      .map { it.toInt() }
      .first()

fun main(args: Array<String>) {

  val props = args.toProps()
  val request = props.getOr("request", "help")

  if ("help" == request) {
    log.info("""parameter request is required:
      /path/to/netty-client --request=Hello!
      """.trimIndent())
    System.exit(0)
  }

  val port = props.getOr("port", 8080)
  val host = props.getOr("host", "localhost")

  NettyEchoClient(host = host, port = port)
      .send(request)
}
