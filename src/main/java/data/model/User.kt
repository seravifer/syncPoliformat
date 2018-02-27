package data.model

import domain.json.UserInfoAdapter
import utils.CredentialsManager
import utils.Utils
import utils.task

import javax.net.ssl.HttpsURLConnection
import java.io.DataOutputStream
import java.io.IOException
import java.net.*

// TODO: Pasar funcionalidad a services
class User {

    var nameUser: String? = null
        private set
    var lastNameUser: String? = null
        private set
    var mailUser: String? = null
        private set
    var isLogged: Boolean? = false
        private set
    private var manager: CookieManager

    init {
        manager = CookieManager()
        CookieHandler.setDefault(manager)
    }

    @Throws(IOException::class)
    fun login(username: String, password: String, remember: Boolean) = task<Boolean> {
        val param = "&id=c&estilo=500&vista=MSE&cua=sakai&dni=$username&clau=$password&=Entrar"

        val url = URL("https://www.upv.es/exp/aute_intranet")

        val conn = url.openConnection() as HttpsURLConnection
        conn.doOutput = true

        val post = DataOutputStream(conn.outputStream)
        post.writeBytes(param)
        post.flush()
        post.close()

        val loggedIn = conn.getHeaderField("X-Sakai-Session") != null
        if (loggedIn) {
            syncUserInfo()
            if (remember) saveCredentials()
        }
        loggedIn
    }

    fun logout() {
        manager = CookieManager()
        CookieHandler.setDefault(manager)
        CredentialsManager.deleteCredentials()
    }

    fun checkLogin(): Boolean {
        return CredentialsManager.credentialsFile.exists()
    }

    @Throws(IOException::class)
    fun silentLogin() {
        val credentials = CredentialsManager.credentials

        val cookieToken = HttpCookie("TDp", credentials.first).apply {
            path = "/"
            version = 0
            domain = "upv.es"
        }

        val cookieDns = HttpCookie("JSESSIONID", credentials.second).apply {
            path = "/"
            version = 0
            domain = "poliformat.upv.es"
            secure = true
        }

        with(manager.cookieStore) {
            add(null, cookieToken)
            add(null, cookieDns)
        }

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
        UserInfoAdapter.fromJson(Utils.getJson("user/current.json"))?.let { info ->
            nameUser = info.firstName
            lastNameUser = info.lastName
            mailUser = info.email
        }
    }
}
