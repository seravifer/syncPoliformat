package controller

import com.jfoenix.controls.JFXRippler
import domain.SubjectInfo
import domain.UserInfo
import dorkbox.util.Desktop
import javafx.animation.ScaleTransition
import javafx.animation.SequentialTransition
import javafx.animation.Transition
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
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
import javafx.util.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mu.KLogging
import service.AuthenticationService
import service.SiteService
import utils.fadein
import utils.fadeout
import java.io.File
import java.net.URL
import java.util.ResourceBundle
import kotlin.coroutines.CoroutineContext

class HomeController(
        private val siteService: SiteService,
        private val authService: AuthenticationService,
        private val stage: Stage,
        private val user: UserInfo,
        private val asSubjectComponent: (SubjectInfo) -> SubjectComponent,
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
    private val subjectComponents = FXCollections.observableArrayList<SubjectComponent> {
        arrayOf(it.updating)
    }

    private val root: Parent

    private val parentJob = Job()
    override val coroutineContext: CoroutineContext = parentJob + Dispatchers.Main

    private val subjectListInitJob = setupSubjectList()
    private var subjectsAttached = false

    private var updating: BooleanBinding = SimpleBooleanProperty(true).not()

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/view/home.fxml"))
        fxmlLoader.setController(this)
        root = fxmlLoader.load()

        if (stage.scene?.root == null) {
            val scene = Scene(root)
            scene.stylesheets.add(javaClass.getResource("/css/style.css").toString())
            stage.scene = scene
        }
    }

    @FXML
    override fun initialize(location: URL, resources: ResourceBundle?) {
        subjectListInitJob.start()

        with(user) {
            nameID.text = displayName
            mailID.text = email
        }

        setupOptionsContextMenu()
    }

    override fun show(changeScene: Boolean) {
        val firstTransition = prepareTransition(changeScene)
        stage.show()
        firstTransition.play()
        if (!changeScene) {
            stage.toFront()
        }
    }

    private fun prepareTransition(changeScene: Boolean): Transition {
        val fadein = fadein(Duration.millis(300.0), root)
        root.opacity = 0.0

        var subjectTransitionMaybe: Transition? = null
        val firstTransition = if (changeScene && stage.scene?.root !== root) {
            val fadeout = fadeout(Duration.millis(300.0), stage.scene.root)
            fadeout.onFinished = EventHandler {
                logger.debug { "Previuos view fadeout" }
                if (subjectsAttached) {
                    subjectTransitionMaybe = expandSubjectComponents()
                }
                stage.scene.root = root
                fadein.play()
            }
            fadeout
        } else fadein


        if (subjectsAttached && !changeScene) {
            subjectTransitionMaybe = expandSubjectComponents()
        }
        fadein.onFinished = EventHandler {
            logger.debug { "Home fadein executed" }
            subjectListInitJob.invokeOnCompletion {
                logger.debug { "Subject list view initialized" }
                val subjectTransition = subjectTransitionMaybe ?: expandSubjectComponents()
                attachSubjects()
                subjectTransition.play()
            }
        }
        return firstTransition
    }

    private fun expandSubjectComponents(): Transition {
        return subjectComponents.map {
            val expand = ScaleTransition(Duration.millis(150.0), it)
            expand.fromY = 0.0
            expand.toY = 1.0
            it.scaleY = 0.0
            expand
        }.fold(SequentialTransition()) { acc, fadein ->
            acc.apply { children.add(fadein) }
        }
    }

    override fun hide() {
        stage.hide()
    }

    private fun setupSubjectList() = launch(start = CoroutineStart.LAZY) {
        try {
            val subjects = siteService.getSubjects()
            subjectComponents.addAll(subjects.sortedBy(SubjectInfo::name).map(asSubjectComponent).onEach {
                updating = updating.or(it.updating)
                it.scaleY = 0.0
            })
        } catch (e: Exception) {
            logger.error(e) { "Error al recuperar las asignaturas.\n" }
        }
    }

    private fun attachSubjects() {
        if (!subjectsAttached) {
            listID.children.addAll(subjectComponents.map {
                JFXRippler(it).apply { ripplerFill = it.iconColorProperty.value.brighter() } })
            subjectsAttached = true
        }
    }

    private fun setupOptionsContextMenu() {
        with(ContextMenu()) {
            items.addAll(
                    MenuItem("Sobre nosotros").apply { onAction = EventHandler { launchAbout() } },
                    MenuItem("Feedback...").apply { onAction = EventHandler { sendFeedbak() } },
                    SeparatorMenuItem(),
                    MenuItem("Cerrar sesión").apply {
                        onAction = EventHandler {
                            isDisable = true
                            logoutFromMenu().invokeOnCompletion { isDisable = false }
                        }
                    },
                    MenuItem("Salir").apply { onAction = EventHandler { launch { navigationHandler.send(Exit) } } })
            settingsID.onMouseClicked = EventHandler { show(settingsID, Side.LEFT, 0.0, 0.0) }
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
        subjectComponents.forEach { it.sync() }
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
                    job.complete()
                }
            } else logout()
        } catch (t: Throwable) {
            job.completeExceptionally(t)
        }
        return job
    }

    private fun showExitDialog(): Boolean {
        val alert = Alert(Alert.AlertType.WARNING,
                "Aún se están sincronizando archivos\n¿Quieres que se cierre la sesion al finalizar?",
                ButtonType.YES, ButtonType.NO)
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES
    }

    companion object : KLogging()
}
