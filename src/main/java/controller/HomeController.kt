package controller

import domain.SubjectInfo
import domain.UserInfo
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.geometry.Side
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import javafx.scene.shape.SVGPath
import javafx.stage.Stage
import mu.KLogging
import service.SiteService
import utils.JavaFXExecutor
import utils.Settings
import java.awt.Desktop
import java.net.URI
import java.net.URL
import java.util.*
import java.util.function.BiFunction

class HomeController(private val siteService: SiteService, private val stage: Stage, private val user: UserInfo) : Initializable {

    @FXML
    private lateinit var nameID: Label

    @FXML
    private lateinit var mailID: Label

    @FXML
    private lateinit var settingsID: SVGPath

    @FXML
    private lateinit var listID: VBox

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/view/home.fxml"))
        fxmlLoader.setController(this)
        val parent = fxmlLoader.load<Parent>()

        val scene = Scene(parent)
        scene.stylesheets.add(javaClass.getResource("/css/style.css").toString())

        stage.scene = scene
        stage.show()
    }

    @FXML
    override fun initialize(location: URL, resources: ResourceBundle?) {
        with(user) {
            nameID.text = displayName
            mailID.text = email
        }

        siteService.getSubjects().handleAsync(BiFunction<List<SubjectInfo>, Throwable?, Any> { subjects, e ->
            if (e == null) {
                subjects.sortedBy(SubjectInfo::name)
                        .forEach { listID.children.add(SubjectComponent(it)) }
            } else {
                logger.error(e) { "Error al recuperar las asignaturas.\n" }
            }
        }, JavaFXExecutor)

        val contextMenu = ContextMenu()

        val item1 = MenuItem("Nosotros")
        item1.setOnAction { launchAbout() }

        val item3 = MenuItem("Reportar error")
        item3.setOnAction { sendFeedbak() }

        val item5 = MenuItem("Sincronizar")
        item5.setOnAction { updateAll() }

        //MenuItem item2 = new MenuItem("Preferences");
        val item2 = MenuItem("Cerrar sesión")
        item2.setOnAction { launchSettings() }

        val item6 = MenuItem("Salir")
        item6.setOnAction { System.exit(0) }

        contextMenu.items.addAll(item1, item3, SeparatorMenuItem(), item5, item2, SeparatorMenuItem(), item6)

        settingsID.setOnMouseClicked { contextMenu.show(settingsID, Side.LEFT, 0.0, 0.0) }
    }

    @FXML
    private fun openFolder() {
        Desktop.getDesktop().open(Settings.poliformatDirectory)
    }

    @FXML
    private fun openWeb() {
        Desktop.getDesktop().browse(URI("https://poliformat.upv.es/portal"))
    }

    private fun launchAbout() {
        val stage = Stage()
        val root: Parent? = FXMLLoader.load<Parent>(javaClass.getResource("/view/about.fxml"))
        val scene = Scene(root!!)
        stage.scene = scene
        stage.title = "About syncPoliformat"
        stage.icons += Image(javaClass.getResource("/res/icon-64.png").toString())
        stage.isResizable = false
        stage.show()
    }

    private fun launchSettings() {
        TODO("not implemented")
    }

    private fun sendFeedbak() {
        TODO("not implemented")
    }

    private fun updateAll() {
        listID.children.asSequence()
                .filterIsInstance(SubjectComponent::class.java)
                .forEach { it.sync() }
    }

    companion object : KLogging()
}