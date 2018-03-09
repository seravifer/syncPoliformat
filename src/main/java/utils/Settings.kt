package utils

import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Paths

// TODO: Reevaluar esto
object Settings {

    val subjectsFile: File
        get() = appDirectory.resolve("subjects.json")

    val appDirectory: File
        @JvmStatic
        get() {
            val pathDirectory = if (Utils.isWindows) {
                Paths.get(System.getenv("APPDATA"))
            } else {
                Paths.get(System.getProperty("user.home"), ".local", "share")
            }

            return pathDirectory.resolve("syncPoliformat").toFile()
        }

    val poliformatDirectory by lazy { File(System.getProperty("user.home"), "Poliformat") }

    fun loadLocal(id: String) = appDirectory.resolve("$id.json").readText()

    fun initFolders() {
        if (!poliformatDirectory.exists()) poliformatDirectory.mkdir()
        if (!appDirectory.exists()) appDirectory.mkdir()
        if (!subjectsFile.exists()) subjectsFile.writeText("{}")
    }

    fun checkVersion(): Boolean {
        try {
            val url = URL("https://seravifer.github.io/syncPoliformat/version")
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
