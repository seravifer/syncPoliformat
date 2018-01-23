package utils;

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

import java.io.IOException;

public class SubjectComponent extends AnchorPane {

    @FXML
    private Label longNameID;

    @FXML
    private Label dateID;

    @FXML
    private SVGPath donwloadID;

    @FXML
    private JFXSpinner loadingID;

    @FXML
    private SVGPath cancelID;

    @FXML
    private Label nameID;

    @FXML
    private Circle circleID;

    private Subject subject;
    private Thread thread;
    private String[] colors = {"#C0CA33", "#FFC107", "#78909C", "#3F51B5", "#42A5F5", "#795548", "#FFEA00", "#ffd54f", "#80deea"};

    public SubjectComponent(Subject sbt) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/subject.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();

        this.subject = sbt;
        nameID.setText(subject.getName().replaceAll("[^a-zA-Z]", "").substring(0, 3).toUpperCase());
        longNameID.setText(subject.getName());
        dateID.setText("No ha sido descargada.");
        circleID.setFill(Color.web(colors[Utils.random(1, 6)]));

        donwloadID.setOnMouseClicked((event) -> {
            donwloadID.setVisible(false);
            loadingID.setVisible(true);
            cancelID.setVisible(true);
            dateID.setText("Descargando asignatura...");

            Runnable myRunnable = () -> {
                try {
                    subject.parseSubject();
                } catch (IOException e) {
                    finish(); // No internet connection
                }
                subject.downloadSubject();
                Platform.runLater(this::finish);
            };

            thread = new Thread(myRunnable);
            thread.start();
        });

        cancelID.setOnMouseClicked((event) -> {
            thread.interrupt(); // No funciona
            finish();
        });
    }

    private void finish() {
        donwloadID.setVisible(true);
        loadingID.setVisible(false);
        cancelID.setVisible(false);
        dateID.setText(Utils.now());
    }

}
