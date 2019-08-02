package data

import domain.ContentEntity
import domain.SubjectInfo
import domain.UserInfo
import java.net.URL
import java.nio.file.Path

interface Repository {
    suspend fun getCurrentUser(): UserInfo
    suspend fun getSiteSubjectNames(): Map<GradId, SubjectName>
    suspend fun getSiteSubjects(): List<SubjectInfo>
    suspend fun getSubjectContent(id: String): ContentEntity
    suspend fun downloadFile(url: URL, dest: Path): Boolean
}

typealias GradId = String
typealias SubjectName = String
