package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static sample.Server.send;

/**
 * Kontroler logowania uzytkownika
 */
public class ControllerLogin {

    /**
     * Pole hasla
     */
    public PasswordField password;
    /**
     * Pole loginu
     */
    public TextField login;
    /**
     * Logowanie
     */
    public Button loginButton;
    /**
     * Rejestracja
     */
    public Button registerButton;
    /**
     * Logowanie jako gosc
     */
    public Button loginGuestButton;
    /**
     * Dodatkowe informacje
     */
    public Label loginInfo;

    /**
     * Nacisnieto loginButton, logowanie
     *
     * @param event the event
     * @throws Exception the exception
     */
    public void loginButtonPressed(ActionEvent event) throws Exception {
        Packet packet = new Packet(Code.LOGIN);
        packet.list.add(login.getText());
        packet.list.add(password.getText());
        packet = send(packet);
        if(packet.code == Code.LOGIN_SUCCESS) {
            if(packet.list.get(0).charAt(0) == 'K')
                Misc.changeScene(loginButton, "Wybór linii", "pickLine.fxml", 960, 540, ControllerLogin.class);
            else
                Misc.changeScene(loginButton, "Wyszukiwanie autobusu", "search.fxml", 960, 540, ControllerLogin.class);
        }
        else
            loginInfo.setText("Wprowadzono błędne dane");
    }

    /**
     * Nacisnieto registerButton, rejestracja
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void registerButtonPressed(ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Misc.changeScene(registerButton,"Rejestracja","register.fxml",600,400,ControllerLogin.class);
    }

    /**
     * Nacisnieto loginGuest, logowanie jako gosc (pasazer)
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void loginGuestButtonPressed(ActionEvent actionEvent) throws Exception {
        Misc.changeScene(loginGuestButton,"Wyszukiwanie autobusu","search.fxml",960,540,ControllerLogin.class);
    }

}
