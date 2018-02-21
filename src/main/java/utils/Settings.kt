package utils

import model.SubjectInfo
import model.json.LastSubjectUpdateAdapter
import java.io.File

import java.io.IOException
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths

object Settings {

    val subjectsPath: Path
        get() = Paths.get(appDirectory(), "subjects.json")

    fun appDirectory(): String {
        val pathDirectory = if (Utils.isWindowsHost) {
            System.getenv("APPDATA")
        } else {
            Paths.get(System.getProperty("user.home"), ".local", "share").toString()
        }

        return Paths.get(pathDirectory, "syncPoliformat").toString()
    }

    fun poliformatDirectory(): String {
        return Paths.get(System.getProperty("user.home"), "Poliformat").toString()
    }

    @Throws(IOException::class)
    fun loadLocal(id: String): String {
        return File(appDirectory(), "$id.json").readText()
    }

    @Throws(IOException::class)
    fun saveRemote(id: String) {
        val url = URL("https://poliformat.upv.es/direct/content/site/$id.json")
        val to = File(appDirectory(), "$id.json")
        url.openStream().use { from ->
            from.copyTo(to.outputStream())
        }
    }

    fun updateSubject(subjectInfo: SubjectInfo) {
        val json = subjectsPath.toFile().readText()
        val jsonSubjects = LastSubjectUpdateAdapter.fromJson(json) as MutableMap<String, String>
        jsonSubjects[subjectInfo.id] = subjectInfo.lastUpdate
        subjectsPath.toFile().writeText(LastSubjectUpdateAdapter.toJson(jsonSubjects))
    }

}
