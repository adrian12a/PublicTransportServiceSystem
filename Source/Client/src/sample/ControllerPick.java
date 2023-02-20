package sample;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.Server.send;

/**
 * Wybor polaczenia (pasazer)
 */
public class ControllerPick implements Initializable {
    /**
     * Powrot do poprzedniego ekranu
     */
    public JFXButton backButton;
    /**
     * Wybor opcji nr 1
     */
    public JFXButton option1;
    /**
     * Wybor opcji nr 2
     */
    public JFXButton option2;
    /**
     * Wybor opcji nr 3
     */
    public JFXButton option3;
    /**
     * Wybor opcji nr 4
     */
    public JFXButton option4;
    /**
     * Informacje o opcji nr 1
     */
    public Label option1Info;
    /**
     * Informacje o opcji nr 2
     */
    public Label option2Info;
    /**
     * Informacje o opcji nr 3
     */
    public Label option3Info;
    /**
     * Informacje o opcji nr 4
     */
    public Label option4Info;

    /**
     * Czas przyjazdu
     */
    public static String time;
    /**
     * Startowy przystanek
     */
    public static String startBusStop;
    /**
     * Docelowy przystanek
     */
    public static String destBusStop;
    /**
     * Dodatkowe informacje
     */
    public Label info;
    /**
     * Informacje o wybranym startowym przystanku
     */
    public Label startLabel;
    /**
     * Informacje o wybranym docelowym przystanku
     */
    public Label destLabel;
    /**
     * Informacje o linii
     */
    public Label lineLabel;
    /**
     * Informacje o danym polaczeniu autobusowym
     */
    public String[] connection;

    /**
     * Inicjalizacja kontrolera, ustawienie widocznosci odpowiednich elementów, wypelnienie informacji o polaczeniach
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        option1.setVisible(false);
        option2.setVisible(false);
        option3.setVisible(false);
        option4.setVisible(false);
        startLabel.setVisible(false);
        destLabel.setVisible(false);
        lineLabel.setVisible(false);
        option1Info.setVisible(false);
        option2Info.setVisible(false);
        option3Info.setVisible(false);
        option4Info.setVisible(false);
        Packet packet = new Packet(Code.CONNECTIONS);
        packet.list.add(time);
        packet.list.add(startBusStop);
        packet.list.add(destBusStop);
        packet = send(packet);
        if(packet.code == Code.CONNECTIONS_SUCCESS)
        {
            if(packet.list.size() >= 1) {
                info.setVisible(false);
                option1.setVisible(true);
                option1Info.setVisible(true);
                startLabel.setVisible(true);
                destLabel.setVisible(true);
                lineLabel.setVisible(true);
                String temp = packet.list.get(0);
                connection = temp.split(" ");
                option1.setText("Linia nr: " + connection[0]);
                option1Info.setText("       " + connection[2] + "                           " + connection[4]);
                startLabel.setText("Odjazd z\n" + connection[1]);
                destLabel.setText("Przyjazd do\n" + connection[3]);
            }
            if(packet.list.size() >= 2) {
                option2.setVisible(true);
                option2Info.setVisible(true);
                String temp = packet.list.get(1);
                connection = temp.split(" ");
                option2.setText("Linia nr: " + connection[0]);
                option2Info.setText("       " + connection[2] + "                           " + connection[4]);
            }
            if(packet.list.size() >= 3) {
                option3.setVisible(true);
                option3Info.setVisible(true);
                String temp = packet.list.get(2);
                connection = temp.split(" ");
                option3.setText("Linia nr: " + connection[0]);
                option3Info.setText("       " + connection[2] + "                           " + connection[4]);
            }
            if(packet.list.size() >= 4) {
                option4.setVisible(true);
                option4Info.setVisible(true);
                String temp = packet.list.get(3);
                connection = temp.split(" ");
                option4.setText("Linia nr: " + connection[0]);
                option4Info.setText("       " + connection[2] + "                           " + connection[4]);
            }
        }
    }

    /**
     * Nacisnieto backButton, powrot do poprzedniego ekranu
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void backButtonPressed(ActionEvent actionEvent) throws Exception {
        Misc.changeScene(backButton,"Wyszukiwanie autobusu","search.fxml",960,540,ControllerPick.class);
    }

    /**
     * Nacisnieto option1, wybranie opcji 1
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void option1Picked(ActionEvent actionEvent) throws Exception {
        optionPicked(option1);
    }

    /**
     * Nacisnieto option2, wybranie opcji 2
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void option2Picked(ActionEvent actionEvent) throws Exception {
        optionPicked(option2);
    }

    /**
     * Nacisnieto option3, wybranie opcji 3
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void option3Picked(ActionEvent actionEvent) throws Exception {
        optionPicked(option3);
    }

    /**
     * Nacisnieto option4, wybranie opcji 4
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void option4Picked(ActionEvent actionEvent) throws Exception {
        optionPicked(option4);
    }

    /**
     * Przekazanie informacji o wybranej opcji do panelu pasazera, zmiana sceny na panel pasazera
     *
     * @param b Przycisk
     * @throws Exception the exception
     */
    void optionPicked(Button b) throws Exception {
        ControllerPassengerPanel.lineId = b.getText().substring(10);
        ControllerPassengerPanel.start = connection[1];
        ControllerPassengerPanel.dest = connection[3];
        Misc.changeScene(b,"Panel pasażera","passengerPanel.fxml",960, 540,ControllerPickLine.class);
    }
}
