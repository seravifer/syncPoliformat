package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import model.Poliformat;
import model.Subject;
import model.User;
import utils.Utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Label nameID;

    @FXML
    private Label mailID;

    @FXML
    private SVGPath settingsID;

    @FXML
    private VBox listID;

    private User user;
    private Poliformat poliformat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(User user) throws IOException {
        this.user = user;
        nameID.setText(user.getNameUser() + " " + user.getLastNameUser());
        mailID.setText(user.getMailUser());
        
        poliformat = new Poliformat();

        poliformat.syncRemote();
        poliformat.syncLocal();

        for (Subject item : poliformat.getSubjects()) {
            listID.getChildren().add(new SubjectComponent(item));
        }
    }

    private void updateAll() {
        for (int i = 0; i < listID.getChildren().size(); i++) {
            ((SubjectComponent) listID.getChildren().get(i)).sync();
        }
    }

    @FXML
    private void openFolder(MouseEvent event) throws IOException {
        Desktop.getDesktop().open(new File(Utils.poliformatDirectory()));
    }

    @FXML
    private void openWeb(MouseEvent event) throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URL("https://poliformat.upv.es/portal").toURI());
    }

}