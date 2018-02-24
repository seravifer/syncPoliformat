package controller

import com.jfoenix.controls.JFXSpinner
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.SVGPath
import model.SubjectInfo
import utils.Utils

import java.io.IOException

class SubjectComponent @Throws(IOException::class)
constructor(private val subject: SubjectInfo) : AnchorPane() {

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

        donwloadID.setOnMouseClicked { _ -> update() }
    }

    private fun formatLastUpdate(lastUpdate: String): String {
        return if (lastUpdate == "") String("Todavía no ha sido sincronizado.".toByteArray()) else lastUpdate
    }

    fun sync() {
        update()
    }

    private fun update() {
        donwloadID.isVisible = false
        loadingID.isVisible = true
        dateID.text = "Descargando asignatura..."

        subject.manager
                .syncFiles()
                .onSucceeded { finish() }
                .onFailed { finish() }
                .toThread(name = "Sync-Download-Files-Thread").start()
    }

    private fun finish() {
        donwloadID.isVisible = true
        loadingID.isVisible = false
        subject.manager.updateLastUpdate(Utils.now())
        dateID.text = subject.lastUpdate
    }

}
