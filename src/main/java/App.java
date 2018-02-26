import controller.HomeController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class App extends Application {

    private User user = new User();
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
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

        Platform.setImplicitExit(false);
        SwingUtilities.invokeLater(this::tryaicon);
    }

    private void tryaicon() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/icon.png"));

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(image);
        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem displayMenu = new MenuItem("Open");
        MenuItem exitItem = new MenuItem("Exit");

        popup.add(displayMenu);
        popup.addSeparator();
        popup.add(exitItem);

        displayMenu.addActionListener(event -> Platform.runLater(this::showStage));
        trayIcon.addActionListener(event -> Platform.runLater(this::showStage));
        exitItem.addActionListener(event -> System.exit(0));

        trayIcon.setPopupMenu(popup);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void showStage() {
        stage.show();
        stage.toFront();
    }

}
