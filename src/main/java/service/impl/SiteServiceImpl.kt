package service.impl

import data.Repository
import domain.SubjectInfo
import domain.json.LastSubjectUpdateAdapter
import mu.KLogging
import service.SiteService
import utils.Settings
import java.util.concurrent.CompletableFuture

class SiteServiceImpl(val repo: Repository) : SiteService {
    override fun getSubjects(): CompletableFuture<List<SubjectInfo>> {
        return repo.getSiteSubjects().thenCombineAsync(repo.getSiteSubjectNames()) { subjectList, nameMap ->
            subjectList.apply {
                loadLastUpdateDate()
                saveNewSubjectIds()
            }
            logger.debug { "Subjects\n" }
            subjectList.forEach { it.name = nameMap[it.id] ?: it.shortName }
            subjectList
        }
    }

    private fun List<SubjectInfo>.saveNewSubjectIds() {
        val oldMap = LastSubjectUpdateAdapter.fromJson(Settings.subjectsPath.toFile().readText()) as MutableMap<String, String>?
        val newMap = oldMap?.also { map ->
            forEach { subject ->
                map[subject.id] = subject.lastUpdate
            }
        } ?: associate { it.id to it.lastUpdate }
        val json = LastSubjectUpdateAdapter.toJson(newMap)
        Settings.subjectsPath.toFile().writeText(json)
    }

    private fun List<SubjectInfo>.loadLastUpdateDate() {
        val lastUpdateMap = LastSubjectUpdateAdapter.fromJson(Settings.subjectsPath.toFile().readText())
        if (lastUpdateMap != null) forEach { subject -> lastUpdateMap[subject.id]?.let { subject.lastUpdate = it } }
    }

    /*private fun fetchRealSubjectNames(subjects: List<SubjectInfo>): List<SubjectInfo> {
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
    }*/
    companion object : KLogging()
}