package utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.Subject;

import java.io.IOException;

public class SubjectComponent extends AnchorPane {

    @FXML
    private Label nameID;

    @FXML
    private Label dateID;

    @FXML
    private Label syncID;

    private Subject subject;

    public SubjectComponent(Subject s) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/subject.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            System.err.println("Error al cargar la vista: " + this.getClass().getSimpleName());
        }

        this.subject = s;
        nameID.setText(subject.getName());
    }

}
