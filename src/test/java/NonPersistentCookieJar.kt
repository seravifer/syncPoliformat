
import okhttp3.Cookie
import java.util.ArrayList
import okhttp3.HttpUrl
import java.util.LinkedHashSet
import okhttp3.CookieJar


object NonPersistentCookieJar : CookieJar {
    private val cookieStore = LinkedHashSet<Cookie>()

    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.addAll(cookies)
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val matchingCookies = ArrayList<Cookie>()
        val it = cookieStore.iterator()
        while (it.hasNext()) {
            val cookie = it.next()
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                it.remove()
            } else if (cookie.matches(url)) {
                matchingCookies.add(cookie)
            }
        }
        return matchingCookies
    }
}