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
import utils.Settings
import utils.Utils
import java.util.function.BiFunction

class SubjectComponent(
        private val subject: SubjectInfo,
        private val fileService: FileService = FileServiceImpl(DataRepository(Poliformat, Intranet), SubjectServiceImpl(DataRepository(Poliformat, Intranet)), Settings.subjectsFile)
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

    private val colors = arrayOf("#F44336", "#E91E63", "#9C27B0", "#673AB", "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
            "#009688", "#4CAF50", "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722", "#607D8B")

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/view/subject.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        fxmlLoader.load<Any>()

        nameID.text = subject.shortName
        longNameID.text = subject.name
        dateID.text = formatLastUpdate(subject.lastUpdate)
        circleID.fill = Color.web(colors[Utils.random(colors.size)])

        syncID.setOnMouseClicked { update() }

        if (subject.lastUpdate.isEmpty()) {
            syncID.isVisible = false
            downloadID.isVisible = true
        }
    }

    private fun formatLastUpdate(lastUpdate: String): String {
        return if (lastUpdate.isEmpty()) String("No ha sido sincronizada todavía.".toByteArray()) else lastUpdate
    }

    @FXML
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
