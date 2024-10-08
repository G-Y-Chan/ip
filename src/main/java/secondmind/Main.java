package secondmind;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for SecondMind using FXML.
 */
public class Main extends Application {

    private SecondMind secondMind = new SecondMind();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setSecondMind(this.secondMind);  // inject the SecondMind instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
