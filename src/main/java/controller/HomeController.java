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

    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void init(User user) throws IOException {
        this.user = user;
        nameID.setText(user.getDisplayName());
        mailID.setText(user.getEmail());

        Subjects poliformat = new Subjects();

        poliformat.syncRemote();
        poliformat.syncLocal();

        poliformat.getSubjects().values().stream()
                .sorted(Comparator.comparing(SubjectInfo::getName))
                .forEachOrdered(subjectInfo -> listID.getChildren().add(new SubjectComponent(subjectInfo)));

        final ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Nosotros");
        item1.setOnAction(e -> launchAbout());

        MenuItem item3 = new MenuItem("Reportar error");
        item3.setOnAction(e -> sendFeedbak());

        MenuItem item5 = new MenuItem("Sincronizar");
        item5.setOnAction(e -> updateAll());

        //MenuItem item2 = new MenuItem("Preferences");
        MenuItem item2 = new MenuItem("Cerrar sesión");
        item2.setOnAction(e -> launchSettings());

        MenuItem item6 = new MenuItem("Salir");
        item6.setOnAction(e -> System.exit(0));

        contextMenu.getItems().addAll(item1, item3, new SeparatorMenuItem(), item5, item2, new SeparatorMenuItem(), item6);

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

    }

    private void updateAll() {
        for (int i = 0; i < listID.getChildren().size(); i++) {
            ((SubjectComponent) listID.getChildren().get(i)).sync();
        }
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
        user.logout();
        System.exit(0);
    }

}