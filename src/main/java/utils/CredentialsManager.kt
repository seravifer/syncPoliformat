package utils

import java.io.*

// TODO: Abstraer a una interfax e implementar en CookieJar O Credentials
object CredentialsManager {

    val credentials: Pair<String, String>
        @Throws(IOException::class)
        get() {
            val credentials = credentialsFile.readLines()
            val token = credentials[0]
            val dns = credentials[1]
            return token to dns
        }

    val credentialsFile: File
        get() = Settings.appDirectory.resolve("credentials").toFile()

}// TODO: Encriptar las credenciales
