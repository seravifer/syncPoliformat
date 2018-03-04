package service

import domain.SubjectInfo
import java.util.concurrent.CompletableFuture


interface FileService {
    fun syncSubjectFiles(subjectInfo: SubjectInfo): CompletableFuture<Now>
}

typealias Now = String