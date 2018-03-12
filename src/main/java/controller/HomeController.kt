package controller

import domain.SubjectInfo
import domain.UserInfo
import dorkbox.systemTray.SystemTray
import dorkbox.util.Desktop
import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.SimpleBooleanProperty
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
import service.AuthenticationService
import service.SiteService
import utils.JavaFXExecutor
import utils.Settings
import utils.Utils
import java.net.URL
import java.util.*
import java.util.function.BiFunction

class HomeController(
        private val siteService: SiteService,
        private val authService: AuthenticationService,
        private val stage: Stage,
        private val user: UserInfo
) : Initializable {

    @FXML
    private lateinit var nameID: Label

    @FXML
    private lateinit var mailID: Label

    @FXML
    private lateinit var settingsID: SVGPath

    @FXML
    private lateinit var listID: VBox

    var updating: BooleanBinding = Bindings.and(SimpleBooleanProperty(false), SimpleBooleanProperty(false))

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
                        .forEach {
                            val component = SubjectComponent(it)
                            updating = updating.or(component.updating)
                            listID.children.add(component)
                        }
            } else {
                logger.error(e) { "Error al recuperar las asignaturas.\n" }
            }
        }, JavaFXExecutor)

        val contextMenu = ContextMenu()

        val item1 = MenuItem("Sobre nosotros")
        item1.setOnAction { launchAbout() }

        val item2 = MenuItem("Feedback...")
        item2.setOnAction { sendFeedbak() }

        val item3 = MenuItem("Cerrar sesión")
        item3.setOnAction { launchSettings() }

        val item4 = MenuItem("Salir")
        item4.setOnAction {
            if (!Utils.isWindows) SystemTray.get().shutdown()
            System.exit(0)
        }

        contextMenu.items.addAll(item1, item2, SeparatorMenuItem(), item3, item4)

        settingsID.setOnMouseClicked { contextMenu.show(settingsID, Side.LEFT, 0.0, 0.0) }
    }

    @FXML
    private fun openFolder() {
        Desktop.browseDirectory(Settings.poliformatDirectory.toString())
    }

    @FXML
    private fun openWeb() {
        Desktop.browseURL("https://poliformat.upv.es/portal")
    }

    @FXML
    private fun updateAll() {
        listID.children.asSequence()
                .filterIsInstance(SubjectComponent::class.java)
                .forEach { it.sync() }
    }

    private fun launchAbout() {
        val stage = Stage()
        val root: Parent? = FXMLLoader.load<Parent>(javaClass.getResource("/view/about.fxml"))
        val scene = Scene(root!!)
        stage.scene = scene
        stage.title = "syncPoliformat"
        stage.icons += Image(javaClass.getResource("/img/icon-64.png").toString())
        stage.isResizable = false
        stage.show()
    }

    // TODO: Añadir dialogo de aviso de que se cerrará sesión al terminar de actualizar.
    private fun launchSettings() {
        fun logout() {
            stage.hide()
            authService.logout()
            LoginController(authService, stage)
        }
        if (updating.value) updating.addListener { _, _, updating -> if (!updating) logout() }
        else logout()
    }

    private fun sendFeedbak() {
        Desktop.browseURL("https://docs.google.com/forms/d/e/1FAIpQLSeusf0F2u98Vn28xH7OE3BF6BlMl7ZCKPEdxo2MTqvO-3LlMg/viewform")
    }

    companion object : KLogging()
}