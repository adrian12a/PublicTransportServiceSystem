package sample;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * enum dla kodow wiadomosci
 */
enum Code {
    /**
     * Pusta wiadomosc
     */
    NONE,
    /**
     * Odpowiedz serwera - nie zidentyfikowano kodu
     */
    NOT_RECOGNIZED,
    /**
     * Zadanie klienta - logowanie
     */
    LOGIN,
    /**
     * Odpowiedz serwera - pomyslnie zalogowano
     */
    LOGIN_SUCCESS,
    /**
     * Odpowiedz serwera - niepowodzenie logowania
     */
    LOGIN_DENIED,
    /**
     * Zadanie klienta - rejestracja
     */
    REGISTER,
    /**
     * Odpowiedz serwera - pomyslnie zarejestrowano
     */
    REGISTER_SUCCESS,
    /**
     * Odpowiedz serwera - niepowodzenie rejestracji
     */
    REGISTER_DENIED,
    /**
     * Zadanie klienta - wykonanie prostego selecta do bazy
     */
    SELECT,
    /**
     * Odpowiedz serwera - select wykonany pomyslnie
     */
    SELECT_SUCCESS,
    /**
     * Odpowiedz serwera - niepowodzenie wykonania selecta
     */
    SELECT_DENIED,
    /**
     * Zadanie klienta - wyszukiwanie polaczenia autobusowego
     */
    CONNECTIONS,
    /**
     * Odpowiedz serwera - pomyslnie wyszukano polaczenia
     */
    CONNECTIONS_SUCCESS,
    /**
     * Odpowiedz serwera - niepowodzenie wyszukiwania polaczenia
     */
    CONNECTIONS_FAILED,
    /**
     * Zadanie klienta - wyszukiwanie informacji o polaczeniu dla panela glownego kierowcy/pasazera
     */
    PANEL,
    /**
     * Odpowiedz serwera - pomyslnie wyszukano informacje
     */
    PANEL_SUCCESS
}

/**
 * Pakiet wysylany/odbierany do/z serwera
 */
public class Packet implements Serializable {
    /**
     * Kod wiadomosci
     */
    Code code;
    /**
     * Przesylane dane
     */
    ArrayList<String> list = new ArrayList<>();

    /**
     * Konstruktor Packet
     *
     * @param code Kod wiadomosci
     */
    Packet(Code code) {
        this.code = code;
    }

    /**
     * Konstruktor Packet
     *
     * @param code    Kod wiadomosci
     * @param message Dane
     */
    Packet(Code code, String message) {
        this.code = code;
        list.add(message);
    }
}
