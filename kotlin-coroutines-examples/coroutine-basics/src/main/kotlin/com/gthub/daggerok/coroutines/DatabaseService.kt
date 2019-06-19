package com.gthub.daggerok.coroutines

import kotlinx.coroutines.delay
import java.util.*

data class DatabaseResponse(val result: String)
data class DatabaseRequest(val data: String, val token: UUID)

fun none(token: UUID) =
  DatabaseResponse("Sorry, but $token token is invalid")

class DatabaseService(val maxDelay: Number = 0,
                      private val db: MutableList<DatabaseRequest> = mutableListOf()) {
  private val random = Random()
  private suspend fun sleep(delay: Number = maxDelay) = delay(random.nextInt(delay.toInt()).toLong())
  suspend fun saveData(payload: DatabaseRequest, withDelay: Number = maxDelay): DatabaseResponse {
    sleep(withDelay)
    if (payload.token != defaultUuid) return none(payload.token)
    db.add(payload)
    return DatabaseResponse("request data '${payload.data}' was saved")
  }
  fun db() = db.toList()
}
