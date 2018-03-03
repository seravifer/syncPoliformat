package data.model

import domain.json.*
import domain.PoliformatFile
import domain.SubjectInfo
import mu.KLogging
import utils.Settings
import utils.Tree
import utils.Utils
import utils.task
import java.io.File

import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths

// TODO: Pasar funcionalidad a services
class SubjectManager(private val subjectInfo: SubjectInfo) {

    companion object Logger : KLogging()

    var filesystem: Tree<PoliformatFile>? = null
        private set

    fun updateLastUpdate(lastUpdate: String) {
        subjectInfo.lastUpdate = lastUpdate
        Settings.updateSubject(subjectInfo)
    }

    /**
     * Método lanzadera que sincroniza los archivos de Poliformat.
     *
     */
    @Throws(IOException::class)
    fun syncFiles() = task<Unit> {
        val entityFilesJson = Utils.getJson("content/site/${subjectInfo.id}.json")
        ContentEntityAdapter.fromJson(entityFilesJson)?.let { entity ->
            filesystem = entity.toFileTree()
            if (subjectInfo.lastUpdate.isEmpty()) {
                downloadSubject()
            } else {
                syncSubject()
            }

            saveChanges()
        }
    }

    /**
     * Descarga la asignatura por primera vez.
     *
     */
    private fun downloadSubject() {
        logger.info { "Download started: ${subjectInfo.name}\n" }
        downloadTree(filesystem!!, Settings.poliformatDirectory.toPath())
        logger.info { "Download finished: ${subjectInfo.name}\n" }
    }

    private fun downloadTree(node: Tree<PoliformatFile>, parentPath: Path) {
        val data = node.data
        val path = parentPath.resolve(data.title)

        if (data.isFolder) {
            path.toFile().mkdir()
            logger.info { "Creating the folder: $path\n" }
        } else {
            Utils.downloadFile(data.url, path)
            logger.info { "Downloading the file: $path\n" }
        }

        for (child in node.getChildren()) {
            downloadTree(child, path)
        }
    }

    /**
     * Compara los archivos locales con el remoto y descarga la diferencia.
     *
     */
    @Throws(IOException::class)
    private fun syncSubject() {
        val response = ContentEntityAdapter.fromJson(Settings.loadLocal(subjectInfo.id))
        val localTree = response!!.toFileTree()
        val files = localTree.merge(filesystem!!)
        downloadMergeFiles(files)
    }

    private fun downloadMergeFiles(pendingFiles: List<Pair<PoliformatFile, String>>) {
        val parentPath = Settings.poliformatDirectory

        for (file in pendingFiles) {
            val path = Paths.get(parentPath.path, file.second)
            if (file.first.isFolder) {
                val directory = File(path.toString())
                directory.mkdir()
                logger.info {"Sync: Creating folder: $path" }
            } else {
                try {
                    Utils.downloadFile(file.first.url, path)
                    logger.info { "Sync: Downloading file: $path\n" }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    /**
     * Guarda en local el archivo 'json' que ya ha sido sincronizado.
     *
     */
    @Throws(IOException::class)
    fun saveChanges() {
        Settings.saveRemote(subjectInfo.id)
    }
}