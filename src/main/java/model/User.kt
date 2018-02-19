package model

import model.json.UserInfoAdapter
import utils.CredentialsManager
import utils.Utils

import javax.net.ssl.HttpsURLConnection
import java.io.DataOutputStream
import java.io.IOException
import java.net.*

class User {

    var nameUser: String? = null
        private set
    var lastNameUser: String? = null
        private set
    var mailUser: String? = null
        private set
    var isLogged: Boolean? = false
        private set
    private val manager: CookieManager

    init {
        manager = CookieManager()
        CookieHandler.setDefault(manager)
    }

    @Throws(IOException::class)
    fun login(username: String, password: String, remember: Boolean?) {
        val param = "&id=c&estilo=500&vista=MSE&cua=sakai&dni=$username&clau=$password&=Entrar"

        val url = URL("https://www.upv.es/exp/aute_intranet")

        val conn = url.openConnection() as HttpsURLConnection
        conn.doOutput = true

        val post = DataOutputStream(conn.outputStream)
        post.writeBytes(param)
        post.flush()
        post.close()

        if (conn.getHeaderField("X-Sakai-Session") != null) {
            isLogged = true
            syncUserInfo()
            if (remember!!) saveCredentials()
        } else {
            isLogged = false
        }
    }

    fun logout() {
        CookieHandler.setDefault(CookieManager())
        CredentialsManager.deleteCredentials()
    }

    fun checkLogin(): Boolean {
        return CredentialsManager.credentialsFile().exists()
    }

    @Throws(IOException::class)
    fun silentLogin() {
        val credentials = CredentialsManager.credentials

        val cookieToken = HttpCookie("TDp", credentials.key)
        cookieToken.path = "/"
        cookieToken.version = 0
        cookieToken.domain = "upv.es"

        val cookieDns = HttpCookie("JSESSIONID", credentials.value)
        cookieDns.path = "/"
        cookieDns.version = 0
        cookieDns.domain = "poliformat.upv.es"
        cookieDns.secure = true

        val cookieJar = manager.cookieStore
        cookieJar.add(null, cookieToken)
        cookieJar.add(null, cookieDns)

        //printCokies();

        isLogged = true
        syncUserInfo()
    }

    @Throws(IOException::class)
    private fun saveCredentials() {
        val cookies = manager.cookieStore.cookies
        val token = cookies[0]
        val dns = cookies[1]
        CredentialsManager.saveCredentials(token.value, dns.value)
    }

    @Throws(IOException::class)
    private fun syncUserInfo() {
        val info = UserInfoAdapter.fromJson(Utils.getJson("user/current.json"))
        nameUser = info?.firstName
        lastNameUser = info?.lastName
        mailUser = info?.email
    }

    private fun printCokies() {
        val cookieJar = manager.cookieStore
        val cookies = cookieJar.cookies
        for (cookie in cookies) {
            println(cookie.value + " - " +
                    cookie.domain + " - " +
                    cookie.name + " - " +
                    cookie.path + " - " +
                    cookie.secure + " - " +
                    cookie.maxAge + " - " +
                    cookie.version)
        }
    }

}
