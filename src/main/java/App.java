import controller.HomeController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.User;
import utils.Settings;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public class App extends Application {

    private User user = new User();
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Settings.initFolders();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stage = stage;
        Parent root;

        if (user.checkRememberLogin()) {
            user.silenceLogin();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            root = loader.load();

            ((HomeController)loader.getController()).init(user);
        } else {
            root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
        }


        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
        loadFonts();

        stage.setScene(scene);
        stage.setTitle("syncPoliformat");
        stage.setResizable(false);
        stage.show();

        Platform.setImplicitExit(false);
        SwingUtilities.invokeLater(this::trayIcon);

        if (Utils.checkVersion()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nueva versión disponible");
            alert.setHeaderText(null);
            alert.setContentText("Hemos detectado que existe una nueva versión disponible de la aplicación. " +
                    "Por favor descarguela de nuestra páguina web para poder garantizar su correcto funcionamiento.");
            alert.showAndWait();
        }
    }

    private void trayIcon() {
        if (!SystemTray.isSupported()) {
            System.err.println("SystemTray is not supported");
            return;
        }

        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/tray-icon.png"));

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

    private void loadFonts() {
        String[] fonts = {"Black", "Bold", "Light", "Medium", "Thin"};
        for (String font : fonts) {
            Font.loadFont(getClass().getResource("/css/fonts/Roboto-"+font+".ttf").toExternalForm(), 14);
            Font.loadFont(getClass().getResource("/css/fonts/Roboto-"+font+"Italic.ttf").toExternalForm(), 14);
        }

        Font.loadFont(getClass().getResource("/css/fonts/Roboto-Regular.ttf").toExternalForm(), 14);
        Font.loadFont(getClass().getResource("/css/fonts/Roboto-Italic.ttf").toExternalForm(), 14);
    }


    private void appleDockIcon() {
        try {
            Class util = Class.forName("com.apple.eawt.Application");
            Method getApplication = util.getMethod("getApplication");
            Object application = getApplication.invoke(util);
            Class params[] = new Class[1];
            params[0] = Image.class;
            Method setDockIconImage = util.getMethod("setDockIconImage", params);
            URL url = getClass().getResource("res/icon-1024.png");
            Image image = Toolkit.getDefaultToolkit().getImage(url);
            setDockIconImage.invoke(application, image);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void showStage() {
        stage.show();
        stage.toFront();
    }

}
