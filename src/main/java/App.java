import controller.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URISyntaxException;

public class App extends Application {

    private User user = new User();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, URISyntaxException {
        if (user.checkLogin()) {
            user.silentLogin();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            Parent sceneMain = loader.load();

            HomeController controller = loader.getController();
            controller.init(user);

            Scene scene = new Scene(sceneMain);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());

            primaryStage.setScene(scene);
            primaryStage.setTitle("syncPoliformat");
            primaryStage.setResizable(false);
            primaryStage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());

            //Font.loadFont(getClass().getResource("/css/Roboto.ttf").toExternalForm(), 14);

            primaryStage.setScene(scene);
            primaryStage.setTitle("syncPoliformat");
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }

}
