@file:JvmName("syncPoliformat.App")

package syncPoliformat

import controller.Home
import controller.Login
import controller.NavigationHandler
import javafx.application.Application
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
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.on
import service.AuthenticationService
import java.io.File
import java.io.IOException
import java.net.URL
import kotlin.coroutines.CoroutineContext


open class App(private val actualKodein: () -> Kodein = { appComponent }) : Application(), KodeinAware, CoroutineScope {
    private val lateInitKodein = LateInitKodein()
    override val kodein: Kodein = lateInitKodein

    private val parentJob = Job()
    override val coroutineContext: CoroutineContext = parentJob + Dispatchers.Main

    lateinit var stage: Stage

    private val appFolder: File by instance(ApplicationFiles.ConfigFolder)
    private val poliformatFolder: File by instance(ApplicationFiles.PoliformatFolder)
    private val subjectsFile: File by instance(ApplicationFiles.SubjectsFile)

    private val authService by instance<AuthenticationService>()
    private val navigationHandler by instance<NavigationHandler>()

    override fun start(primaryStage: Stage) {
        stage = primaryStage
        lateInitKodein.baseKodein = actualKodein().on(this)
        initConfigFiles(appFolder, poliformatFolder, subjectsFile)
        loadFonts()
        logger.debug { "Initializing application" }

        launch {
            if (authService.existSavedCredentials()) {
                logger.debug { "Trying to use saved credentials..." }
                try {
                    authService.login()
                    val user = authService.currentUser()
                    navigationHandler.send(Home(user))
                } catch (e: Exception) {
                    authService.logout()
                    navigationHandler.send(Login)
                }
            } else {
                navigationHandler.send(Login)
            }

            setupStage()
            if (updateAvailable()) {
                showVersionAlert()
            }
            logger.debug { "Application initialized" }
        }
    }

    override fun stop() {
        logger.debug { "Stopping the application" }
        super.stop()
        logger.debug { "Application stoped" }
    }

    private fun loadFonts() {
        val fonts = arrayOf("Black", "Bold", "Light", "Medium", "Regular", "Thin")
        for (font in fonts) {
            Font.loadFont(javaClass.getResource("/css/fonts/Roboto-$font.ttf").toExternalForm(), 14.0)
            Font.loadFont(javaClass.getResource("/css/fonts/Roboto-${font}Italic.ttf").toExternalForm(), 14.0)
        }
    }

    private fun setupStage() {
        stage.title = "syncPoliformat"
        stage.isResizable = false
        stage.icons += Image(javaClass.getResource("/img/icon-24.png").toString())
    }

    private fun showVersionAlert() {
        val alert = Alert(Alert.AlertType.WARNING)
        alert.title = "Nueva versión disponible"
        alert.headerText = null
        alert.contentText = """Hemos detectado que existe una nueva versión disponible de la aplicación. 
            |Por favor descarguela de nuestra páguina web para poder garantizar su correcto funcionamiento.
        """.trimMargin()
        alert.showAndWait()
    }

    private fun updateAvailable(): Boolean {
        try {
            val url = URL("https://seravifer.github.io/syncPoliformat/version")
            val newVersion: Double = url.openStream().bufferedReader().use {
                it.readLine().toDouble()
            }

            return 1.0 < newVersion
        } catch (e: IOException) {
            logger.error(e) { "Error al comprobar la versión de la aplicación." }
        }

        return false
    }

    private fun initConfigFiles(vararg files: File) {
        for (file in files) {
            if (!file.exists()) {
                if (file.extension.isNotEmpty()) {
                    if (file.extension.contains("json")) file.writeText("{}")
                } else {
                    file.mkdirs()
                }
            }
        }
    }

    companion object : KLogging() {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(App::class.java, *args)
        }
    }
}
