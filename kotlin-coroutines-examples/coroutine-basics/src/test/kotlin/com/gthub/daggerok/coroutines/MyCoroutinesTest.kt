package com.gthub.daggerok.coroutines

/*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
*/
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test
import java.util.*

internal class MyCoroutinesTest {

/*
  @ExperimentalCoroutinesApi
  private val testThreadSurrogate = newSingleThreadContext("UI thread")

  @BeforeEach internal fun before() = Dispatchers.setMain(testThreadSurrogate)

  @AfterEach internal fun after() {
    Dispatchers.resetMain()
    testThreadSurrogate.close()
  }
*/

  @Test
  internal fun test() {
    val delay = 3000L
    val tokenService = TokenService()
    val databaseService = DatabaseService()

    for (i in 0..delay) {
      GlobalScope.launch {
        val token = tokenService.getToken(i)
        println("$i token: $token")

        val request = DatabaseRequest("ololo trololo " + UUID.randomUUID(), token.payload)
        println("$i request: $request")

        val response = databaseService.saveData(request, i)
        println("$i response: $response")
      }
    }
    Thread.sleep(2 * delay)
    println("\n\t${databaseService.db().size} items in database.")
  }
}
