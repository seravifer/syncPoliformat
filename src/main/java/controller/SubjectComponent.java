package controller;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import model.Subject;
import utils.Utils;

import java.io.IOException;

public class SubjectComponent extends AnchorPane {

    @FXML
    private Label longNameID;

    @FXML
    private Label nameID;

    @FXML
    private Label dateID;

    @FXML
    private SVGPath donwloadID;

    @FXML
    private JFXSpinner loadingID;

    @FXML
    private Circle circleID;

    private Subject subject;
    private String[] colors = {"#ad1457", "#6a1b9a", "#4527a0", "#283593", "#1565c0", "#0277bd", "#00838f", "#00695c",
            "#2e7d32", "#558b2f", "#9e9d24", "#f9a825", "#ff8f00", "#ef6c00", "#d84315"};

    public SubjectComponent(Subject subj) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/subject.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();

        subject = subj;

        nameID.setText(subject.getShortName());
        longNameID.setText(subject.getName());
        dateID.setText(subject.getLastUpdate());
        circleID.setFill(Color.web(colors[Utils.random(1, colors.length - 1)]));

        donwloadID.setOnMouseClicked((e) -> update());
    }

    public void sync() {
        update();
    }

    private void update() {
        donwloadID.setVisible(false);
        loadingID.setVisible(true);
        dateID.setText("Descargando asignatura...");

        Runnable myRunnable = () -> {
            try {
                subject.sync();
            } catch (IOException e) {
                Platform.runLater(this::finish);
            }
            Platform.runLater(this::finish);
        };

        Thread thread = new Thread(myRunnable);
        thread.start();
    }

    private void finish() {
        donwloadID.setVisible(true);
        loadingID.setVisible(false);
        subject.updateLastUpdate(Utils.now());
        dateID.setText(subject.getLastUpdate());
    }

}
