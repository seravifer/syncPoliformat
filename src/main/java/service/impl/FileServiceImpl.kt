package service.impl

import com.squareup.moshi.JsonAdapter
import data.Repository
import domain.ContentEntity
import domain.PoliformatFile
import domain.SubjectInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import mu.KLogging
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
        private val subjectsFile: File,
        private val poliformatFolder: File,
        private val subjectFileFactory: (SubjectInfo) -> File,
        private val contentEntityJsonAdapter: JsonAdapter<ContentEntity>,
        private val subjectsMapJsonAdapter: JsonAdapter<Map<String, String>>
) : FileService, CoroutineScope {
    private val parentJob = Job()
    override val coroutineContext: CoroutineContext = parentJob + Dispatchers.IO

    override suspend fun syncSubjectFiles(subjectInfo: SubjectInfo): String = withContext(coroutineContext) {
        val content =  subjectService.subjectContentResources(subjectInfo.id)
        val filesFromPoliformat = content.collection.asSequence()
        val filesToDownload = if (subjectInfo.lastUpdate.isEmpty()) {
            filesFromPoliformat
        } else {
            val oldFilesJson = subjectFileFactory(subjectInfo).readText()
            val oldFilesData = contentEntityJsonAdapter.fromJson(oldFilesJson)
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
                    val path = poliformatFolder.toPath()
                            .resolve(it.localPath)
                            .changeExtension(Paths.get(it.url.path).toFile().extension)
                    path.parent.toFile().mkdirs()
                    logger.info { "Downloading file: $path" }
                    async {
                        repo.downloadFile(it.url, path).also {
                            logger.debug { "Downloaded file: $path" }
                        }
                    }
                }.toList()
    }

    private fun Path.changeExtension(ext: String): Path {
        return parent.resolve(toFile().nameWithoutExtension + "." + ext)
    }

    private fun saveSubjectUpdateDate(subject: SubjectInfo, now: String) {
        val map = subjectsMapJsonAdapter.fromJson(subjectsFile.readText()) as MutableMap<String, String>
        map[subject.id] = now
        subject.lastUpdate = now
        subjectsFile.writeText(subjectsMapJsonAdapter.toJson(map))
    }

    private fun saveContentInfo(subjectInfo: SubjectInfo, subjectContent: ContentEntity) {
        val json = contentEntityJsonAdapter.toJson(subjectContent)
        subjectFileFactory(subjectInfo).writeText(json)
    }

    companion object : KLogging()
}
