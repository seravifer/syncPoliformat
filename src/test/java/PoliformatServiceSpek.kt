import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import model.json.adapter.CleanAdapter
import model.json.adapter.ContentAdapter
import model.json.adapter.PosixDateAdapter
import model.json.adapter.UrlAdapter
import okhttp3.OkHttpClient
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object PoliformatServiceSpek : Spek({
    given("a UpvService") {
        val httpClient = OkHttpClient.Builder()
                .cookieJar(NonPersistentCookieJar)
                .build()
        val upvRetrofit = Retrofit.Builder()
                .baseUrl("https://www.upv.es/")
                .client(httpClient)
                .build()
        val upvService = upvRetrofit.create(UpvService::class.java)

        on("login") {
            val dni = System.getProperty("dni") ?: System.getenv("dni")
            val clau = System.getProperty("clau") ?: System.getenv("clau")

            it ("should do the request with a valid dni") {
                assert(dni != null) { "The DNI was null" }
            }

            it ("should do the request with a valid password") {
                assert(clau != null) { "The clau was null" }
            }

            val call = upvService.login(mapOf(
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

        val moshiParser = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(ContentAdapter())
                .add(UrlAdapter())
                .add(CleanAdapter())
                .add(PosixDateAdapter())
                .build()
        val poliformatRetrofit = Retrofit.Builder()
                .baseUrl("https://poliformat.upv.es/")
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshiParser))
                .build()
        val poliformatService = poliformatRetrofit.create(PoliformatService::class.java)
        on ("subject resources request") {
            val subjectId = System.getProperty("subject.id") ?: System.getenv("subject.id") ?: "GRA_11571_2017"
            val call = poliformatService.resources(subjectId)
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