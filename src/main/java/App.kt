import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import data.model.User
import javafx.application.Platform
import javafx.scene.text.Font
import utils.Settings
import java.awt.*

import java.io.IOException
import java.net.URISyntaxException
import javax.swing.SwingUtilities

class App : Application() {

    private val user = User()
    private lateinit var stage: Stage

    @Throws(IOException::class, URISyntaxException::class)
    override fun start(primaryStage: Stage) {
        Settings.initFolders()
        stage = primaryStage

        val root: Parent = if (user.checkLogin()) {
            user.silentLogin()

            val loader = FXMLLoader(javaClass.getResource("/view/home.fxml"))
            val parent = loader.load<Parent>()

            // TODO: Dejar de delegar la construccion del controlador a JavaFx y instanciarlo mediante constructor
            //loader.getController<HomeController>().init(user)
            parent
        } else {
            FXMLLoader.load<Parent>(javaClass.getResource("view/login.fxml"))
        }

        val scene = Scene(root)
        scene.stylesheets.add(javaClass.getResource("/css/style.css").toString())
        loadFonts()

        primaryStage.scene = scene
        primaryStage.title = "syncPoliformat"
        primaryStage.isResizable = false
        primaryStage.show()

        Platform.setImplicitExit(false)
        SwingUtilities.invokeLater { this.trayIcon() }

        /*if (Utils.checkVersion()) {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Nueva versión disponible"
            alert.headerText = null
            alert.contentText = "Hemos detectado que existe una nueva versión disponible de la aplicación. " + "Por favor descarguela de nuestra páguina web para poder garantizar su correcto funcionamiento."
            alert.showAndWait()
        }*/
    }

    private fun trayIcon() {
        if (!SystemTray.isSupported()) {
            System.err.println("SystemTray is not supported")
            return
        }

        val image = Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/res/tray-icon.png"))

        val popup = PopupMenu()
        val trayIcon = TrayIcon(image)
        val tray = SystemTray.getSystemTray()

        val displayMenu = MenuItem("Open")
        val exitItem = MenuItem("Exit")

        popup.add(displayMenu)
        popup.addSeparator()
        popup.add(exitItem)

        displayMenu.addActionListener { _ -> Platform.runLater { this.showStage() } }
        trayIcon.addActionListener { _ -> Platform.runLater { this.showStage() } }
        exitItem.addActionListener { _ -> System.exit(0) }

        trayIcon.popupMenu = popup
        try {
            tray.add(trayIcon)
        } catch (e: AWTException) {
            e.printStackTrace()
        }

    }

    private fun loadFonts() {
        val fonts = arrayOf("Black", "Bold", "Light", "Medium", "Thin")
        for (font in fonts) {
            Font.loadFont(javaClass.getResource("/css/fonts/Roboto-$font.ttf").toExternalForm(), 14.0)
            Font.loadFont(javaClass.getResource("/css/fonts/Roboto-" + font + "Italic.ttf").toExternalForm(), 14.0)
        }

        Font.loadFont(javaClass.getResource("/css/fonts/Roboto-Regular.ttf").toExternalForm(), 14.0)
        Font.loadFont(javaClass.getResource("/css/fonts/Roboto-Italic.ttf").toExternalForm(), 14.0)
    }

    private fun showStage() {
        stage.show()
        stage.toFront()
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
