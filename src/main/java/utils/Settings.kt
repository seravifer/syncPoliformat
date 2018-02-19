package utils

import model.SubjectInfo
import model.json.LastSubjectUpdateAdapter

import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object Settings {

    val subjectsPath: Path
        get() = Paths.get(appDirectory(), "subjects.json")

    fun appDirectory(): String {
        val pathDirectory: String
        val os = System.getProperty("os.name").toLowerCase()

        if (os.contains("win")) {
            pathDirectory = System.getenv("APPDATA")
        } else {
            pathDirectory = Paths.get(System.getProperty("user.home"), ".local", "share").toString()
        }

        return Paths.get(pathDirectory, "syncPoliformat").toString()
    }

    fun poliformatDirectory(): String {
        return Paths.get(System.getProperty("user.home"), "Poliformat").toString()
    }

    @Throws(IOException::class)
    fun loadLocal(id: String): String {
        return String(Files.readAllBytes(Paths.get(appDirectory(), id + ".json")))
    }

    @Throws(IOException::class)
    fun saveRemote(id: String) {
        val url = URL("https://poliformat.upv.es/direct/content/site/$id.json")
        val to = Paths.get(appDirectory(), id + ".json")
        url.openStream().use { from -> Files.copy(from, to) }
    }

    fun updateSubject(subjectInfo: SubjectInfo) {
        val file = subjectsPath.toFile()
        try {
            val jsonSubjects = LastSubjectUpdateAdapter
                    .fromJson(Utils.readFile(file)) as MutableMap<String, String>
            jsonSubjects[subjectInfo.id] = subjectInfo.lastUpdate

            val out = FileOutputStream(file, false)
            out.write(LastSubjectUpdateAdapter.toJson(jsonSubjects).toByteArray(charset("UTF-8")))
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}
