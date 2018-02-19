package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import model.Poliformat;
import model.SubjectInfo;
import model.User;
import utils.Settings;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void init(User user) throws IOException {
        this.user = user;
        nameID.setText(user.getNameUser() + " " + user.getLastNameUser());
        mailID.setText(user.getMailUser());

        Poliformat poliformat = new Poliformat();

        poliformat.syncRemote();
        poliformat.syncLocal();

        poliformat.getSubjects().values().stream()
                .sorted(Comparator.comparing(SubjectInfo::getName))
                .forEachOrdered(subjectInfo -> {
                    try {
                        listID.getChildren().add(new SubjectComponent(subjectInfo));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @FXML
    private void updateAll() {
        for (int i = 0; i < listID.getChildren().size(); i++) {
            ((SubjectComponent) listID.getChildren().get(i)).sync();
        }
    }

    @FXML
    private void openFolder(MouseEvent event) throws IOException {
        Desktop.getDesktop().open(new File(Settings.poliformatDirectory()));
    }

    @FXML
    private void openWeb(MouseEvent event) throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URI("https://poliformat.upv.es/portal"));
    }

}