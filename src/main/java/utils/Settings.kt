package utils

import java.io.BufferedReader
import java.io.File

import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths

// TODO: Reevaluar esto
object Settings {

    val subjectsPath: Path
        get() = appDirectory.resolve("subjects.json")

    val appDirectory: Path
        @JvmStatic
        get() {
            val pathDirectory = if (Utils.isWindowsHost) {
                Paths.get(System.getenv("APPDATA"))
            } else {
                Paths.get(System.getProperty("user.home"), ".local", "share")
            }

            return pathDirectory.resolve("syncPoliformat")
        }

    val poliformatDirectory by lazy { File(System.getProperty("user.home"), "Poliformat") }


    @Throws(IOException::class)
    fun loadLocal(id: String) = appDirectory.resolve("$id.json").toFile().readText()

    @Throws(IOException::class)
    fun initFolders() {
        val subjectsUpdate = subjectsPath.toFile()
        val directory = appDirectory.toFile()

        if (!poliformatDirectory.exists()) poliformatDirectory.mkdir()
        if (!directory.exists()) directory.mkdir()
        if (!subjectsUpdate.exists()) subjectsUpdate.writeText("{}")
    }

    fun checkVersion(): Boolean {
        try {
            val url = URL("http://sergiavila.com/version") // Temporal
            val `in` = BufferedReader(InputStreamReader(url.openStream()))

            val newVersion = java.lang.Double.valueOf(`in`.readLine())
            `in`.close()

            return 1.0 < newVersion
        } catch (e: IOException) {
            System.err.println("Error al comprobar la versión de la aplicación.")
        }

        return false
    }

}
