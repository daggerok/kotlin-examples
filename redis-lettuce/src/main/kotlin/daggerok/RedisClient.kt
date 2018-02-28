package daggerok

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisReactiveCommandsImpl
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.reactive.RedisReactiveCommands
import java.time.Duration

private class ConnectionFactory {
  companion object {
    fun createConnection(port: Int = 6379, host: String = "127.0.0.1"): RedisClient =
        RedisClient.create(RedisURI.builder()
            .withTimeout(Duration.ofMinutes(1))
            .withHost(host)
            .withPort(port)
            .build())
  }
}

class RedisClient {

  companion object {

    private val client: RedisClient by lazy {
      ConnectionFactory.createConnection()
    }

    private val connection: StatefulRedisConnection<String, String> by lazy {
      client.connectPubSub()
    }

    //val connection = client.connect() as StatefulRedisConnection<String, String>
    fun getCommands(): RedisReactiveCommands<String, String> = connection.reactive()

    fun shutdown() = client.shutdown(Duration.ofSeconds(1), Duration.ofSeconds(1))
  }
}
