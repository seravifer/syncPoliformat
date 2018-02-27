package data.network

import utils.IOExecutor
import utils.Settings
import java.io.File

class Credentials(token: String? = null, dns: String? = null, remember: Boolean = false) {

    companion object {
        val credentialsFile: File
            get() = Settings.appDirectory.resolve("credentials").toFile()

        private fun saveCredentials(token: String, dns: String) {
            with(credentialsFile) {
                createNewFile()
                printWriter().use {
                    it.println(token)
                    it.println(dns)
                }
            }
        }
    }

    var token: String
    var dns: String

    init {
        if (token == null || dns == null) {
            val credentials = credentialsFile.readLines()
            this.token = credentials[0]
            this.dns = credentials[1]
        } else {
            this.token = token
            this.dns = dns
            if (remember) IOExecutor.execute { saveCredentials(token, dns) }
        }
    }
}