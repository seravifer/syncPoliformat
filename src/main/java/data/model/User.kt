package data.model

import domain.json.UserInfoAdapter
import utils.CredentialsManager
import utils.Utils
import java.io.IOException
import java.net.CookieHandler
import java.net.CookieManager
import java.net.HttpCookie

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
    private fun syncUserInfo() {
        UserInfoAdapter.fromJson(Utils.getJson("user/current.json"))?.let { info ->
            nameUser = info.firstName
            lastNameUser = info.lastName
            mailUser = info.email
        }
    }
}
