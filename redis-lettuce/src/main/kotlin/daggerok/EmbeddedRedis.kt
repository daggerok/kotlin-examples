package daggerok

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.slf4j.LoggerFactory
import redis.embedded.RedisExecProvider
import redis.embedded.RedisServer
import java.time.Duration

class EmbeddedRedis {

  init {
    println(RedisExecProvider.defaultProvider().get())
  }

  companion object {

    private val log = LoggerFactory.getLogger(EmbeddedRedis::class.java)

    private val redisServer: RedisServer by lazy {
      RedisServer.builder()
          .port(6379)
          .setting("maxmemory 128M")
          .build()
    }

    fun start(): RedisServer {

      log.info("starting server...")

      if (redisServer.isActive)
        return redisServer

      //val redisServer = RedisServer.builder().build()
      redisServer.start()

      log.info("server started.")
      return redisServer
    }

    fun stop() {
      log.info("shutting down...")
      log.info("is active before: {}", redisServer.isActive)
      redisServer.stop()
      log.info("is active after: {}", redisServer.isActive)
      Runtime.getRuntime().exit(0)
    }
  }
}
