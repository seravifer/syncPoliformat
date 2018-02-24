package controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.scene.shape.SVGPath
import model.Poliformat
import model.SubjectInfo
import model.User
import utils.Settings

import java.awt.Desktop
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.ResourceBundle

class HomeController : Initializable {

    @FXML
    private lateinit var nameID: Label

    @FXML
    private lateinit var mailID: Label

    @FXML
    private lateinit var settingsID: SVGPath

    @FXML
    private lateinit var listID: VBox

    @FXML
    override fun initialize(location: URL, resources: ResourceBundle?) {}

    @Throws(IOException::class)
    fun init(user: User) {
        with(user) {
            nameID.text = "$nameUser $lastNameUser"
            mailID.text = mailUser
        }

        val poliformat = Poliformat()

        poliformat.fetchSubjectsInfo().onSucceeded { subjects ->
            poliformat.persistLastUpdateSubjectsDate(subjects).onSucceeded {
                subjects.sortedBy(SubjectInfo::name)
                        .forEach { listID.children.add(SubjectComponent(it)) }
            }.toThread(name = "Persist-Subjects-Last-Update", isDaemon = false).start()
        }.toThread(name = "Fetch-Subjects-Info").start()
    }

    @FXML
    private fun updateAll() {
        for (i in 0 until listID.children.size) {
            (listID.children[i] as SubjectComponent).sync()
        }
    }

    @FXML
    @Throws(IOException::class)
    private fun openFolder(event: MouseEvent) {
        Desktop.getDesktop().open(Settings.poliformatDirectory)
    }

    @FXML
    @Throws(IOException::class, URISyntaxException::class)
    private fun openWeb(event: MouseEvent) {
        Desktop.getDesktop().browse(URI("https://poliformat.upv.es/portal"))
    }

}