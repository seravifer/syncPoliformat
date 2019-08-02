package controller

import appComponent
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXPasswordField
import com.jfoenix.controls.JFXProgressBar
import data.network.BadCredentialsException
import domain.UserInfo
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mu.KLogging
import org.kodein.di.Multi2
import org.kodein.di.direct
import org.kodein.di.generic.M
import org.kodein.di.generic.instance
import service.AuthenticationService
import java.net.URL
import java.util.*
import kotlin.coroutines.CoroutineContext

class LoginController(
        private val authService: AuthenticationService,
        private val stage: Stage
) : Initializable, CoroutineScope {

    @FXML
    private lateinit var usernameID: JFXPasswordField

    @FXML
    private lateinit var passwordID: JFXPasswordField

    @FXML
    private lateinit var rememberID: JFXCheckBox

    @FXML
    private lateinit var loginID: JFXButton

    @FXML
    private lateinit var errorID: Label

    @FXML
    private lateinit var loadingID: JFXProgressBar

    @FXML
    private lateinit var sceneID: AnchorPane

    private val parentJob = Job()
    override val coroutineContext: CoroutineContext = parentJob + Dispatchers.Main

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/view/login.fxml"))
        fxmlLoader.setController(this)
        val parent = fxmlLoader.load<Parent>()

        val scene = Scene(parent)
        scene.stylesheets.add(javaClass.getResource("/css/style.css").toString())

        stage.scene = scene
        stage.show()
    }

    @FXML
    override fun initialize(location: URL, resources: ResourceBundle?) {
        usernameID.textProperty().addListener { _, oldValue, _ ->
            if (usernameID.text.length > 8 || !usernameID.text.matches("[0-9]*".toRegex())) {
                usernameID.text = oldValue
            }
        }

        sceneID.setOnKeyPressed { event -> if (event.code == KeyCode.ENTER) loginID.fire() }
    }

    @FXML
    private fun login() {
        loginID.isDisable = true
        loadingID.isVisible = true

        launch {
            try {
                val loggedIn = authService.login(usernameID.text, passwordID.text, rememberID.isSelected)
                loginID.isDisable = false
                loadingID.isVisible = false
                if (loggedIn) {
                    logger.debug { "Signed in!" }
                    val user = authService.currentUser()
                    stage.hide()
                    appComponent.direct.instance<Multi2<Stage, UserInfo>, HomeController>(arg = M(stage, user))
                } else {
                    errorID.isVisible = true
                    passwordID.text = ""
                    throw BadCredentialsException()
                }
            } catch (e: Exception) {
                logger.error(e) {
                    if (e is BadCredentialsException) e.message
                    else "El usuario no tiene conexión a internet.\n"
                }
            }
        }
    }

    companion object : KLogging()
}
