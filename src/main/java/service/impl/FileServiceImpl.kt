package service.impl

import appComponent
import com.squareup.moshi.JsonAdapter
import data.Repository
import domain.ContentEntity
import domain.PoliformatFile
import domain.SubjectInfo
import kotlinx.coroutines.*
import mu.KLogging
import org.kodein.di.direct
import org.kodein.di.generic.instance
import service.FileService
import service.SubjectService
import utils.Utils
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.coroutines.CoroutineContext

class FileServiceImpl(
        private val repo: Repository,
        private val subjectService: SubjectService,
        private val subjectsFile: File
) : FileService, CoroutineScope {
    private val parentJob = Job()
    override val coroutineContext: CoroutineContext = parentJob + Dispatchers.IO

    override suspend fun syncSubjectFiles(subjectInfo: SubjectInfo): String = withContext(coroutineContext) {
        val content =  subjectService.subjectContentResources(subjectInfo.id)
        val filesFromPoliformat = content.collection.asSequence()
        val filesToDownload = if (subjectInfo.lastUpdate.isEmpty()) {
            filesFromPoliformat
        } else {
            val oldFilesJson = appComponent.direct.instance<SubjectInfo, File>(arg = subjectInfo).readText()
            val oldFilesData = appComponent.direct.instance<JsonAdapter<ContentEntity>>().fromJson(oldFilesJson)
            filesFromPoliformat - oldFilesData!!.collection
        }
        downloadFiles(filesToDownload).forEach { it.await() }
        saveContentInfo(subjectInfo, content)
        val now = Utils.now()
        saveSubjectUpdateDate(subjectInfo, now)
        now
    }

    private fun downloadFiles(files: Sequence<PoliformatFile>): List<Deferred<Boolean>> {
        return files.filter { !it.isFolder }
                .map {
                    val path = appComponent.direct.instance<File>("poliformat").toPath()
                            .resolve(it.localPath)
                            .changeExtension(Paths.get(it.url.path).toFile().extension)
                    path.parent.toFile().mkdirs()
                    logger.info { "Downloading file: $path\n" }
                    async { repo.downloadFile(it.url, path) }
                }.toList()
    }

    private fun Path.changeExtension(ext: String): Path {
        return parent.resolve(toFile().nameWithoutExtension + "." + ext)
    }

    private fun saveSubjectUpdateDate(subject: SubjectInfo, now: String) {
        val map = appComponent.direct.instance<JsonAdapter<Map<String, String>>>().fromJson(subjectsFile.readText()) as MutableMap<String, String>
        map[subject.id] = now
        subject.lastUpdate = now
        subjectsFile.writeText(appComponent.direct.instance<JsonAdapter<Map<String, String>>>().toJson(map))
    }

    private fun saveContentInfo(subjectInfo: SubjectInfo, subjectContent: ContentEntity) {
        val json = appComponent.direct.instance<JsonAdapter<ContentEntity>>().toJson(subjectContent)
        appComponent.direct.instance<SubjectInfo, File>(arg = subjectInfo).writeText(json)
    }

    companion object : KLogging()
}
