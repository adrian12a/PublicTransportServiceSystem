package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Kontroler
 */
public class Controller {

    /**
     * Uruchamianie serwera
     */
    public Button startButton;
    /**
     * Konsola
     */
    public TextArea textArea;
    /**
     * Przechowuje zawartosc konsoli
     */
    public String input = "";
    /**
     * Executor przydzielajacy watki
     */
    static public ExecutorService executor = Executors.newFixedThreadPool(8);

    /**
     * Nacisnieto startButton, uruchomienie serwera
     *
     * @param actionEvent the action event
     */
    public void startButtonPressed(ActionEvent actionEvent) {
        startButton.setDisable(true);
        executor.execute(new Server());
    }

    /**
     * Dodanie nowego wpisu do konsoli
     *
     * @param text Tekst wpisu
     */
    public void insert(String text) {
        input = input + text + "\n";
        textArea.setText(input);
    }
}
