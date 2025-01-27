package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.SubjectInfo;
import model.Subjects;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void init(User user) throws IOException {
        nameID.setText(user.getDisplayName());
        mailID.setText(user.getEmail());

        Subjects poliformat = new Subjects();

        poliformat.syncRemote();
        poliformat.syncLocal();

        poliformat.getSubjects().values().stream()
                .sorted(Comparator.comparing(SubjectInfo::getName))
                .forEachOrdered(subjectInfo -> listID.getChildren().add(new SubjectComponent(subjectInfo)));

        final ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("About");
        item1.setOnAction(e -> launchAbout());

        MenuItem item2 = new MenuItem("Preferences");
        item2.setOnAction(e -> launchSettings());

        MenuItem item3 = new MenuItem("Send feedback...");
        item3.setOnAction(e -> sendFeedbak());

        MenuItem item4 = new MenuItem("Help");
        item4.setOnAction(e -> help());

        MenuItem item5 = new MenuItem("Update all");
        item5.setOnAction(e -> updateAll());

        MenuItem item6 = new MenuItem("Exit");
        item6.setOnAction(e -> System.exit(0));

        contextMenu.getItems().addAll(item1, item4, item3, new SeparatorMenuItem(), item5, item2, new SeparatorMenuItem(), item6);

        settingsID.setOnMouseClicked((e) -> contextMenu.show(settingsID, Side.LEFT, 0 ,0));
    }

    @FXML
    private void openFolder() {
        try {
            Desktop.getDesktop().open(new File(Settings.poliformatDirectory()));
        } catch (IOException e) {
            System.err.println("No se ha encontrado ninguna carpeta.");
            e.printStackTrace();
        }
    }

    @FXML
    private void openWeb() throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URI("https://poliformat.upv.es/portal"));
    }

    private void sendFeedbak() {
        try {
            Desktop.getDesktop().browse(new URI("mailto:hola@sergiavila.com"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void updateAll() {
        for (int i = 0; i < listID.getChildren().size(); i++) {
            ((SubjectComponent) listID.getChildren().get(i)).sync();
        }
    }

    private void help() {

    }

    private void launchAbout() {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/about.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("About");
        stage.setResizable(false);
        stage.show();
    }

    private void launchSettings() {

    }

}