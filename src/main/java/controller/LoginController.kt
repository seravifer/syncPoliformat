package controller

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXPasswordField
import com.jfoenix.controls.JFXProgressBar
import data.DataRepository
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import data.network.Intranet
import data.network.Poliformat
import domain.UserInfo
import mu.KLogging
import service.AuthenticationService
import service.impl.AuthenticationServiceImpl
import utils.JavaFXExecutor

import java.net.URL
import java.util.ResourceBundle
import java.util.function.BiFunction

class LoginController(private val authService: AuthenticationService = AuthenticationServiceImpl(DataRepository(Poliformat), Intranet)) : Initializable {

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

    @FXML
    override fun initialize(location: URL, resources: ResourceBundle?) {
        usernameID.textProperty().addListener { _, oldValue, _ ->
            if (usernameID.text.length > 8 || !usernameID.text.matches("[0-9]*".toRegex())) {
                usernameID.text = oldValue
            }
        }

        rememberID.isDisable = true
        sceneID.setOnKeyPressed { event -> if (event.code == KeyCode.ENTER) loginID.fire() }
    }

    @FXML
    private fun login() {
        loginID.isDisable = true
        loadingID.isVisible = true

        authService.login(usernameID.text, passwordID.text, rememberID.isSelected)
                .thenCompose { loggedIn ->
                    if (loggedIn) {
                        logger.info { "Logged In" }
                    } else {
                        errorID.isVisible = true
                        passwordID.text = ""
                    }
                    loginID.isDisable = false
                    loadingID.isVisible = false
                    authService.currentUser()
                }.handleAsync(BiFunction<UserInfo, Throwable?, Unit> { user, e ->
                    if (e == null) {
                        showHome(user)
                    } else {
                        logger.error(e) { "El usuario no tiene conexión a internet.\n" }
                    }
                }, JavaFXExecutor)
    }

    private fun showHome(user: UserInfo) {
        (loginID.scene.window as Stage).close()

        val stage = Stage()
        val loader = FXMLLoader(javaClass.getResource("/view/home.fxml"))
        val sceneMain = loader.load<Parent>()

        // TODO: Dejar de delegar la construccion del controlador a JavaFx y instanciarlo mediante constructor
        val controller = loader.getController<HomeController>()
        controller.init(user)

        val scene = Scene(sceneMain)
        scene.stylesheets.add(javaClass.getResource("/css/style.css").toString())

        stage.scene = scene
        stage.title = "syncPoliformat"
        stage.isResizable = false
        stage.show()
    }

    companion object : KLogging()
}