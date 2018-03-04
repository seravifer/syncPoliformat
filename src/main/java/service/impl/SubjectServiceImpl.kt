package service.impl

import data.Repository
import domain.ContentEntity
import service.SubjectService
import java.util.concurrent.CompletableFuture

class SubjectServiceImpl(private val repo: Repository) : SubjectService {
    override fun subjectContentResources(id: String): CompletableFuture<ContentEntity> {
        return repo.getSubjectContent(id)
    }
}