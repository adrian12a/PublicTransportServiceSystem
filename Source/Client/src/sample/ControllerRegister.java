package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

import static sample.Server.send;

/**
 * Kontroler rejestracji konta
 */
public class ControllerRegister {

    /**
     * Typ konta
     */
    String type = "Pasażer";
    /**
     * Rejestracja konta
     */
    public Button registerButton;
    /**
     * Pole loginu
     */
    public TextField login;
    /**
     * Pole emaila
     */
    public TextField email;
    /**
     * Pole hasla
     */
    public PasswordField password;
    /**
     * Powrot do poprzedniego ekranu
     */
    public Button backButton;
    /**
     * Dodatkowe informacje
     */
    public Label registerInfo;
    /**
     * Wybor typu konta - pasazer
     */
    public RadioButton radioPassenger;
    /**
     * Wybor typu konta - kierowca
     */
    public RadioButton radioDriver;

    /**
     * Nacisnieto RegisterButton, rejestracja konta
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void registerButtonPressed(ActionEvent actionEvent) throws Exception {
        Packet packet = new Packet(Code.REGISTER);
        String registerInsert = "INSERT INTO accounts VALUES ('" + login.getText() + "','" + password.getText() + "','" + email.getText() + "','" + type + "');";
        packet.list.add(login.getText());
        packet.list.add(password.getText());
        packet.list.add(email.getText());
        packet.list.add(type);
        packet = send(packet);
        if (packet.code == Code.REGISTER_SUCCESS)
            Misc.changeScene(registerButton, "Logowanie", "login.fxml", 300, 400, ControllerRegister.class);
        else
            registerInfo.setText("Konto o podanym loginie już istnieje");
    }

    /**
     * Nacisnieto backButton, powrot do poprzedniego ekranu
     *
     * @throws Exception the exception
     */
    public void backButtonPressed() throws Exception {
        Misc.changeScene(backButton, "Logowanie", "login.fxml", 300, 400, ControllerRegister.class);
    }

    /**
     * Nacisnieto radioPassenger, wybranie typu konta pasażer
     *
     * @param actionEvent the action event
     */
    public void radioPassengerAction(ActionEvent actionEvent) {
        radioPassenger.setSelected(true);
        radioDriver.setSelected(false);
        type = "Pasażer";
    }

    /**
     * Nacisnieto radioDriver, wybranie typu konta kierowca
     *
     * @param actionEvent the action event
     */
    public void radioDriverAction(ActionEvent actionEvent) {
        radioDriver.setSelected(true);
        radioPassenger.setSelected(false);
        type = "Kierowca";
    }
}
