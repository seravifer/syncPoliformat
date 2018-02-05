import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class App  extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());

        //Font.loadFont(getClass().getResource("/css/Roboto.ttf").toExternalForm(), 14);

        primaryStage.setScene(scene);
        primaryStage.setTitle("syncPoliformaT");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
