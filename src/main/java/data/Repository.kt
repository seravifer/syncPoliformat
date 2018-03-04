package data

import domain.ContentEntity
import domain.SubjectInfo
import domain.UserInfo
import java.net.URL
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

interface Repository {
    fun getCurrentUser(): CompletableFuture<UserInfo>
    fun getSiteSubjectNames(): CompletableFuture<Map<GradId, SubjectName>>
    fun getSiteSubjects(): CompletableFuture<List<SubjectInfo>>
    fun getSubjectContent(id: String): CompletableFuture<ContentEntity>
    fun downloadFile(url: URL, dest: Path): CompletableFuture<Boolean>
}

typealias GradId = String
typealias SubjectName = String