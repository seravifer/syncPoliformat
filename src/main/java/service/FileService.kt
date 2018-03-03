package service

import java.util.concurrent.CompletableFuture


interface FileService {
    fun syncSubjectFiles(subjectId: String): CompletableFuture<Unit>
}