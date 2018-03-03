package controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.scene.shape.SVGPath
import data.model.PoliformatApi
import domain.SubjectInfo
import data.model.User
import javafx.fxml.FXMLLoader
import javafx.geometry.Side
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.stage.Stage
import utils.Settings

import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.ResourceBundle

class HomeController : Initializable {

    @FXML
    private lateinit var nameID: Label

    @FXML
    private lateinit var mailID: Label

    @FXML
    private lateinit var settingsID: SVGPath

    @FXML
    private lateinit var listID: VBox

    @FXML
    override fun initialize(location: URL, resources: ResourceBundle?) {}

    @Throws(IOException::class)
    fun init(user: User) {
        with(user) {
            nameID.text = "$nameUser $lastNameUser"
            mailID.text = mailUser
        }

        // TODO: Proveer del servicio de asignaturas a través del constructor
        val poliformat = PoliformatApi()

        poliformat.fetchSubjectsInfo().onSucceeded { subjects ->
            poliformat.persistLastUpdateSubjectsDate(subjects).onSucceeded {
                subjects.sortedBy(SubjectInfo::name)
                        .forEach { listID.children.add(SubjectComponent(it)) }
            }.toThread(name = "Persist-Subjects-Last-Update", isDaemon = false).start()
        }.toThread(name = "Fetch-Subjects-Info").start()

        val contextMenu = ContextMenu()

        val item1 = MenuItem("Nosotros")
        item1.setOnAction { e -> launchAbout() }

        val item3 = MenuItem("Reportar error")
        item3.setOnAction { e -> sendFeedbak() }

        val item5 = MenuItem("Sincronizar")
        item5.setOnAction { e -> updateAll() }

        //MenuItem item2 = new MenuItem("Preferences");
        val item2 = MenuItem("Cerrar sesión")
        item2.setOnAction { _ -> launchSettings() }

        val item6 = MenuItem("Salir")
        item6.setOnAction { _ -> System.exit(0) }

        contextMenu.items.addAll(item1, item3, SeparatorMenuItem(), item5, item2, SeparatorMenuItem(), item6)

        settingsID.setOnMouseClicked { e -> contextMenu.show(settingsID, Side.LEFT, 0.0, 0.0) }
    }

    @FXML
    private fun updateAll() {
        listID.children.asSequence()
                .filterIsInstance(SubjectComponent::class.java)
                .forEach { it.sync() }
    }

    @FXML
    @Throws(IOException::class)
    private fun openFolder(event: MouseEvent) {
        try {
            Desktop.getDesktop().open(Settings.poliformatDirectory)
        } catch (e: IOException) {
            System.err.println("No se ha encontrado ninguna carpeta.")
            e.printStackTrace()
        }

    }

    @FXML
    @Throws(IOException::class, URISyntaxException::class)
    private fun openWeb(event: MouseEvent) {
        Desktop.getDesktop().browse(URI("https://poliformat.upv.es/portal"))
    }

    private fun launchAbout() {
        val stage = Stage()
        var root: Parent? = null
        try {
            root = FXMLLoader.load<Parent>(javaClass.getResource("/view/about.fxml"))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val scene = Scene(root!!)
        stage.scene = scene
        stage.title = "About"
        stage.isResizable = false
        stage.show()
    }

    private fun launchSettings() {
        TODO("not implemented")
    }

    private fun sendFeedbak() {
        TODO("not implemented")
    }

}