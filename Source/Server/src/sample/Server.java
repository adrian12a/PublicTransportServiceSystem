package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Glowna klasa serwera
 */
public class Server implements Runnable {
    static private ServerSocket serverSocket;
    static private final Integer port = 44444;

    /**
     * Uruchomienie serwera
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Log4j.info("Uruchomiono serwer na porcie: " + port);
            while(true) {
                Socket client = serverSocket.accept();
                Log4j.info("Nowy klient podlaczyl sie do serwera");
                Controller.executor.execute(new ClientThread(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log4j.error("Blad serwera " + e.getMessage());
        }
    }
}