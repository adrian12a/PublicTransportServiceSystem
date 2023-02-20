package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static sample.Server.send;

/**
 * Kontroler glownego panelu kierowcy
 */
public class ControllerDriverPanel implements Initializable {
    /**
     * Id linii
     */
    public static String lineId;
    /**
     * Indeks aktualnego przystanku
     */
    public Integer index;
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
     * Godzina przyjazdu na nastepny przystanek
     */
    public Label nextBusStopTime;
    /**
     * Odliczanie do nastepnego przystanku
     */
    public Label nextBusStopCount;
    /**
     * Odliczanie do ostatniego przystanku
     */
    public Label lastBusStopCount;
    /**
     * Animacja odliczania do nastepnego przystanku
     */
    public Timeline nextBusStopAnimation;
    /**
     * Animacja odliczania do ostatniego przystanku
     */
    public Timeline lastBusStopAnimation;
    /**
     * Pakiet
     */
    public Packet packet;
    /**
     * Potwierdzenie przyjazdu na kolejny przystanek
     */
    public JFXButton confirmButton;
    /**
     * Panel podsumowania przejazdu
     */
    public AnchorPane resultPage;
    /**
     * Format godziny
     */
    public DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
    /**
     * Podsumowanie kursu uzywane przez TreeTable
     */
    public ObservableList<Summary> data;
    /**
     * TreeTable wyswietlajacy podsumowanie kursu
     */
    public JFXTreeTableView summary;

    /**
     * Nacisnieto backButton, powrot do poprzedniego ekranu
     *
     * @param actionEvent the action event
     * @throws Exception the exception
     */
    public void backButtonPressed(ActionEvent actionEvent) throws Exception {
        Misc.changeScene(backButton, "Wybór linii", "pickLine.fxml", 960, 540, ControllerDriverPanel.class);
    }

    /**
     * Inicjalizacja kontrolera, stworzenie tabeli TreeView, pobranie danych od serwera, wypelnienie elementow graficznych danymi
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeTableColumn busStopColumn = new TreeTableColumn("Przystanek");
        TreeTableColumn timeColumn = new TreeTableColumn("Różnica względem rozkładu");
        TreeTableColumn ratingColumn = new TreeTableColumn("Ocena przejazdu");
        busStopColumn.setMinWidth(100.0);
        timeColumn.setMinWidth(200.0);
        ratingColumn.setMinWidth(150.0);
        summary.getColumns().addAll(busStopColumn,timeColumn,ratingColumn);
        data = FXCollections.observableArrayList();
        busStopColumn.setCellValueFactory(
                new TreeItemPropertyValueFactory<Summary,String>("busStop")
        );
        timeColumn.setCellValueFactory(
                new TreeItemPropertyValueFactory<Summary,String>("time")
        );
        ratingColumn.setCellValueFactory(
                new TreeItemPropertyValueFactory<Summary,String>("status")
        );
        TreeItem<Summary> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        summary.setRoot(root);
        summary.setShowRoot(false);
        index = 0;
        Misc.timer(time);
        packet = new Packet(Code.PANEL);
        packet.list.add(lineId);
        packet = send(packet);
        if (packet.code == Code.PANEL_SUCCESS)
            refresh();
    }

    /**
     * Odswiezenie danych dla kolejnego przystanku
     */
    public void refresh() {
            nextBusStop.setText("Następny przystanek:\n" + packet.list.get(index));
            nextBusStopTime.setText("Godzina przyjazdu\nna następny przystanek:\n" + packet.list.get(index + 1));
            if(nextBusStopAnimation != null) {
                nextBusStopAnimation.stop();
                lastBusStopAnimation.stop();
            }
            nextBusStopAnimation = Misc.timerCounting(nextBusStopCount,LocalTime.parse(packet.list.get(index + 1),format), "Pozostały czas do\nnastępnego przystanku:\n","Opóźnienie do\nnastępnego przystanku:\n");
            lastBusStopAnimation = Misc.timerCounting(lastBusStopCount,LocalTime.parse(packet.list.get(packet.list.size() - 1),format), "Pozostały czas do\nkońca kursu:\n","Opóźnienie do\nostatniego przystanku\nw kursie:\n");
        }

    /**
     * Nacisnieto confirmButton, zmiana przystanku na nastepny
     *
     * @param actionEvent the action event
     */
    public void confirmButtonPressed(ActionEvent actionEvent) {
        if(index + 2 != packet.list.size()) {
            data.add(new Summary(packet.list.get(index),LocalTime.parse(packet.list.get(index + 1),format)));
            index += 2;
            refresh();
        }
        else
        {
            if(nextBusStopAnimation != null) {
                nextBusStopAnimation.stop();
                lastBusStopAnimation.stop();
                data.add(new Summary(packet.list.get(index),LocalTime.parse(packet.list.get(index + 1),format)));
                resultPage.setVisible(true);
            }
        }
    }
}