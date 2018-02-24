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
        get() = appDirectory.resolve("subjects.json")

    val appDirectory: Path
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
    fun saveRemote(id: String) {
        val url = URL("https://poliformat.upv.es/direct/content/site/$id.json")
        val to = appDirectory.resolve("$id.json").toFile()
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

    @Throws(IOException::class)
    fun initFolders() {
        val subjectsUpdate = subjectsPath.toFile()
        val directory = appDirectory.toFile()

        if (!poliformatDirectory.exists()) poliformatDirectory.mkdir()
        if (!directory.exists()) directory.mkdir()
        if (!subjectsUpdate.exists()) subjectsUpdate.writeText("{}")
    }

}
