package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Komunikacja z serwerem
 */
public class Server {
    static private Socket socket;
    static private ObjectInputStream in;
    static private ObjectOutputStream out;
    final static private String host = "localhost";
    final static private Integer port = 44444;

    /**
     * Polaczenie z serwerem
     *
     * @return Zwraca 0, gdy pomyslnie polaczono z serwerem
     */
    static int connect() {
        try {
            socket = new Socket(host,port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            return 0;
        }
        catch (IOException e) {
            e.printStackTrace();
            Log4j.logger.error("Blad laczenia z serwerem " + e.getMessage());
            return -1;
        }
    }

    /**
     * Wyslanie wiadomosci do serwera
     *
     * @param outPacket Pakiet wychodzacy
     * @return Pakiet przychodzacy
     */
    static Packet send(Packet outPacket) {
        Packet inPacket = null;
        try {
            out.writeObject(outPacket);
            inPacket = (Packet) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            Log4j.logger.error("Błąd wysyłania/odbierania wiadomości do/z serwera");
        }
        return inPacket;
    }

    /**
     * Zamkniecie polaczenia z serwerem
     */
    static void close() {
        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
