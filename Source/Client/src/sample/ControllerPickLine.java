package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.Server.send;

/**
 * Kontroler wyboru linii (kierowca)
 */
public class ControllerPickLine implements Initializable {
    /**
     * Powrot do poprzedniego ekranu
     */
    public JFXButton backButton;
    /**
     * Wybor linii
     */
    public JFXComboBox line;
    /**
     * Akceptowanie wyboru
     */
    public JFXButton accept;
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
        Misc.changeScene(backButton,"Logowanie","login.fxml",300,400,ControllerPickLine.class);
    }

    /**
     * Nacisnieto acceptButton, wybranie linii, przejscie do panelu kierowcy
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void acceptButtonPressed(ActionEvent actionEvent) throws Exception {
        if(line.getValue() != null) {
            ControllerDriverPanel.lineId = line.getValue().toString();
            Misc.changeScene(accept,"Panel kierowcy","driverPanel.fxml",960, 540,ControllerPickLine.class);
        }
        else {
            if(!info.isVisible())
                info.setVisible(true);
        }
    }

    /**
     * Inicjalizacja kontrolera, wypelnienie ComboBox danymi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(line.getItems().size() == 0) {
            Packet packet = new Packet(Code.SELECT);
            packet.list.add("SELECT DISTINCT id_trasy FROM połączenia");
            packet.list.add("id_trasy");
            packet = send(packet);
            if (packet.code == Code.SELECT_SUCCESS) {
                for (int i = 0; i < packet.list.size(); i++)
                    line.getItems().add(packet.list.get(i));
            }
        }
    }
}
