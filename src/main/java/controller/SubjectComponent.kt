package controller

import com.jfoenix.controls.JFXSpinner
import data.DataRepository
import data.network.Intranet
import data.network.Poliformat
import domain.SubjectInfo
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.SVGPath
import mu.KLogging
import service.FileService
import service.impl.FileServiceImpl
import service.impl.SubjectServiceImpl
import utils.JavaFXExecutor
import utils.Utils
import java.util.function.BiFunction

class SubjectComponent
constructor(
        private val subject: SubjectInfo,
        private val fileService: FileService = FileServiceImpl(DataRepository(Poliformat, Intranet), SubjectServiceImpl(DataRepository(Poliformat, Intranet)))
) : AnchorPane() {

    @FXML
    private lateinit var longNameID: Label

    @FXML
    private lateinit var nameID: Label

    @FXML
    private lateinit var dateID: Label

    @FXML
    private lateinit var downloadID: SVGPath

    @FXML
    private lateinit var syncID: SVGPath

    @FXML
    private lateinit var loadingID: JFXSpinner

    @FXML
    private lateinit var circleID: Circle

    private val colors = arrayOf("#ad1457", "#6a1b9a", "#4527a0", "#283593", "#1565c0", "#0277bd", "#00838f", "#00695c",
            "#2e7d32", "#558b2f", "#9e9d24", "#f9a825", "#ff8f00", "#ef6c00", "#d84315")

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/view/subject.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        fxmlLoader.load<Any>()

        nameID.text = subject.shortName
        longNameID.text = subject.name
        dateID.text = formatLastUpdate(subject.lastUpdate)
        circleID.fill = Color.web(colors[Utils.random(1, colors.size - 1)])

        syncID.setOnMouseClicked { update() }

        if (subject.lastUpdate.isEmpty()) {
            syncID.isVisible = false
            downloadID.isVisible = true
        }
    }

    private fun formatLastUpdate(lastUpdate: String): String {
        return if (lastUpdate.isEmpty()) String("No ha sido sincronizada todavía.".toByteArray()) else lastUpdate
    }

    private fun update() {
        syncID.isVisible = false
        downloadID.isVisible = false
        loadingID.isVisible = true
        dateID.text = "Descargando asignatura..."

        fileService.syncSubjectFiles(subject)
                .handleAsync(BiFunction<String, Throwable?, Unit> { now, e ->
                    if (e != null) logger.warn(e) { "Error al descargar los archivos\n" }
                    else subject.lastUpdate = now
                    finish()
                }, JavaFXExecutor)
    }

    private fun finish() {
        syncID.isVisible = true
        loadingID.isVisible = false
        dateID.text = formatLastUpdate(subject.lastUpdate)
    }

    fun sync() {
        update()
    }

    companion object : KLogging()

}
