package model

import model.json.*
import org.jsoup.Jsoup
import utils.Settings
import utils.Utils
import utils.Utils.curso
import utils.task

import java.io.IOException

class Poliformat {

    fun fetchSubjectsInfo() = task<List<SubjectInfo>> {
        val json = Utils.getJson("site.json")
        val subjectsEntity = SiteEntityAdapter.fromJson(json)
        val subjects = subjectsEntity?.siteCollection
                ?.filter(SubjectInfo::isRealSubject) ?: arrayListOf()
        fetchRealSubjectNames(subjects)
    }

    @Throws(IOException::class)
    private fun fetchRealSubjectNames(subjects: List<SubjectInfo>): List<SubjectInfo> {
        val doc = Jsoup.connect("https://intranet.upv.es/pls/soalu/sic_asi.Lista_asig").get()
        val inputElements = doc.getElementsByClass("upv_enlace")

        for (inputElement in inputElements) {
            val name = inputElement.ownText().trim()
            val completeId = inputElement.getElementsByTag("span").text()
            val firstComaIndex = completeId.indexOf(',')
            val id = completeId.substring(1 until firstComaIndex)
            subjects.find { it.id == "GRA_${id}_$curso" }?.name = name
        }

        return subjects
    }

    @Throws(IOException::class)
    fun persistLastUpdateSubjectsDate(currentSubjects: List<SubjectInfo>) = task<Unit> {
        val file = Settings.subjectsPath.toFile()

        val jsonSubjects = LastSubjectUpdateAdapter.fromJson(file.readText()) as MutableMap<String, String>
        for (itemSubject in currentSubjects) {
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