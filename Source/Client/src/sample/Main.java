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
     * Wyswietlenie ekranu logowania
     */
    @Override
    public void start(Stage stage) throws Exception{
        Parent login = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setTitle("Logowanie");
        stage.setScene(new Scene(login, 300, 400));
        stage.show();
    }

    /**
     * Uruchomienie aplikacji, polaczenie z serwerem
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Server.connect();
        launch(args);
        Server.close();
    }

}
