package service.impl

import data.Repository
import domain.SubjectInfo
import service.SiteService
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class SiteServiceImpl(val repo: Repository) : SiteService {
    override fun getSubjects(): CompletableFuture<List<SubjectInfo>> {
        val subjects = repo.getSiteSubjects()
        val subjectNames  = repo.getSiteSubjectNames()
        return subjects.thenCombineAsync(subjectNames) { subjects, names ->
            subjects.map { it.apply { name = names[id] ?: title } }
        }
    }
}