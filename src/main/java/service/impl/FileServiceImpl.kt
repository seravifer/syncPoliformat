package service.impl

import appModule
import build
import com.github.salomonbrys.kodein.instance
import com.squareup.moshi.JsonAdapter
import data.Repository
import domain.ContentEntity
import domain.PoliformatFile
import domain.SubjectInfo
import mu.KLogging
import service.FileService
import service.SubjectService
import utils.Utils
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture

class FileServiceImpl(
        private val repo: Repository,
        private val subjectService: SubjectService,
        private val subjectsFile: File
) : FileService {

    override fun syncSubjectFiles(subjectInfo: SubjectInfo): CompletableFuture<String> {
        return subjectService.subjectContentResources(subjectInfo.id)
                .thenApplyAsync { content ->
                    val filesFromPoliformat = content.collection.asSequence()
                    val filesToDownload = if (subjectInfo.lastUpdate.isEmpty()) {
                        filesFromPoliformat
                    } else {
                        val oldFilesJson = appModule.build<SubjectInfo, File>(subjectInfo).readText()
                        val oldFilesData = appModule.instance<JsonAdapter<ContentEntity>>().fromJson(oldFilesJson)
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
                    val path = appModule.instance<File>("poliformat").toPath()
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
        val map = appModule.instance<JsonAdapter<Map<String, String>>>().fromJson(subjectsFile.readText()) as MutableMap<String, String>
        map[subject.id] = now
        subject.lastUpdate = now
        subjectsFile.writeText(appModule.instance<JsonAdapter<Map<String, String>>>().toJson(map))
    }

    private fun saveContentInfo(subjectInfo: SubjectInfo, subjectContent: ContentEntity) {
        val json = appModule.instance<JsonAdapter<ContentEntity>>().toJson(subjectContent)
        appModule.build<SubjectInfo, File>(subjectInfo).writeText(json)
    }

    companion object : KLogging()
}