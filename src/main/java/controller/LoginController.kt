package controller

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXPasswordField
import com.jfoenix.controls.JFXProgressBar
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import model.User

import java.io.IOException
import java.net.URL
import java.util.ResourceBundle

class LoginController : Initializable {

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

    private val user = User()

    @FXML
    override fun initialize(location: URL, resources: ResourceBundle?) {
        usernameID.textProperty()?.addListener { _, oldValue, _ ->
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

        Thread {
            try {
                user.login(usernameID.text, passwordID.text, rememberID.isSelected)
            } catch (e: Exception) {
                System.err.println("El usuario no tiene conexión a internet.")
                e.printStackTrace()
            }

            Platform.runLater { status(user.isLogged!!) }
        }.start()
    }

    private fun status(isLogged: Boolean) {
        try {
            if (isLogged) {
                (loginID.scene.window as Stage).close()

                val stage = Stage()
                val loader = FXMLLoader(javaClass.getResource("/view/home.fxml"))
                val sceneMain = loader.load<Parent>()

                val controller = loader.getController<HomeController>()
                controller.init(user)

                val scene = Scene(sceneMain)
                scene.stylesheets.add(javaClass.getResource("/css/style.css").toString())

                stage.scene = scene
                stage.title = "syncPoliformat"
                stage.isResizable = false
                stage.show()
            } else {
                errorID.isVisible = true
                passwordID.text = ""
            }

            loginID.isDisable = false
            loadingID.isVisible = false
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}