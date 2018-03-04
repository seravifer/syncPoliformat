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
import java.io.IOException
import java.util.function.BiFunction

class SubjectComponent @Throws(IOException::class)
constructor(private val subject: SubjectInfo, private val fileService: FileService = FileServiceImpl(DataRepository(Poliformat, Intranet), SubjectServiceImpl(DataRepository(Poliformat, Intranet)))) : AnchorPane() {

    @FXML
    private lateinit var longNameID: Label

    @FXML
    private lateinit var nameID: Label

    @FXML
    private lateinit var dateID: Label

    @FXML
    private lateinit var donwloadID: SVGPath

    @FXML
    private lateinit var loadingID: JFXSpinner

    @FXML
    private lateinit var circleID: Circle
    private val colors = arrayOf("#ad1457", "#6a1b9a", "#4527a0", "#283593", "#1565c0", "#0277bd", "#00838f", "#00695c", "#2e7d32", "#558b2f", "#9e9d24", "#f9a825", "#ff8f00", "#ef6c00", "#d84315")

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/view/subject.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        fxmlLoader.load<Any>()

        nameID.text = subject.shortName
        longNameID.text = subject.name
        dateID.text = formatLastUpdate(subject.lastUpdate)
        circleID.fill = Color.web(colors[Utils.random(1, colors.size - 1)])

        donwloadID.setOnMouseClicked { update() }
        if (!subject.lastUpdate.isEmpty())
            donwloadID.content = "M12,4C15.64,4 18.67,6.59 19.35,10.04C21.95,10.22 24,12.36 24,15A5,5 0 0,1 19,20H6A6,6 0 0,1 0,14C0,10.91 2.34,8.36 5.35,8.04C6.6,5.64 9.11,4 12,4M7.5,9.69C6.06,11.5 6.2,14.06 7.82,15.68C8.66,16.5 9.81,17 11,17V18.86L13.83,16.04L11,13.21V15C10.34,15 9.7,14.74 9.23,14.27C8.39,13.43 8.26,12.11 8.92,11.12L7.5,9.69M9.17,8.97L10.62,10.42L12,11.79V10C12.66,10 13.3,10.26 13.77,10.73C14.61,11.57 14.74,12.89 14.08,13.88L15.5,15.31C16.94,13.5 16.8,10.94 15.18,9.32C14.34,8.5 13.19,8 12,8V6.14L9.17,8.97Z"
    }

    private fun formatLastUpdate(lastUpdate: String): String {
        return if (lastUpdate.isEmpty()) String("Todavía no ha sido sincronizado.".toByteArray()) else lastUpdate
    }

    fun sync() {
        update()
    }

    private fun update() {
        donwloadID.isVisible = false
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
        donwloadID.isVisible = true
        loadingID.isVisible = false
        dateID.text = formatLastUpdate(subject.lastUpdate)
        donwloadID.content = "M12,4C15.64,4 18.67,6.59 19.35,10.04C21.95,10.22 24,12.36 24,15A5,5 0 0,1 19,20H6A6,6 0 0,1 0,14C0,10.91 2.34,8.36 5.35,8.04C6.6,5.64 9.11,4 12,4M7.5,9.69C6.06,11.5 6.2,14.06 7.82,15.68C8.66,16.5 9.81,17 11,17V18.86L13.83,16.04L11,13.21V15C10.34,15 9.7,14.74 9.23,14.27C8.39,13.43 8.26,12.11 8.92,11.12L7.5,9.69M9.17,8.97L10.62,10.42L12,11.79V10C12.66,10 13.3,10.26 13.77,10.73C14.61,11.57 14.74,12.89 14.08,13.88L15.5,15.31C16.94,13.5 16.8,10.94 15.18,9.32C14.34,8.5 13.19,8 12,8V6.14L9.17,8.97Z"
    }

    companion object : KLogging()

}
