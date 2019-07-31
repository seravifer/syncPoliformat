package controller

import com.jfoenix.controls.JFXSpinner
import domain.SubjectInfo
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.SVGPath
import mu.KLogging
import service.FileService
import utils.JavaFXExecutor
import utils.Utils
import java.util.function.BiFunction

class SubjectComponent(
        private val subject: SubjectInfo,
        private val fileService: FileService
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

    private val colors = arrayOf("#f44336", "#e91e63", "#9c27b0", "#673ab7", "#3f51b5", "#2196f3", "#03a9f4", "#00bcd4",
            "#009688", "#4caf50", "#8bc34a", "#cddc39", "#ffeb3b", "#ffc107", "#ff9800", "#ff5722", "#607d8b")

    val updating = SimpleBooleanProperty(false)

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/view/subject.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        fxmlLoader.load<Any>()

        nameID.text = subject.shortName
        longNameID.text = subject.name
        dateID.text = formatLastUpdate(subject.lastUpdate)
        circleID.fill = Color.web(colors[Utils.random(colors.size)])

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
        updating.value = true
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
        updating.value = false
    }

    fun sync() {
        update()
    }

    companion object : KLogging()

}
