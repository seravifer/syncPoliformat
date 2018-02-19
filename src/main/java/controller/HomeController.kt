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

import java.awt.*
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.Comparator
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

    private var user: User? = null

    @FXML
    override fun initialize(location: URL, resources: ResourceBundle?) {}

    @Throws(IOException::class)
    fun init(user: User) {
        this.user = user
        nameID.text = user.nameUser + " " + user.lastNameUser
        mailID.text = user.mailUser

        val poliformat = Poliformat()

        poliformat.syncRemote()
        poliformat.syncLocal()

        poliformat.subjects!!.values.stream()
                .sorted(Comparator.comparing<SubjectInfo, String>(SubjectInfo::name))
                .forEachOrdered { subjectInfo ->
                    try {
                        listID.children.add(SubjectComponent(subjectInfo))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
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
        Desktop.getDesktop().open(File(Settings.poliformatDirectory()))
    }

    @FXML
    @Throws(IOException::class, URISyntaxException::class)
    private fun openWeb(event: MouseEvent) {
        Desktop.getDesktop().browse(URI("https://poliformat.upv.es/portal"))
    }

}