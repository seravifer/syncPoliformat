package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private User user;
    private int attemps;

    @FXML
    private TextField usernameID;
    @FXML
    private PasswordField passwordID;
    @FXML
    private Button loginID;
    @FXML
    private CheckBox rememberID;

    @FXML
    private void loginClick(ActionEvent event) throws Exception {
        if(user.login(usernameID.getText(), passwordID.getText())) {
            ((Stage)loginID.getScene().getWindow()).close();

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("view/home.fxml"));

            stage.setTitle("syncPoliformaT - Home");
            stage.setScene(new Scene(root));
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
