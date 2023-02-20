package sample;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static sample.Code.NONE;

/**
 * Watek klienta
 */
public class ClientThread implements Runnable {
    /**
     * Socket klienta
     */
    private Socket socket;
    /**
     * Strumien wychodzacy
     */
    private ObjectOutputStream out;
    /**
     * Strumien przychodzacy
     */
    private ObjectInputStream in;
    /**
     * Pakiet
     */
    private Packet packet;

    /**
     * Konstruktor watku klienta
     *
     * @param socket Socket klienta
     */
    public ClientThread(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException e) {
            e.printStackTrace();
            Log4j.error("Błąd wątku klienta");
        }
    }

    /**
     * Uruchomienie watku klienta, obsluga wiadomosci
     */
    @Override
    public void run() {
        try {
            while (true) {
                try {
                    packet = (Packet) in.readObject();
                    Log4j.info("Otrzymano wiadomość:" + packet.code);
                    packet = Handler.readMessage(packet);
                    if(packet.code != NONE) {
                        out.writeObject(packet);
                        Log4j.info("Wysłano wiadomość:" + packet.code);
                    }
                }
                catch (EOFException e) {
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Log4j.info("Klient rozlaczyl sie z serwerem");
            try {
                if (socket != null) {
                    socket.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            catch(Exception e) {
                Log4j.error("Błąd wątku klienta");
                e.printStackTrace();
            }
        }
    }
}
