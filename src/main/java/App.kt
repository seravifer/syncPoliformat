import controller.LoginController
import data.DataRepository
import data.network.Intranet
import data.network.Poliformat
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.scene.text.Font
import javafx.stage.Stage
import service.impl.AuthenticationServiceImpl
import utils.Settings
import java.awt.*
import javax.swing.SwingUtilities

class App : Application() {

    private lateinit var stage: Stage

    override fun start(primaryStage: Stage) {
        Settings.initFolders()
        stage = primaryStage

        val authService = AuthenticationServiceImpl(DataRepository(Poliformat, Intranet), Intranet)

        LoginController(authService, stage)

        loadFonts()

        primaryStage.title = "syncPoliformat"
        primaryStage.isResizable = false
        primaryStage.icons += Image(javaClass.getResource("/res/icon-64.png").toString())
        primaryStage.show()

        Platform.setImplicitExit(false)
        SwingUtilities.invokeLater { this.trayIcon() }

        if (Settings.checkVersion()) {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Nueva versión disponible"
            alert.headerText = null
            alert.contentText = "Hemos detectado que existe una nueva versión disponible de la aplicación. " +
                    "Por favor descarguela de nuestra páguina web para poder garantizar su correcto funcionamiento."
            alert.showAndWait()
        }
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

        displayMenu.addActionListener { Platform.runLater { this.showStage() } }
        trayIcon.addActionListener { Platform.runLater { this.showStage() } }
        exitItem.addActionListener { System.exit(0) }

        trayIcon.popupMenu = popup
        try {
            tray.add(trayIcon)
        } catch (e: AWTException) {
            e.printStackTrace()
        }
    }

    private fun loadFonts() {
        val fonts = arrayOf("Black", "Bold", "Light", "Medium", "Regular", "Thin")
        for (font in fonts) {
            Font.loadFont(javaClass.getResource("/css/fonts/Roboto-$font.ttf").toExternalForm(), 14.0)
            Font.loadFont(javaClass.getResource("/css/fonts/Roboto-${font}Italic.ttf").toExternalForm(), 14.0)
        }
    }

    private fun appleDockIcon() {
        val util = Class.forName("com.apple.eawt.Application")
        val getApplication = util.getMethod("getApplication")
        val application = getApplication.invoke(util)
        val setDockIconImage = util.getMethod("setDockIconImage", Image::class.java)
        val url = javaClass.getResource("res/icon-1024.png")
        val image = Toolkit.getDefaultToolkit().getImage(url)
        setDockIconImage.invoke(application, image)
    }

    private fun showStage() {
        stage.show()
        stage.toFront()
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
