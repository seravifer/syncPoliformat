package service.impl

import data.Repository
import domain.ContentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import service.SubjectService
import kotlin.coroutines.CoroutineContext

class SubjectServiceImpl(private val repo: Repository) : SubjectService, CoroutineScope {
    private val parentJob = Job()
    override val coroutineContext: CoroutineContext = parentJob + Dispatchers.IO

    override suspend fun subjectContentResources(id: String): ContentEntity = withContext(coroutineContext) {
        repo.getSubjectContent(id)
    }
}
