package sample;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static sample.Server.send;

/**
 * Kontroler ekranu wyszukiwania polaczenia (pasazer)
 */
public class ControllerSearch implements Initializable {
    /**
     * Powrot do poprzedniego ekranu
     */
    public Button backButton;
    /**
     * Wybor startowego przystanku
     */
    public ComboBox startBusStop;
    /**
     * Wybor docelowego przystanku
     */
    public ComboBox destBusStop;
    /**
     * Wybor czasu
     */
    public ComboBox time;
    /**
     * Wyszukiwanie polaczenia
     */
    public JFXButton searchButton;
    /**
     * Dodatkowe informacje
     */
    public Label info;

    /**
     * Nacisnieto backButton, powrot do poprzedniego ekranu
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void backButtonPressed(ActionEvent actionEvent) throws Exception {
        Misc.changeScene(backButton,"Logowanie","login.fxml",300,400,ControllerSearch.class);
    }

    /**
     * Wypennianie comboBox
     *
     * @param sqlCode Kod sql
     * @param box     ComboBox
     * @param column  Kolumna w bazie danych
     * @throws SQLException the sql exception
     */
    public void insertCombobox(String sqlCode, ComboBox box, String column) throws SQLException {
        if(box.getItems().size() == 0) {
            Packet packet = new Packet(Code.SELECT);
            packet.list.add(sqlCode);
            packet.list.add(column);
            packet = send(packet);
            if (packet.code == Code.SELECT_SUCCESS) {
                for (int i = 0; i < packet.list.size(); i++)
                    box.getItems().add(packet.list.get(i));
            }
        }
    }

    /**
     * Nacisnieto searchButton, wyszukiwanie polaczenia
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void searchButtonPressed(ActionEvent actionEvent) throws Exception {
        boolean error = false;
        try {
            LocalTime.parse(time.getValue().toString());
        }
        catch (Exception e) {
            if(!info.isVisible())
                info.setVisible(true);
            error = true;
        }
        if(startBusStop.getValue() == null || destBusStop.getValue() == null || time.getValue() == null) {
            if(!info.isVisible())
                info.setVisible(true);
            error = true;
        }
        if(!error) {
            ControllerPick.time = (String) time.getSelectionModel().getSelectedItem();
            ControllerPick.startBusStop = (String) startBusStop.getSelectionModel().getSelectedItem();
            ControllerPick.destBusStop = (String) destBusStop.getSelectionModel().getSelectedItem();
            Misc.changeScene(searchButton,"Wybór połączenia","pickConnection.fxml",960,540,ControllerSearch.class);
        }
    }

    /**
     * Inicjalizacja kontrolera, wypelnienie ComboBox danymi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            insertCombobox("SELECT ulica FROM przystanki",startBusStop,"ulica");
            insertCombobox("SELECT ulica FROM przystanki",destBusStop,"ulica");
            insertCombobox("SELECT godzina_odjazdu FROM połączenia",time,"godzina_odjazdu");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
