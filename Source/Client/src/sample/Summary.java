package sample;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Przechowywanie informacji dla TreeTable wyswietlanej jako podsumowanie kursu Kierowcy (DriverPanel)
 */
public class Summary extends RecursiveTreeObject<Summary> {
    private final SimpleStringProperty busStop;
    private final SimpleStringProperty time;
    private final SimpleStringProperty status;

    /**
     * Konstruktor Summary
     *
     * @param busStop Nazwa przystanku
     * @param time    Czas przybycia na przystanek
     */
    Summary(String busStop, LocalTime time) {
        this.busStop = new SimpleStringProperty(busStop);
        this.time = new SimpleStringProperty(Misc.format(Duration.between(LocalTime.now(), time)));
        if(this.time.getValue().charAt(0) == '-')
            status = new SimpleStringProperty("Spóźnienie");
        else
            status = new SimpleStringProperty("OK");
    }

    /**
     * Pobranie nazwy przystanku
     *
     * @return nazwa przystanku
     */
    public String getBusStop() {
        return busStop.get();
    }

    /**
     * Pobranie czasu przybycia na przystanek
     *
     * @return Czas przybycia na przystanek
     */
    public String getTime() {
        return time.get();
    }

    /**
     * Pobranie oceny przejazdu
     *
     * @return Ocena przejazdu
     */
    public String getStatus() {
        return status.get();
    }
}

