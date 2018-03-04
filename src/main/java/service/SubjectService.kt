package service

import domain.ContentEntity
import domain.PoliformatFile
import utils.ResourceTree
import java.util.concurrent.CompletableFuture

interface SubjectService {
    fun subjectContentResources(id: String): CompletableFuture<ContentEntity>
}