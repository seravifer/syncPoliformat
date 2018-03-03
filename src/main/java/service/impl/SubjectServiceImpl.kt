package service.impl

import data.Repository
import domain.PoliformatFile
import service.SubjectService
import utils.Tree
import java.util.concurrent.CompletableFuture

class SubjectServiceImpl(private val repo: Repository) : SubjectService {
    override fun subjectResources(id: String): CompletableFuture<Tree<PoliformatFile>> {
        return repo.getSubjectContent(id).thenApplyAsync { it.toFileTree() }
    }
}