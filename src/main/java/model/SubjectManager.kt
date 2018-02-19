package model

import model.json.*
import utils.Settings
import utils.Tree
import utils.Utils

import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths

class SubjectManager(private val subjectInfo: SubjectInfo) {
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
    fun sync() {
        val entityFilesJson = Utils.getJson("content/site/" + subjectInfo.id + ".json")
        val entity = ContentEntityAdapter.fromJson(entityFilesJson)
        filesystem = entity!!.toFileTree()
        if (subjectInfo.lastUpdate.isEmpty()) {
            downloadSubject()
        } else {
            syncSubject()
        }

        saveChanges()
    }

    /**
     * Descarga la asignatura por primera vez.
     *
     */
    private fun downloadSubject() {
        System.out.printf("Download started: %s\n", subjectInfo.name)
        downloadTree(filesystem!!, Settings.poliformatDirectory())
        System.out.printf("Download finished: %s\n", subjectInfo.name)
    }

    private fun downloadTree(node: Tree<PoliformatFile>, parentPath: String) {
        val data = node.data
        val path = Paths.get(parentPath, data.title)

        if (data.type == "collection") {
            val directory = File(path.toString())
            directory.mkdir()
            System.out.printf("Creating the folder: %s\n", path.toString())
        } else {
            try {
                Utils.downloadFile(data.url!!, path.toString())
                System.out.printf("Downloading the file: %s\n", path.toString())
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        for (child in node.getChildren()) {
            downloadTree(child, path.toString())
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
        val files = filesystem!!.merge(localTree)
        downloadMergeFiles(files)
    }

    private fun downloadMergeFiles(files: List<PoliformatFile>?) {

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