package controller

import domain.UserInfo
import javafx.application.Platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import mu.KLogging
import utils.OS
import java.awt.Image
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.event.ActionEvent

sealed class NavigationOrder
object Login : NavigationOrder()
data class Home(val user: UserInfo) : NavigationOrder()
object Hide : NavigationOrder()
object Show : NavigationOrder()
object Exit : NavigationOrder()

class NavigationHandler(
        private val os: OS,
        coroutineScope: CoroutineScope,
        loginControllerFactory: (NavigationHandler) -> LoginController,
        homeFactory: (UserInfo, NavigationHandler) -> HomeController
) : CoroutineScope by coroutineScope {
    private val loginController by lazy {
        loginControllerFactory(this)
    }

    private val actualHomeFactory = { user: UserInfo ->
        homeFactory(user, this)
    }

    private val actor by lazy {
        actor<NavigationOrder>(coroutineContext + Dispatchers.Main) {
            var lastController: Controller = loginController
            var deleteSystemTray: (() -> Unit)? = null
            for (order in channel) {
                when(order) {
                    Exit -> {
                        deleteSystemTray?.invoke()
                        Platform.exit()
                    }
                    Show -> {
                        lastController.show(false)
                        logger.debug { "Current controller was shown" }
                    }
                    Hide -> {
                        lastController.hide()
                        logger.debug { "Current controller was hidden" }
                    }
                    Login -> {
                        deleteSystemTray?.let { delete ->
                            delete()
                            Platform.setImplicitExit(true)
                            deleteSystemTray = null
                        }
                        loginController.also { lastController = it }.show()
                        logger.debug { "Navigated to login" }
                    }
                    is Home -> {
                        if (deleteSystemTray == null) {
                            deleteSystemTray = enableSystemTray()
                            Platform.setImplicitExit(false)
                        }
                        actualHomeFactory(order.user).also { lastController = it }.show()
                        logger.debug { "Navigated to Home" }
                    }
                }
            }
        }
    }

    suspend fun send(order: NavigationOrder) {
        actor.send(order)
    }

    private val showStage: (ActionEvent) -> Unit = {
        logger.debug { "Implict exit is ${Platform.isImplicitExit()}" }
        launch {
            logger.debug { "Running coroutine on ${Thread.currentThread().name}" }
            actor.send(Show)
        }
    }

    private fun enableSystemTray(): () -> Unit {
        val deleteTrayIcon = if (os === OS.WINDOWS) trayIconWin() else trayIcon()
        if (os === OS.MAC) appleDockIcon()
        logger.debug { "Enabled system tray icon for os $os" }
        return {
            deleteTrayIcon()
            logger.debug { "Deleted system tray icon" }
        }
    }

    private fun trayIconWin(): () -> Unit {
        val image = Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/img/tray-icon.png"))

        val popup = PopupMenu()
        val trayIcon = TrayIcon(image)
        val tray = SystemTray.getSystemTray()

        val displayMenu = MenuItem("Open")
        val exitItem = MenuItem("Exit")

        popup.add(displayMenu)
        popup.addSeparator()
        popup.add(exitItem)

        displayMenu.addActionListener(showStage)
        trayIcon.addActionListener(showStage)
        exitItem.addActionListener {
            tray.remove(trayIcon)
            Platform.exit()
        }

        trayIcon.popupMenu = popup
        trayIcon.isImageAutoSize = true
        tray.add(trayIcon)
        return {
            tray.remove(trayIcon)
        }
    }

    private fun trayIcon(): () -> Unit {
        val systemTray = dorkbox.systemTray.SystemTray.get()
        systemTray.setImage(javaClass.getResource("/img/tray-icon.png"))

        systemTray.menu.add<dorkbox.systemTray.Entry>(dorkbox.systemTray.MenuItem("Abrir", showStage))

        systemTray.menu.add<dorkbox.systemTray.Entry>(dorkbox.systemTray.MenuItem("Salir") {
            systemTray.shutdown()
            Platform.exit()
        })
        return systemTray::shutdown
    }

    private fun appleDockIcon() {
        val appleLibrary = Class.forName("com.apple.eawt.Application")
        val application = appleLibrary.getMethod("getApplication").invoke(appleLibrary)
        val setDockIconImage = appleLibrary.getMethod("setDockIconImage", Image::class.java)
        val image: Image = Toolkit.getDefaultToolkit().getImage(javaClass.getResource("img/icon-128.png"))
        setDockIconImage.invoke(application, image)
    }

    companion object : KLogging()
}
