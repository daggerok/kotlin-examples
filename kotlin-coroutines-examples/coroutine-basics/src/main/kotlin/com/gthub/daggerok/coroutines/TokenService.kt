package com.gthub.daggerok.coroutines

import kotlinx.coroutines.delay
import java.util.*

data class Token(val payload: UUID = UUID.randomUUID())

val defaultUuid = UUID.fromString("000000000000-0000-0000-0000-00000000")!!

class TokenService(val maxDelay: Number = 0) {
  private val random = Random()
  private suspend fun sleep(delay: Number = maxDelay) = delay(random.nextInt(delay.toInt()).toLong())
  suspend fun getToken(withDelay: Number = maxDelay): Token {
    sleep(withDelay)
    val uuid = if (System.currentTimeMillis() % 2 == 0L) UUID.randomUUID() else defaultUuid
    return Token(uuid)
  }
}
