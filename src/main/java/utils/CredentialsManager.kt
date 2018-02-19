package utils

import javafx.util.Pair

import java.io.*

object CredentialsManager {

    val credentials: Pair<String, String>
        @Throws(IOException::class)
        get() {
            val br = BufferedReader(FileReader(credentialsFile()))

            val credentials = br.lines().toArray()
            val token = credentials[0].toString()
            val dns = credentials[1].toString()

            return Pair(token, dns)
        }

    fun credentialsFile(): File {
        return File(Settings.appDirectory(), "credentials")
    }

    fun saveCredentials(token: String, dns: String) {
        try {
            credentialsFile().createNewFile()

            val printer = PrintWriter(credentialsFile(), "UTF-8")
            printer.println(token)
            printer.println(dns)
            printer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun deleteCredentials() {
        credentialsFile().delete()
    }

}// TODO: Encriptar las credenciales
