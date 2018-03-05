package controller

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXPasswordField
import com.jfoenix.controls.JFXProgressBar
import data.DataRepository
import data.network.BadCredentialsException
import data.network.Intranet
import data.network.Poliformat
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
import mu.KLogging
import service.AuthenticationService
import service.impl.SiteServiceImpl
import utils.JavaFXExecutor
import java.net.URL
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.BiFunction

class LoginController(
        private val authService: AuthenticationService,
        private val stage: Stage
) : Initializable {

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

        authService.login(usernameID.text, passwordID.text, rememberID.isSelected)
                .thenCompose { loggedIn ->
                    loginID.isDisable = false
                    loadingID.isVisible = false
                    if (loggedIn) authService.currentUser()
                    else {
                        errorID.isVisible = true
                        passwordID.text = ""
                        CompletableFuture<UserInfo>().apply {
                            completeExceptionally(BadCredentialsException())
                        }
                    }
                }.handleAsync(BiFunction<UserInfo, Throwable?, Unit> { user, e ->
                    if (e == null) {
                        showHome(user)
                    } else {
                        logger.error(e) {
                            if (e is BadCredentialsException) e.message
                            else "El usuario no tiene conexión a internet.\n"
                        }
                    }
                }, JavaFXExecutor)
    }

    private fun showHome(user: UserInfo) {
        stage.hide()

        val siteService = SiteServiceImpl(DataRepository(Poliformat, Intranet))
        HomeController(siteService, authService, stage, user)
    }

    companion object : KLogging()
}