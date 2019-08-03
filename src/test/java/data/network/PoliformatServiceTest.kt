package data.network

import domain.ContentEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.kodein.di.direct
import org.kodein.di.generic.instance
import retrofit2.HttpException
import syncPoliformat.appComponent

class PoliformatServiceTest {
    val dni = System.getProperty("dni") ?: System.getenv("dni")
    val clau = System.getProperty("clau") ?: System.getenv("clau")

    @Test
    fun `should login successfully`() {
        assert(dni != null) { "The DNI was null" }
        assert(clau != null) { "The clau was null" }

        val intranet : UpvService = appComponent.direct.instance()
        val response = runBlocking { intranet.login(dni, clau) }
        assert(response.isSuccessful) {
            "Http error on login. Code: ${response.code()} (${response.errorBody()?.string() ?: ""})"
        }
    }

    @Test
    fun `should request a subject`() {
        val poliformat: PoliformatService = appComponent.direct.instance()

        val subjectId = System.getProperty("subject.id") ?: System.getenv("subject.id") ?: "GRA_11571_2017"
        val promise = GlobalScope.async {
            poliformat.resources(subjectId)
        }
        val result: ContentEntity? = try {
            runBlocking { promise.await() }
        } catch (e: Exception) { null }

        assert(promise.isCompleted && promise.getCompletionExceptionOrNull() === null) {
            when (val e = promise.getCompletionExceptionOrNull()) {
                is HttpException -> "Http error on login. Code: ${e.code()} (${e.message()})"
                is Exception -> "Unknown exception on connection: ${e.message}"
                else -> "Unreachable"
            }
        }
        assert(result?.collection?.isNotEmpty() == true) {
            "The psw was expected to contain files or folders but it was empty"
        }
    }
}
