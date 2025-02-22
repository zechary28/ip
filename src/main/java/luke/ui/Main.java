package luke.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private Luke luke = new Luke();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinHeight(417);

            // set app icon and title
            stage.setTitle("Luke Task Manager");
            Image icon = new Image(getClass().getResource("/images/appicon.png").toExternalForm());
            stage.getIcons().add(icon);

            // get controller
            MainWindow controller = fxmlLoader.<MainWindow>getController();

            controller.setDuke(luke); // inject the Duke instance

            // bind lifecycle events
            stage.setOnShown(event -> controller.handleStartUp());
            stage.setOnCloseRequest(event -> {
                controller.handleShutDown();
                // Ensure the application exits
                stage.close();
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
