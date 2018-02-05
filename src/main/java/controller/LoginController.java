package controller;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private User user;
    private int attemps = 3;

    @FXML
    private JFXTextField usernameID;

    @FXML
    private JFXPasswordField passwordID;

    @FXML
    private JFXCheckBox rememberID;

    @FXML
    private JFXButton loginID;

    @FXML
    private Label errorID;

    @FXML
    private JFXProgressBar loadingID;

    @FXML
    private void loginClick() {
        loginID.setDisable(true);
        loadingID.setVisible(true);
        new Thread(() -> {
            try {
                user.login(usernameID.getText(), passwordID.getText(), rememberID.isSelected());
            } catch (Exception e) {
                System.err.println("El usuario no tiene conexión a internet.");
            }
            Platform.runLater(() -> status(user.isLogged()));
        }).start();
    }

    public void status(Boolean isLogged) {
        try {
            if (isLogged) {
                ((Stage) loginID.getScene().getWindow()).close();

                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));

                Parent sceneMain = loader.load();

                HomeController controller = loader.getController();
                controller.init(user);

                Scene scene = new Scene(sceneMain);
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());

                stage.setScene(scene);
                stage.setTitle("syncPoliformaT");
                stage.setResizable(false);
                stage.show();
            } else {
                errorID.setVisible(true);
                passwordID.setText("");
                attemps--;
            }

            loginID.setDisable(false);
            loadingID.setVisible(false);
            if (attemps == 0) loginID.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = new User();

        usernameID.textProperty().addListener((ov, oldValue, newValue) -> {
            if (usernameID.getText().length() > 8 || !usernameID.getText().matches("[0-9]*")) {
                usernameID.setText(oldValue);
            }
        });
    }
}
