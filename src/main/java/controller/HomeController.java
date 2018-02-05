package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import model.Poliformat;
import model.Subject;
import model.User;
import utils.SubjectComponent;
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
    private SVGPath folderID;

    @FXML
    private SVGPath webID;

    @FXML
    private VBox listID;

    private User user;
    private Poliformat poliformat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webID.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URL("https://poliformat.upv.es/portal").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        folderID.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().open(new File(Utils.poliformatDirectory()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

}