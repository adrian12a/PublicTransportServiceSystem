package sample;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static sample.Server.send;

/**
 * Kontroler glownego panelu pasazera
 */
public class ControllerPassengerPanel implements Initializable {
    /**
     * Indeks aktualnego przystanku
     */
    public Integer index;
    /**
     * Pakiet
     */
    public Packet packet;
    /**
     * Id linii
     */
    public static String lineId;
    /**
     * Nazwa przystanku startowego
     */
    public static String start;
    /**
     * Nazwa przystanku docelowego
     */
    public static String dest;
    /**
     * Id przystanku docelowego
     */
    public static String destID;
    /**
     * Powrot do poprzedniego ekranu
     */
    public JFXButton backButton;
    /**
     * Aktualny czas
     */
    public Label time;
    /**
     * Nazwa nastepnego przystanku
     */
    public Label nextBusStop;
    /**
     * Odliczanie do nastepnego przystanku
     */
    public Label nextBusStopCount;
    /**
     * Godzina przyjazdu na nastepny przystanek
     */
    public Label nextBusStopTime;
    /**
     * Odliczanie do docelowego przystanku
     */
    public Label destBusStopCount;
    /**
     * Godzina przyjazdu na docelowy przystanek
     */
    public Label destBusStopTime;
    /**
     * Nazwa docelowego przystanku
     */
    public Label destInfo;
    /**
     * Nazwa startowego przystanku
     */
    public Label startInfo;

    /**
     * Nacisnieto backButton, powrot do poprzedniego ekranu
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void backButtonPressed(ActionEvent actionEvent) throws Exception {
        Misc.changeScene(backButton, "Wybór połączenia","pickConnection.fxml",960,540, ControllerPassengerPanel.class);
    }

    /**
     * Inicjalizacja kontrolera, pobranie danych od serwera, wypelnienie elementow graficznych danymi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startInfo.setText("Przystanek startowy: " + start);
        destInfo.setText("Przystanek docelowy: " + dest);
        index = 0;
        Misc.timer(time);
        packet = new Packet(Code.SELECT);
        packet.list.add("SELECT id_przystanku FROM przystanki WHERE ulica = '" + dest + "';");
        packet.list.add("id_przystanku");
        packet = send(packet);
        destID = packet.list.get(0);
        packet = new Packet(Code.SELECT);
        packet.list.add("SELECT godzina_odjazdu FROM połączenia WHERE id_przystanku = '" + destID + "';");
        packet.list.add("godzina_odjazdu");
        packet = send(packet);
        destBusStopTime.setText(packet.list.get(0));
        packet = new Packet(Code.PANEL);
        packet.list.add(lineId);
        packet = send(packet);
        for(int i = 0; i < packet.list.size();i++) {
            if(packet.list.get(i).equals(dest))
                dest = String.valueOf(i+1);
        }
        nextBusStop.setText("Następny przystanek:\n" + packet.list.get(index));
        nextBusStopTime.setText("Godzina przyjazdu\nna następny przystanek:\n" + packet.list.get(index + 1));
        destBusStopTime.setText("Godzina przyjazdu\nna docelowy przystanek:\n" + packet.list.get(Integer.valueOf(dest)));
        Misc.timerCountingPassenger(nextBusStopCount,"Pozostały czas do\nnastępnego przystanku:\n",packet,index,nextBusStop,nextBusStopTime);
        Misc.timerCounting(destBusStopCount,LocalTime.parse(packet.list.get(Integer.valueOf(dest)),Misc.format), "Pozostały czas do\nprzystanku docelowego:\n","Autobus był na\nprzystanku docelowym:\n");
    }
}