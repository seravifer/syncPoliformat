package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private User user;
    private int attemps; // Se bloquea la cuenta a los 5 intentos

    @FXML
    private JFXTextField usernameID;

    @FXML
    private JFXPasswordField passwordID;

    @FXML
    private JFXCheckBox rememberID;

    @FXML
    private JFXButton loginID;

    @FXML
    private void loginClick() throws Exception {
        if(user.login(usernameID.getText(), passwordID.getText(), rememberID.isSelected())) {
            ((Stage)loginID.getScene().getWindow()).close();

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
            attemps++;
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
