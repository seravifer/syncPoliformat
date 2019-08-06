package controller

import domain.SubjectInfo
import domain.UserInfo
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
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import javafx.scene.shape.SVGPath
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mu.KLogging
import service.AuthenticationService
import service.SiteService
import java.io.File
import java.net.URL
import java.util.ResourceBundle
import kotlin.coroutines.CoroutineContext

class HomeController(
        private val siteService: SiteService,
        private val authService: AuthenticationService,
        private val stage: Stage,
        private val user: UserInfo,
        private val subjectComponentFactory: (SubjectInfo) -> SubjectComponent,
        private val navigationHandler: NavigationHandler,
        private val poliformatFolder: File
) : Initializable, Controller, CoroutineScope {

    @FXML
    private lateinit var nameID: Label

    @FXML
    private lateinit var mailID: Label

    @FXML
    private lateinit var settingsID: SVGPath

    @FXML
    private lateinit var listID: VBox

    private val scene: Scene

    private val parentJob = Job()
    override val coroutineContext: CoroutineContext = parentJob + Dispatchers.Main

    var updating: BooleanBinding = Bindings.and(SimpleBooleanProperty(false), SimpleBooleanProperty(false))

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/view/home.fxml"))
        fxmlLoader.setController(this)
        val parent = fxmlLoader.load<Parent>()

        scene = Scene(parent)
        scene.stylesheets.add(javaClass.getResource("/css/style.css").toString())
    }

    override fun show(changeScene: Boolean) {
        if (changeScene) {
            stage.hide()
            stage.scene = scene
        }
        stage.show()
        if (!changeScene) {
            stage.toFront()
        }
    }

    override fun hide() {
        stage.hide()
    }

    @FXML
    override fun initialize(location: URL, resources: ResourceBundle?) {
        with(user) {
            nameID.text = displayName
            mailID.text = email
        }

        setupSubjectList()
        setupOptionsContextMenu()
    }

    private fun setupSubjectList() = launch {
        try {
            val subjects = siteService.getSubjects()
            subjects.sortedBy(SubjectInfo::name).forEach {
                val component = subjectComponentFactory(it)
                updating = updating.or(component.updating)
                listID.children.add(component)
            }
        } catch (e: Exception) {
            logger.error(e) { "Error al recuperar las asignaturas.\n" }
        }
    }

    private fun setupOptionsContextMenu() {
        with(ContextMenu()) {
            items.addAll(
                    MenuItem("Sobre nosotros").apply { setOnAction { launchAbout() } },
                    MenuItem("Feedback...").apply { setOnAction { sendFeedbak() } },
                    SeparatorMenuItem(),
                    MenuItem("Cerrar sesión").apply {
                        setOnAction {
                            isDisable = true
                            logoutFromMenu().invokeOnCompletion { isDisable = false }
                        }
                    },
                    MenuItem("Salir").apply { setOnAction { launch { navigationHandler.send(Exit) } } })
            settingsID.setOnMouseClicked { show(settingsID, Side.LEFT, 0.0, 0.0) }
        }
    }

    @FXML
    private fun openFolder() {
        Desktop.browseDirectory(poliformatFolder.toString())
    }

    @FXML
    private fun openWeb() {
        Desktop.browseURL("https://poliformat.upv.es/portal")
    }

    private fun sendFeedbak() {
        Desktop.browseURL("https://docs.google.com/forms/d/e/1FAIpQLSeusf0F2u98Vn28xH7OE3BF6BlMl7ZCKPEdxo2MTqvO-3LlMg/viewform")
    }

    @FXML
    private fun updateAll() {
        listID.children.asSequence()
                .filterIsInstance(SubjectComponent::class.java)
                .forEach { it.sync() }
    }

    private fun launchAbout() {
        val stage = Stage()
        val root: Parent = FXMLLoader.load(javaClass.getResource("/view/about.fxml"))
        val scene = Scene(root)
        stage.scene = scene
        stage.title = "syncPoliformat"
        stage.icons += Image(javaClass.getResource("/img/icon-64.png").toString())
        stage.isResizable = false
        stage.show()
    }

    private fun logoutFromMenu(): Job {
        val job = Job()
        fun logout() = launch {
            authService.logout()
            navigationHandler.send(if (stage.isShowing) Login else Exit)
        }.invokeOnCompletion {
            logger.debug { "Completed logout coroutine" }
            if (it == null) job.complete() else job.completeExceptionally(it)
        }
        try {
            if (updating.value) {
                if (showExitDialog()) {
                    updating.addListener { _, _, updating ->
                        if (!updating) {
                            logout()
                        }
                    }
                } else {
                    logger.debug { "Completed logout coroutine" }
                    job.complete()
                }
            } else logout()
        } catch (t: Throwable) {
            logger.error(t) { "Completed logout coroutine" }
            job.completeExceptionally(t)
        }
        return job
    }

    companion object : KLogging()
}
