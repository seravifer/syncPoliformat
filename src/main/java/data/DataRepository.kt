package data

import data.network.PoliformatService
import domain.ContentEntity
import domain.SubjectInfo
import domain.UserInfo
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class DataRepository(
        private val poliformatService: PoliformatService
) : Repository {
    override fun getCurrentUser(): CompletableFuture<UserInfo> = poliformatService.currentUser()

    override fun getSiteSubjectNames(): CompletableFuture<Map<GradId, SubjectName>> {
        TODO("La recuperación de los nombres reales de las asignaturas")
    }

    override fun getSiteSubjects(): CompletableFuture<List<SubjectInfo>> {
        return poliformatService.siteInfo().thenApplyAsync { it.siteCollection!!.filter { it.isRealSubject } }
    }

    override fun getSubjectContent(id: String): CompletableFuture<ContentEntity> {
        return poliformatService.resources(id)
    }

    override fun downloadFile(url: URL, dest: Path): CompletableFuture<Boolean> {
        return poliformatService.downloadFile(url.toString()).thenApplyAsync {
            writeToDisk(it.byteStream(), dest.toFile())
        }
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