package data

import data.network.PoliformatService
import data.network.UpvService
import domain.ContentEntity
import domain.SubjectInfo
import domain.UserInfo
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.nio.file.Path

class DataRepository(
        private val poliformatService: PoliformatService,
        private val intranetService: UpvService
) : Repository {
    override suspend fun getCurrentUser(): UserInfo = poliformatService.currentUser()

    override suspend fun getSiteSubjectNames(): Map<GradId, SubjectName> {
        return intranetService.subjects().subjects
                .associate { subject -> subject.graId to subject.name }
    }

    override suspend fun getSiteSubjects(): List<SubjectInfo> {
        return poliformatService.siteInfo().siteCollection!!.filter { it.isRealSubject }
    }

    override suspend fun getSubjectContent(id: String): ContentEntity {
        return poliformatService.resources(id)
    }

    override suspend fun downloadFile(url: URL, dest: Path): Boolean {
        val file = poliformatService.downloadFile(url.toString())
        return writeToDisk(file.byteStream(), dest.toFile())
    }

    private fun writeToDisk(content: InputStream, dest: File): Boolean {
        return try {
            content.use { input ->
                dest.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: IOException) {
            false
        }
    }
}
