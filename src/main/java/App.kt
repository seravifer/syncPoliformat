import controller.HomeController
import controller.LoginController
import data.DataRepository
import data.network.CookieJarImpl
import data.network.CredentialsStorageImpl
import data.network.Intranet
import data.network.Poliformat
import domain.UserInfo
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.scene.text.Font
import javafx.stage.Stage
import mu.KLogging
import service.impl.AuthenticationServiceImpl
import service.impl.SiteServiceImpl
import utils.JavaFXExecutor
import utils.Settings
import utils.Utils
import java.awt.*
import java.util.function.BiFunction
import javax.swing.SwingUtilities

class App : Application() {

    private lateinit var stage: Stage

    override fun start(primaryStage: Stage) {
        Settings.initFolders()
        stage = primaryStage

        val authService = AuthenticationServiceImpl(DataRepository(Poliformat, Intranet), Poliformat, Intranet, CredentialsStorageImpl, CookieJarImpl)

        if (authService.existSavedCredentials()) {
            authService.login().thenCompose {
                authService.currentUser()
            }.handleAsync(BiFunction<UserInfo, Throwable?, Unit> { user, e ->
                if (e == null) {
                    val siteService = SiteServiceImpl(DataRepository(Poliformat, Intranet), Settings.subjectsFile)
                    HomeController(siteService, authService, stage, user)
                } else {
                    authService.logout()
                    LoginController(authService, stage)
                }
            }, JavaFXExecutor)
        } else {
            LoginController(authService, stage)
        }

        loadFonts()

        primaryStage.title = "syncPoliformat"
        primaryStage.isResizable = false
        primaryStage.icons += Image(javaClass.getResource("/img/icon-24.png").toString())
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

        val image = Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/img/tray-icon.png"))

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
        trayIcon.isImageAutoSize = true
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
        val setDockIconImage = appleLibrary.getMethod("setDockIconImage", java.awt.Image::class.java)
        val image: java.awt.Image = Toolkit.getDefaultToolkit().getImage(javaClass.getResource("img/icon-128.png"))
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
