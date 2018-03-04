package service

import domain.ContentEntity
import java.util.concurrent.CompletableFuture

interface SubjectService {
    fun subjectContentResources(id: String): CompletableFuture<ContentEntity>
}