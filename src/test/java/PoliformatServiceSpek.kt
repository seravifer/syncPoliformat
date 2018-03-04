import data.network.*
import domain.ContentEntity
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import retrofit2.HttpException

object PoliformatServiceSpek : Spek({
    given("a UPV Service") {
        val intranet : UpvService = Intranet
        on("login") {
            val dni = System.getProperty("dni") ?: System.getenv("dni")
            val clau = System.getProperty("clau") ?: System.getenv("clau")

            it ("should do the request with a valid dni") {
                assert(dni != null) { "The DNI was null" }
            }

            it ("should do the request with a valid password") {
                assert(clau != null) { "The clau was null" }
            }

            val call = intranet.login(mapOf(
                    "cua" to "sakai",
                    "estilo" to "500",
                    "id" to "c",
                    "vista" to "MSE",
                    "dni" to dni,
                    "clau" to clau
            ))
            val response = call.execute()

            it("should be a successful connection") {
                assert(response.isSuccessful) {
                    "Http error on login. Code: ${response.code()} (${response.errorBody()?.string() ?: ""})"
                }
            }

            it("should save a token cookie") {
                val cookies = NonPersistentCookieJar.loadForRequest(call.request().url())
                assert(cookies.any { it.name() == "TDp" })
            }
        }

        val poliformat: PoliformatService = Poliformat
        on ("subject resources request") {
            val subjectId = System.getProperty("subject.id") ?: System.getenv("subject.id") ?: "GRA_11571_2017"
            val promise = poliformat.resources(subjectId)
            val result: ContentEntity? = try { promise.join() } catch (e: Exception) { null }
            it ("should be a successful connection") {
                assert(promise.isDone && !promise.isCompletedExceptionally) {
                    try {
                        promise.join() // It's is going to throw always at this point
                        "Unreachable"
                    } catch (e: HttpException) {
                        "Http error on login. Code: ${e.code()} (${e.message()})"
                    } catch (e: Exception) {
                        "Unknown exception on connection: ${e.message}"
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