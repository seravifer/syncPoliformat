package data.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.LinkedHashSet

object CookieJarImpl : CookieJar, CredentialsHandler {
    private val cookieStore = LinkedHashSet<Cookie>()

    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.addAll(cookies)
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore.asSequence()
                .filter { it.expiresAt >= System.currentTimeMillis() }
                .filter { it.matches(url) }
                .toList()
    }

    fun getCookieByName(name: String): Cookie? = cookieStore.find { it.name == name }

    override fun loadCredentials(credentials: Credentials) {
        cookieStore += Cookie.Builder()
                .name("TDp")
                .value(credentials.token)
                .path("/")
                .domain("upv.es")
                .build()
        cookieStore += Cookie.Builder()
                .name("JSESSIONID")
                .value(credentials.dns)
                .path("/")
                .domain("poliformat.upv.es")
                .secure()
                .build()
    }

    override fun getCredentials(): Credentials? {
        val token = getCookieByName("TDp")?.value
        val dns = getCookieByName("JSESSIONID")?.value
        return if (token != null && dns != null) Credentials(token, dns) else null
    }

    override fun cleanCredentials() {
        cookieStore.removeIf { it.name == "TDp" || it.name == "JSESSIONID" }
    }
}
