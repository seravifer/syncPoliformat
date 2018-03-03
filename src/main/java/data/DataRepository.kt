package data

import data.network.PoliformatService
import data.network.UpvService
import domain.ContentEntity
import domain.SubjectInfo
import domain.UserInfo
import java.net.URL
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

// TODO: Crear interfaz e implementar métodos recuperadores de datos para FileService, SiteService y SybjectService
class DataRepository(
        private val poliformatService: PoliformatService
) : Repository {
    override fun getCurrentUser(): CompletableFuture<UserInfo> = poliformatService.currentUser()

    override fun getSiteSubjectNames(): CompletableFuture<Map<GradId, SubjectName>> {

    }

    override fun getSiteSubjects(): CompletableFuture<List<SubjectInfo>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSubjectContent(id: String): CompletableFuture<ContentEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun downloadFile(url: URL, dest: Path): CompletableFuture<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}