package service.impl

import appModule
import com.github.salomonbrys.kodein.instance
import com.squareup.moshi.JsonAdapter
import data.Repository
import domain.SubjectInfo
import mu.KLogging
import service.SiteService
import java.io.File
import java.util.concurrent.CompletableFuture

class SiteServiceImpl(private val repo: Repository, private val subjectsFile: File) : SiteService {
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
        val oldMap = appModule.instance<JsonAdapter<Map<String, String>>>().fromJson(subjectsFile.readText()) as MutableMap<String, String>?
        val newMap = oldMap?.also { map ->
            forEach { subject ->
                map[subject.id] = subject.lastUpdate
            }
        } ?: associate { it.id to it.lastUpdate }
        val json = appModule.instance<JsonAdapter<Map<String, String>>>().toJson(newMap)
        subjectsFile.writeText(json)
    }

    private fun List<SubjectInfo>.loadLastUpdateDate() {
        val lastUpdateMap = appModule.instance<JsonAdapter<Map<String, String>>>().fromJson(subjectsFile.readText())
        if (lastUpdateMap != null) forEach { subject -> lastUpdateMap[subject.id]?.let { subject.lastUpdate = it } }
    }

    companion object : KLogging()
}