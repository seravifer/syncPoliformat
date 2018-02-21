import controller.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class App extends Application {

    private User user = new User();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root;

        if (user.checkRememberLogin()) {
            user.silenceLogin();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            root = loader.load();

            ((HomeController)loader.getController()).init(user);
        } else {
            root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
        }

        //Font.loadFont(getClass().getResource("/css/Roboto.ttf").toExternalForm(), 14);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());

        primaryStage.setScene(scene);
        primaryStage.setTitle("syncPoliformat");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
