package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import utils.Settings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private JFXPasswordField usernameID;

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
    private AnchorPane sceneID;

    private User user = new User();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Settings.initFolders();
        } catch (IOException e) {
            e.printStackTrace();
        }

        usernameID.textProperty().addListener((ov, oldValue, newValue) -> {
            if (usernameID.getText().length() > 8 || !usernameID.getText().matches("[0-9]*")) {
                usernameID.setText(oldValue);
            }
        });

        sceneID.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) loginID.fire();
        });
    }

    @FXML
    private void login() {
        loginID.setDisable(true);
        loadingID.setVisible(true);

        new Thread(() -> {
            try {
                user.login(usernameID.getText(), passwordID.getText(), rememberID.isSelected());
            } catch (Exception e) {
                System.err.println("Ha ocurrido un fallo en la conexión con el servidor.");
                e.printStackTrace();
            }
            Platform.runLater(this::checkin);
        }).start();
    }

    private void checkin() {
        if (user.isLogged()) {
            try {
                launchHome();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorID.setVisible(true);
            passwordID.setText("");
        }

        loginID.setDisable(false);
        loadingID.setVisible(false);
    }

    private void launchHome() throws IOException {
        ((Stage) loginID.getScene().getWindow()).close();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
        Parent sceneMain = loader.load();

        HomeController controller = loader.getController();
        controller.init(user);

        Scene scene = new Scene(sceneMain);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());

        stage.setScene(scene);
        stage.setTitle("syncPoliformat");
        stage.setResizable(false);
        stage.show();
    }

}