package utils;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
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
    private ProgressIndicator loadingID;

    @FXML
    private SVGPath cancelID;

    @FXML
    private Label nameID;

    private Subject subject;
    private Thread thread = null;

    public SubjectComponent(Subject s) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/subject.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();

        this.subject = s;
        nameID.setText(subject.getName().substring(0, 1));
        longNameID.setText(subject.getName());
        dateID.setText("No ha sido descargada.");

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
            thread.interrupt();
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
