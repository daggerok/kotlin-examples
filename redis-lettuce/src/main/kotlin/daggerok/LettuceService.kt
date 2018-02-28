package daggerok

import com.google.gson.Gson
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import java.util.*
import java.util.concurrent.atomic.AtomicLong

data class MyString(val id: UUID = UUID.randomUUID(), val payload: String = "")

private val gson = Gson()
fun MyString.serialize(): String = gson.toJson(this)
fun String.deserialize(): MyString = gson.fromJson(this, MyString::class.java)

class LettuceService {
  companion object {

    private val log = LoggerFactory.getLogger(LettuceService::class.java)

    fun subscribe() {

      val reactiveCommands = RedisClient.getCommands()
      val id = UUID.randomUUID()
      val jsonFlux = Flux.generate<String, AtomicLong>(

          { AtomicLong() },

          { state: AtomicLong, sink ->

            val i = state.getAndIncrement()
            val message = "3 x " + i + " = " + 3 * i
            sink.next(message)
            reactiveCommands.set(id.toString(), MyString(id, payload = "$i: $message").serialize())
                .block()
            reactiveCommands.get(id.toString()).subscribe({
              log.info("{}", it.deserialize())
            })
            if (i > 10) sink.complete()
            Thread.sleep(111)
            state
          },

          { state -> log.info("consuming last state: $state") }
      )

      jsonFlux.subscribe()

      RedisClient.shutdown()
    }
  }
}
