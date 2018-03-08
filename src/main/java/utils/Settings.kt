package utils

import java.io.File

import java.io.IOException
import java.net.URL

// TODO: Reevaluar esto
object Settings {
    fun initFolders(vararg files: File) {
        files.asSequence().filter { !it.exists() }
                .forEach {
                    if (it.extension.isNotEmpty()) {
                        if (it.extension.contains("json")) it.writeText("{}")
                    } else {
                        it.mkdirs()
                    }
                }
    }

    fun checkVersion(): Boolean {
        try {
            val url = URL("http://sergiavila.com/version") // Temporal
            val newVersion: Double = url.openStream().bufferedReader().use {
                it.readLine().toDouble()
            }

            return 1.0 < newVersion
        } catch (e: IOException) {
            System.err.println("Error al comprobar la versión de la aplicación.")
        }

        return false
    }

}
