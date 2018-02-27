import controller.HomeController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import data.model.User

import java.io.IOException
import java.net.URISyntaxException

class App : Application() {

    private val user = User()

    @Throws(IOException::class, URISyntaxException::class)
    override fun start(primaryStage: Stage) {
        if (user.checkLogin()) {
            user.silentLogin()

            val loader = FXMLLoader(javaClass.getResource("/view/home.fxml"))
            val sceneMain = loader.load<Parent>()

            // TODO: Dejar de delegar la construccion del controlador a JavaFx y instanciarlo mediante constructor
            val controller = loader.getController<HomeController>()
            controller.init(user)

            val scene = Scene(sceneMain)
            scene.stylesheets.add(javaClass.getResource("/css/style.css").toString())

            primaryStage.scene = scene
            primaryStage.title = "syncPoliformat"
            primaryStage.isResizable = false
            primaryStage.show()
        } else {
            val root = FXMLLoader.load<Parent>(javaClass.getResource("view/login.fxml"))

            val scene = Scene(root)
            scene.stylesheets.add(javaClass.getResource("/css/style.css").toString())

            //Font.loadFont(getClass().getResource("/css/Roboto.ttf").toExternalForm(), 14);

            primaryStage.scene = scene
            primaryStage.title = "syncPoliformat"
            primaryStage.isResizable = false
            primaryStage.show()
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
