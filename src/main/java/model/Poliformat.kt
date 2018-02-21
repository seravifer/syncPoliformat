package model

import model.json.*
import org.jsoup.Jsoup
import utils.Settings
import utils.Utils

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.HashMap

class Poliformat {

    var subjects: Map<String, SubjectInfo>
        private set

    init {
        subjects = HashMap()
    }

    @Throws(IOException::class)
    private fun initFolders() {
        val folder = File(Settings.poliformatDirectory())
        val subjectsUpdate = Settings.subjectsPath.toFile()
        val directory = File(Settings.appDirectory())

        folder.mkdir()
        directory.mkdir()

        val out = FileOutputStream(subjectsUpdate)
        out.write("{}".toByteArray(charset("UTF-8")))
        out.flush()
        out.close()
    }

    fun syncRemote() {
        try {
            val json = Utils.getJson("site.json")
            val subjectsEntity = SiteEntityAdapter.fromJson(json)
            subjects = subjectsEntity?.siteCollection
                    ?.filter(SubjectInfo::isRealSubject)
                    ?.associateBy(SubjectInfo::id) ?: subjects
            fetchRealSubjectNames()
        } catch (e: IOException) {
            throw RuntimeException("Error en la descarga del indice de asignaturas", e)
        }

    }

    @Throws(IOException::class)
    private fun fetchRealSubjectNames() {
        val doc = Jsoup.connect("https://intranet.upv.es/pls/soalu/sic_asi.Lista_asig").get()

        val inputElements = doc.getElementsByClass("upv_enlace")

        val course = Utils.curso
        for (inputElement in inputElements) {
            val name = inputElement.ownText().trim { it <= ' ' }
            val completeId = inputElement.getElementsByTag("span").text()
            val firstComaIndex = completeId.indexOf(',')
            val id = completeId.substring(1, firstComaIndex)
            val subjectInfo = subjects!!["GRA_" + id + "_" + course]
            if (subjectInfo != null) {
                subjectInfo.name = name
            }
        }
    }

    @Throws(IOException::class)
    fun syncLocal() {
        val file = Settings.subjectsPath.toFile()

        if (!file.exists()) initFolders()

        val jsonSubjects = LastSubjectUpdateAdapter.fromJson(file.readText()) as MutableMap<String, String>
        for (itemSubject in subjects.values) {
            val lastUpdated = jsonSubjects[itemSubject.id]
            if (lastUpdated == null) {
                jsonSubjects[itemSubject.id] = ""
            } else {
                itemSubject.lastUpdate = lastUpdated
            }
        }

        file.writeText(LastSubjectUpdateAdapter.toJson(jsonSubjects))
    }

}