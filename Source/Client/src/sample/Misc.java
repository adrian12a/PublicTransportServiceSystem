package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Rozne metody wykorzystywane przez rozne klasy
 */
public class Misc {
    /**
     * format wyswietlanej godziny
     */
    public static DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Zmiana sceny
     *
     * @param b      Przycisk
     * @param title  Nazwa sceny
     * @param fxml   Plik fxml
     * @param width  Szerokosc
     * @param height Wysokosc
     * @param c      Klasa
     * @throws Exception Exception
     */
    static public void changeScene(Button b, String title, String fxml, int width, int height, Class c) throws Exception {
        Stage stage = (Stage) b.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(FXMLLoader.load(c.getResource(fxml)), width, height));
        stage.show();
    }

    /**
     * Wyswietlanie aktualnej godziny
     *
     * @param label Label
     */
    static public void timer(Label label) {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            String min;
            String sec;
            if(currentTime.getMinute() < 10)
                min = "0" + currentTime.getMinute();
            else
                min = String.valueOf(currentTime.getMinute());
            if(currentTime.getSecond() < 10)
                sec = "0" + currentTime.getSecond();
            else
                sec = String.valueOf(currentTime.getSecond());
            label.setText("Aktualna godzina: " + currentTime.getHour() + ":" + min + ":" + sec);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    /**
     * Formatowanie Duration na String
     *
     * @param d Duration
     * @return String
     */
    static public String format(java.time.Duration d) {
        long seconds = d.toSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    /**
     * Wyswietlanie odliczania do danej godziny
     *
     * @param label Label
     * @param time  Czas
     * @param text1 Dodatkowy tekst do odliczania
     * @param text2 Dodatkowy tekst, gdy minal czas
     * @return Animacja odliczania
     */
    static public Timeline timerCounting(Label label, LocalTime time, String text1, String text2) {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            label.setText(text1 + format(java.time.Duration.between(LocalTime.now(), time)));
            if(label.getText().contains("-")) {
                label.setText(text2 + format(java.time.Duration.between(LocalTime.now(), time)));
                label.setText(label.getText().replace("-",""));
            }
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        return clock;
    }

    /**
     * Wyswietlanie odliczania do danej godziny, odswiezanie dla kolejnych przystankow dla pasazera
     *
     * @param label           Label
     * @param text            Dodatkowy tekst Labela
     * @param packet          Pakiet
     * @param index           Indeks przystanku
     * @param nextBusStop     Label wyswietlajacy nazwe nastepnego przystanku
     * @param nextBusStopTime Label wyswietlajacy godzine przyjazdu na nastepny przystanek
     * @return Animacja odliczania
     */
    static public Timeline timerCountingPassenger(Label label, String text, Packet packet, Integer index,Label nextBusStop, Label nextBusStopTime) {
        LocalTime time = LocalTime.parse(packet.list.get(index + 1),format);
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            nextBusStop.setText("Następny przystanek:\n" + packet.list.get(index));
            nextBusStopTime.setText("Godzina przyjazdu\nna następny przystanek:\n" + packet.list.get(index + 1));
            label.setText(text + format(java.time.Duration.between(LocalTime.now(), time)));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        ThreadPassenger t = new ThreadPassenger(label,text,index,time,packet,clock,nextBusStop,nextBusStopTime);
        t.start();
        return clock;
    }
}
