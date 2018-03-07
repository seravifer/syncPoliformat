import com.github.salomonbrys.kodein.factory
import com.github.salomonbrys.kodein.instance
import controller.HomeController
import controller.LoginController
import domain.UserInfo
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.scene.text.Font
import javafx.stage.Stage
import mu.KLogging
import service.AuthenticationService
import utils.JavaFXExecutor
import utils.Settings
import utils.Utils
import java.awt.*
import java.util.function.BiFunction
import javax.swing.SwingUtilities

class App : Application() {

    private lateinit var stage: Stage

    override fun start(primaryStage: Stage) {
        appModule.addImport(controllerModule(primaryStage))
        Settings.initFolders()
        stage = primaryStage

        val authService = appModule.instance<AuthenticationService>()

        if (authService.existSavedCredentials()) {
            authService.login().thenCompose {
                authService.currentUser()
            }.handleAsync(BiFunction<UserInfo, Throwable?, Unit> { user, e ->
                if (e == null) {
                    val homeFactory: (UserInfo) -> HomeController = appModule.factory()
                    homeFactory(user)
                } else {
                    authService.logout()
                    appModule.instance<LoginController>()
                }
            }, JavaFXExecutor)
        } else {
            appModule.instance<LoginController>()
        }

        loadFonts()

        primaryStage.title = "syncPoliformat"
        primaryStage.isResizable = false
        primaryStage.icons += Image(javaClass.getResource("/res/icon-64.png").toString())
        if (Utils.isMac) appleDockIcon()

        // TODO solo mantener abierta si estas en el HomeController
        Platform.setImplicitExit(false)
        SwingUtilities.invokeLater { trayIcon() }

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
            logger.warn { "SystemTray is not supported" }
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

        displayMenu.addActionListener { Platform.runLater { showStage() } }
        trayIcon.addActionListener { Platform.runLater { showStage() } }
        exitItem.addActionListener {
            tray.remove(trayIcon)
            System.exit(0)
        }

        trayIcon.popupMenu = popup
        tray.add(trayIcon)
    }

    private fun loadFonts() {
        val fonts = arrayOf("Black", "Bold", "Light", "Medium", "Regular", "Thin")
        for (font in fonts) {
            Font.loadFont(javaClass.getResource("/css/fonts/Roboto-$font.ttf").toExternalForm(), 14.0)
            Font.loadFont(javaClass.getResource("/css/fonts/Roboto-${font}Italic.ttf").toExternalForm(), 14.0)
        }
    }

    private fun appleDockIcon() {
        val appleLibrary = Class.forName("com.apple.eawt.Application")
        val application = appleLibrary.getMethod("getApplication").invoke(appleLibrary)
        val setDockIconImage = appleLibrary.getMethod("setDockIconImage", Image::class.java)
        val image = Toolkit.getDefaultToolkit().getImage(javaClass.getResource("res/icon-1024.png"))
        setDockIconImage.invoke(application, image)
    }

    private fun showStage() {
        stage.show()
        stage.toFront()
    }

    companion object : KLogging()
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
