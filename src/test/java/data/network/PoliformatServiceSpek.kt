package data.network

import appComponent
import domain.ContentEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.kodein.di.direct
import org.kodein.di.generic.instance
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import retrofit2.HttpException

object PoliformatServiceSpek : Spek({
    describe("a UPV Service") {
        val intranet : UpvService = appComponent.direct.instance()
        describe("login") {
            val dni = System.getProperty("dni") ?: System.getenv("dni")
            val clau = System.getProperty("clau") ?: System.getenv("clau")

            it ("should do the request with a valid dni") {
                assert(dni != null) { "The DNI was null" }
            }

            it ("should do the request with a valid password") {
                assert(clau != null) { "The clau was null" }
            }

            val response = runBlocking { intranet.login(dni, clau) }

            it("should be a successful connection") {
                assert(response.isSuccessful) {
                    "Http error on login. Code: ${response.code()} (${response.errorBody()?.string() ?: ""})"
                }
            }

            /*it("should save a token cookie") {
                val cookies = CookieJarImpl.loadForRequest(call.request().url())
                assert(cookies.any { it.name() == "TDp" })
            }*/
        }

        val poliformat: PoliformatService = appComponent.direct.instance()
        describe ("subject resources request") {
            val subjectId = System.getProperty("subject.id") ?: System.getenv("subject.id") ?: "GRA_11571_2017"
            val promise = GlobalScope.async {
                poliformat.resources(subjectId)
            }
            val result: ContentEntity? = try { runBlocking { promise.await() } } catch (e: Exception) { null }
            it ("should be a successful connection") {
                assert(promise.isCompleted && promise.getCompletionExceptionOrNull() === null) {
                    when (val e = promise.getCompletionExceptionOrNull()) {
                        is HttpException -> "Http error on login. Code: ${e.code()} (${e.message()})"
                        is Exception -> "Unknown exception on connection: ${e.message}"
                        else -> "Unreachable"
                    }
                }
            }

            it ("should contain folder or files") {
                assert(result?.collection?.isNotEmpty() == true) {
                    "The psw was expected to contain files or folders but it was empty"
                }
            }
        }
    }
})
