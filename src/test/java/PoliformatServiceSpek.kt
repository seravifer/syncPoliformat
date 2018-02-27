import data.network.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

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
            val call = poliformat.resources(subjectId)
            val response = call.execute()
            it ("should be a successful connection") {
                assert(response.isSuccessful) {
                    "Http error on login. Code: ${response.code()} (${response.errorBody()?.string()})"
                }
            }

            it ("should contain folder or files") {
                assert(response.body()?.collection?.isNotEmpty() == true) {
                    "The psw was expected to contain files or folders but it was empty"
                }
            }
        }
    }
})