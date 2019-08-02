@file:JvmName("App")

import controller.HomeController
import controller.LoginController
import domain.UserInfo
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.scene.text.Font
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mu.KLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.Multi2
import org.kodein.di.direct
import org.kodein.di.generic.M
import org.kodein.di.generic.instance
import service.AuthenticationService
import utils.OS
import utils.Settings
import java.awt.*
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess


class App : Application(), KodeinAware, CoroutineScope {
    override val kodein: Kodein by lazy { appComponent }

    private val parentJob = Job()
    override val coroutineContext: CoroutineContext = parentJob + Dispatchers.Main

    private lateinit var stage: Stage
    private val os: OS by instance()

    private val appFolder: File by instance("app")
    private val poliformatFolder: File by instance("poliformat")
    private val subjectsFolder: File by instance("subjects")

    private val authService by instance<AuthenticationService>()

    override fun start(primaryStage: Stage) {
        Settings.initFolders(appFolder, poliformatFolder, subjectsFolder)
        logger.debug { "Initializing app" }
        stage = primaryStage

        launch {
            if (authService.existSavedCredentials()) {
                try {
                    authService.login()
                    val user = authService.currentUser()
                    direct.instance<Multi2<Stage, UserInfo>, HomeController>(arg = M(stage, user))
                } catch (e: Exception) {
                    authService.logout()
                    direct.instance<Stage, LoginController>(arg = stage)
                }
            } else {
                direct.instance<Stage, LoginController>(arg = stage)
            }

            loadFonts()

            primaryStage.title = "syncPoliformat"
            primaryStage.isResizable = false
            primaryStage.icons += Image(javaClass.getResource("/img/icon-24.png").toString())
            if (os === OS.MAC) appleDockIcon()

            // TODO solo mantener abierta si estas en el HomeController
            Platform.setImplicitExit(false)
            if (os === OS.WINDOWS) trayIconWin() else trayIcon()
            if (os === OS.MAC) appleDockIcon()

            if (Settings.checkVersion()) {
                val alert = Alert(Alert.AlertType.WARNING)
                alert.title = "Nueva versión disponible"
                alert.headerText = null
                alert.contentText = "Hemos detectado que existe una nueva versión disponible de la aplicación. " +
                        "Por favor descarguela de nuestra páguina web para poder garantizar su correcto funcionamiento."
                alert.showAndWait()
            }
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
            exitProcess(0)
        }

        trayIcon.popupMenu = popup
        trayIcon.isImageAutoSize = true
        tray.add(trayIcon)
    }

    private fun trayIcon() {
        val systemTray = dorkbox.systemTray.SystemTray.get()
        systemTray.setImage(javaClass.getResource("/img/tray-icon.png"))

        systemTray.menu.add<dorkbox.systemTray.Entry>(dorkbox.systemTray.MenuItem("Abrir") {
            Platform.runLater { showStage() }
        })

        systemTray.menu.add<dorkbox.systemTray.Entry>(dorkbox.systemTray.MenuItem("Salir") {
            systemTray.shutdown()
            exitProcess(0)
        })
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

    companion object : KLogging() {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(App::class.java, *args)
        }
    }
}
