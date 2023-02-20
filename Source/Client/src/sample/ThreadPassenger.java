package sample;

import javafx.animation.Timeline;
import javafx.scene.control.Label;

import java.time.LocalTime;

/**
 * Watek odswiezajacy informacje na glownym ekranie dla pasazera (PassengerPanel)
 */
public class ThreadPassenger extends Thread {
    /**
     * Label wyswietlajacy czas do nastepnego przystanku
     */
    Label label;
    /**
     * Label wyswietlajacy nazwe nastepnego przystanku
     */
    Label nextBusStop;
    /**
     * Label wyswietlajacy godzine przyjazdu na nastepny przystanek
     */
    Label nextBusStopTime;
    /**
     * Index aktualnego przystanku
     */
    Integer index;
    /**
     * Dodatkowy tekst wyswietlany w Label
     */
    String text;
    /**
     * Czas przyjazdu na nastepny przystanek
     */
    LocalTime time;
    /**
     * Pakiet
     */
    Packet packet;
    /**
     * Aktualna animacja
     */
    Timeline clock;

    /**
     * Konstruktor nowego watku pasazera
     *
     * @param label           Label wyswietlajacy czas do nastepnego przystanku
     * @param text            Dodatkowy tekst wyswietlany w Label
     * @param index           Index aktualnego przystanku
     * @param time            Czas przyjazdu na nastepny przystanek
     * @param packet          Pakiet
     * @param clock           Aktualna animacja
     * @param nextBusStop     Label wyswietlajacy nazwe nastepnego przystanku
     * @param nextBusStopTime Label wyswietlajacy godzine przyjazdu na nastepny przystanek
     */
    ThreadPassenger(Label label, String text, Integer index, LocalTime time, Packet packet, Timeline clock, Label nextBusStop, Label nextBusStopTime) {
        this.label = label;
        this.text = text;
        this.index = index;
        this.time = time;
        this.packet = packet;
        this.clock = clock;
        this.nextBusStop = nextBusStop;
        this.nextBusStopTime = nextBusStopTime;
    }

    /**
     * Uruchomienie watku, sprawdzanie czy nalezy zmienic informacje na kolejny przystanek
     */
    @Override
    public void run() {
        while(true){
            if(java.time.Duration.between(LocalTime.now(), time).isNegative()) {
                clock.stop();
                if(index + 2 != packet.list.size()) {
                    index += 2;
                    Misc.timerCountingPassenger(label,text,packet,index,nextBusStop,nextBusStopTime);
                }
                break;
            }
        }
    }
}
