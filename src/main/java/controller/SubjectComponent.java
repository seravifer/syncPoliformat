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
import model.SubjectInfo;
import utils.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    private SubjectInfo subject;
    private String[] colors = {"#ad1457", "#6a1b9a", "#4527a0", "#283593", "#1565c0", "#0277bd", "#00838f", "#00695c",
            "#2e7d32", "#558b2f", "#9e9d24", "#f9a825", "#ff8f00", "#ef6c00", "#d84315"};

    SubjectComponent(SubjectInfo subj) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/subject.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        subject = subj;

        nameID.setText(subject.getShortName());
        longNameID.setText(subject.getName());
        dateID.setText(formatLastUpdate(subject.getLastUpdate()));
        circleID.setFill(Color.web(colors[Utils.random(1, colors.length - 1)]));

        donwloadID.setOnMouseClicked((e) -> update());

        if (!subject.getLastUpdate().isEmpty())
            donwloadID.setContent("M12,4C15.64,4 18.67,6.59 19.35,10.04C21.95,10.22 24,12.36 24,15A5,5 0 0,1 19,20H6A6,6 0 0,1 0,14C0,10.91 2.34,8.36 5.35,8.04C6.6,5.64 9.11,4 12,4M7.5,9.69C6.06,11.5 6.2,14.06 7.82,15.68C8.66,16.5 9.81,17 11,17V18.86L13.83,16.04L11,13.21V15C10.34,15 9.7,14.74 9.23,14.27C8.39,13.43 8.26,12.11 8.92,11.12L7.5,9.69M9.17,8.97L10.62,10.42L12,11.79V10C12.66,10 13.3,10.26 13.77,10.73C14.61,11.57 14.74,12.89 14.08,13.88L15.5,15.31C16.94,13.5 16.8,10.94 15.18,9.32C14.34,8.5 13.19,8 12,8V6.14L9.17,8.97Z");
    }

    private String formatLastUpdate(String lastUpdate) {
        try {
            return lastUpdate.equals("") ? new String("Todavía no ha sido sincronizado.".getBytes(), "UTF-8") : lastUpdate;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("¿UTF-8 no existe?", e);
        }
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
                subject.getManager().sync();
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
        subject.getManager().updateLastUpdate(Utils.now());
        dateID.setText(subject.getLastUpdate());
        donwloadID.setContent("M12,4C15.64,4 18.67,6.59 19.35,10.04C21.95,10.22 24,12.36 24,15A5,5 0 0,1 19,20H6A6,6 0 0,1 0,14C0,10.91 2.34,8.36 5.35,8.04C6.6,5.64 9.11,4 12,4M7.5,9.69C6.06,11.5 6.2,14.06 7.82,15.68C8.66,16.5 9.81,17 11,17V18.86L13.83,16.04L11,13.21V15C10.34,15 9.7,14.74 9.23,14.27C8.39,13.43 8.26,12.11 8.92,11.12L7.5,9.69M9.17,8.97L10.62,10.42L12,11.79V10C12.66,10 13.3,10.26 13.77,10.73C14.61,11.57 14.74,12.89 14.08,13.88L15.5,15.31C16.94,13.5 16.8,10.94 15.18,9.32C14.34,8.5 13.19,8 12,8V6.14L9.17,8.97Z");
    }

}
