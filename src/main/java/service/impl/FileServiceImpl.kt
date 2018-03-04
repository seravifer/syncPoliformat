package service.impl

import data.Repository
import domain.ContentEntity
import domain.PoliformatFile
import domain.SubjectInfo
import domain.json.ContentEntityAdapter
import domain.json.LastSubjectUpdateAdapter
import mu.KLogging
import service.FileService
import service.SubjectService
import utils.Settings
import utils.Utils
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture

class FileServiceImpl(
        private val repo: Repository,
        private val subjectService: SubjectService
) : FileService {

    override fun syncSubjectFiles(subjectInfo: SubjectInfo): CompletableFuture<String> {
        return subjectService.subjectContentResources(subjectInfo.id)
                .thenApplyAsync { content ->
                    val filesFromPoliformat = content.collection.asSequence()
                    val filesToDownload = if (subjectInfo.lastUpdate.isEmpty()) {
                        filesFromPoliformat
                    } else {
                        val oldFilesData = ContentEntityAdapter.fromJson(Settings.loadLocal(subjectInfo.id))
                        filesFromPoliformat - oldFilesData!!.collection
                    }
                    downloadFiles(filesToDownload)
                    saveContentInfo(subjectInfo, content)
                    val now = Utils.now()
                    saveSubjectUpdateDate(subjectInfo, now)
                    now
                }
    }

    private fun downloadFiles(files: Sequence<PoliformatFile>) {
        files.filter { !it.isFolder }
                .forEach {
                    val path = Settings.poliformatDirectory.toPath()
                            .resolve(it.localPath)
                            .changeExtension(Paths.get(it.url.path).toFile().extension)
                    path.parent.toFile().mkdirs()
                    repo.downloadFile(it.url, path)
                    logger.info { "Downloading file: $path\n" }
                }
    }

    private fun Path.changeExtension(ext: String): Path {
        return parent.resolve(toFile().nameWithoutExtension + "." + ext)
    }

    private fun saveSubjectUpdateDate(subject: SubjectInfo, now: String) {
        val map = LastSubjectUpdateAdapter.fromJson(Settings.subjectsPath.toFile().readText()) as MutableMap<String, String>
        map[subject.id] = now
        subject.lastUpdate = now
        Settings.subjectsPath.toFile().writeText(LastSubjectUpdateAdapter.toJson(map))
    }

    private fun saveContentInfo(subjectInfo: SubjectInfo, subjectContent: ContentEntity) {
        val json = ContentEntityAdapter.toJson(subjectContent)
        Settings.appDirectory.resolve("${subjectInfo.id}.json").toFile().writeText(json)
    }

    companion object : KLogging()
}