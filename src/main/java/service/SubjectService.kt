package service

import domain.PoliformatFile
import utils.Tree
import java.util.concurrent.CompletableFuture

interface SubjectService {
    fun subjectResources(id: String): CompletableFuture<Tree<PoliformatFile>>
}