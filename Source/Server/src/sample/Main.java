package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Glowna klasa
 */
public class Main extends Application {
    /**
     * Obiekt kontrolera
     */
    public static Controller c;

    /**
     * Zaladowanie interfejsu graficznego
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        c = loader.getController();
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    /**
     * Uruchomienie aplikacji
     *
     * @param args the input arguments
     */
    static public void main(String[] args) {
        launch(args);
    }
}