package service.impl

import com.squareup.moshi.JsonAdapter
import data.Repository
import domain.SubjectInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import mu.KLogging
import service.SiteService
import java.io.File
import kotlin.coroutines.CoroutineContext

class SiteServiceImpl(
        private val repo: Repository,
        private val subjectsFile: File,
        private val subjectsMapJsonAdapter: JsonAdapter<Map<String, String>>
) : SiteService, CoroutineScope {
    private val parentJob = Job()
    override val coroutineContext: CoroutineContext = parentJob + Dispatchers.IO

    override suspend fun getSubjects(): List<SubjectInfo> = withContext(coroutineContext) {
        val nameMap = repo.getSiteSubjectNames()
        val subjectList = repo.getSiteSubjects()
        subjectList.apply {
            loadLastUpdateDate()
            saveNewSubjectIds()
        }
        logger.debug { "Retrieved subject list" }
        subjectList.forEach { it.name = nameMap[it.id] ?: it.shortName }
        subjectList
    }

    private fun List<SubjectInfo>.saveNewSubjectIds() {
        val oldMap = subjectsMapJsonAdapter.fromJson(subjectsFile.readText()) as MutableMap<String, String>?
        val newMap = oldMap?.also { map ->
            forEach { subject ->
                map[subject.id] = subject.lastUpdate
            }
        } ?: associate { it.id to it.lastUpdate }
        val json = subjectsMapJsonAdapter.toJson(newMap)
        subjectsFile.writeText(json)
    }

    private fun List<SubjectInfo>.loadLastUpdateDate() {
        val lastUpdateMap = subjectsMapJsonAdapter.fromJson(subjectsFile.readText())
        if (lastUpdateMap != null) forEach { subject -> lastUpdateMap[subject.id]?.let { subject.lastUpdate = it } }
    }

    companion object : KLogging()
}
