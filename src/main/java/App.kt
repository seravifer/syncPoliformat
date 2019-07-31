import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
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
import utils.OS
import utils.Settings
import java.awt.*
import java.util.function.BiFunction


class App : Application(), KodeinAware {
    override val kodein: Kodein by lazy { appModule }

    private lateinit var stage: Stage

    override fun start(primaryStage: Stage) {
        appModule.addImport(controllerModule(primaryStage))
        Settings.initFolders(instance("app"),
                instance("poliformat"),
                instance("subjects"))

        stage = primaryStage

        val authService = instance<AuthenticationService>()

        if (authService.existSavedCredentials()) {
            authService.login().thenCompose {
                authService.currentUser()
            }.handleAsync(BiFunction<UserInfo, Throwable?, Unit> { user, e ->
                if (e == null) {
                    factory<UserInfo,HomeController>()(user)
                } else {
                    authService.logout()
                    instance<LoginController>()
                }
            }, JavaFXExecutor)
        } else {
            instance<LoginController>()
        }

        loadFonts()

        primaryStage.title = "syncPoliformat"
        primaryStage.isResizable = false
        primaryStage.icons += Image(javaClass.getResource("/img/icon-24.png").toString())
        if (instance<OS>() == OS.MAC) appleDockIcon()

        // TODO solo mantener abierta si estas en el HomeController
        Platform.setImplicitExit(false)
        if (Utils.isWindows) trayIconWin() else trayIcon()
        if (Utils.isMac) appleDockIcon()

        if (Settings.checkVersion()) {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Nueva versión disponible"
            alert.headerText = null
            alert.contentText = "Hemos detectado que existe una nueva versión disponible de la aplicación. " +
                    "Por favor descarguela de nuestra páguina web para poder garantizar su correcto funcionamiento."
            alert.showAndWait()
        }
    }

    private fun trayIconWin() {

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

    private fun trayIcon() {
        val systemTray = dorkbox.systemTray.SystemTray.get()
        systemTray.setImage(javaClass.getResource("/img/tray-icon.png"))

        systemTray.menu.add<dorkbox.systemTray.Entry>(dorkbox.systemTray.MenuItem("Abrir", {
            Platform.runLater { showStage() }
        }))

        systemTray.menu.add<dorkbox.systemTray.Entry>(dorkbox.systemTray.MenuItem("Salir", {
            systemTray.shutdown()
            System.exit(0)
        }))
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
