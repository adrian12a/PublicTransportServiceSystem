package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

import static sample.Code.*;

/**
 * Obsluga wiadomosci od klienta
 */
public class Handler {
    /**
     * Polaczenie z baza
     */
    public static Connection database;

    /**
     * Odczyt wiadomosci
     *
     * @param packet Pakiet przychodzacy
     * @return Pakiet wychodzacy
     */
    public static Packet readMessage(Packet packet) {
        Packet returnPacket = new Packet(NONE);
        switch(packet.code) {
            case LOGIN:
                String type = loginClient(packet.list.get(0),packet.list.get(1));
                if(type != null) {
                    returnPacket.code = LOGIN_SUCCESS;
                    returnPacket.list.add(type);
                }
                else {
                    returnPacket.code = LOGIN_DENIED;
                    returnPacket.list.add("Wprowadzono bledne dane");
                }
                break;
            case REGISTER:
                if(registerClient(packet.list.get(0),packet.list.get(1),packet.list.get(2),packet.list.get(3))) {
                    returnPacket.code = REGISTER_SUCCESS;
                    returnPacket.list.add("Pomyslnie utworozno konto");
                }
                else {
                    returnPacket.code = REGISTER_DENIED;
                    returnPacket.list.add("Blad tworzenia konta");
                }
                break;
            case SELECT:
                returnPacket = select(packet.list.get(0),packet.list.get(1));
                break;
            case CONNECTIONS:
                returnPacket = findConnections(packet.list.get(0),packet.list.get(1),packet.list.get(2));
                break;
            case PANEL:
                returnPacket = stats(packet.list.get(0));
                break;
            default:
                returnPacket.code = NOT_RECOGNIZED;
                returnPacket.list.add("Nie rozpoznano kodu wiadomosci");
        }
        return returnPacket;
    }

    /**
     * Obsluga wiadomosci typu PANEL
     *
     * @return Pakiet wychodzacy
     */
    private static Packet stats(String line) {
        Packet packet = new Packet(PANEL_SUCCESS);
        if (database == null)
            connectDB();
        try {
            Statement st = database.createStatement();
            ResultSet result = st.executeQuery("SELECT id_przystanku,godzina_odjazdu  FROM połączenia WHERE id_trasy = '" + line + "';");
            while (result.next()) {
                packet.list.add(result.getString("id_przystanku"));
                packet.list.add(result.getString("godzina_odjazdu"));
            }
            for(int i = 0; i < packet.list.size(); i+=2) {
                result = st.executeQuery("SELECT ulica FROM przystanki WHERE id_przystanku = '" + packet.list.get(i) + "';");
                result.next();
                packet.list.set(i,result.getString("ulica"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return packet;
    }

    /**
     * Obsluga wiadomosci typu SELECT
     *
     * @param sql    Kod sql
     * @param column Kolumna w bazie danych
     * @return Pakiet wychodzacy
     */
    static Packet select(String sql, String column) {
        Packet packet = new Packet(SELECT_SUCCESS);
        if (database == null)
            connectDB();
        try {
            Statement st = database.createStatement();
            ResultSet result = st.executeQuery(sql);
            while (result.next()) {
                packet.list.add(result.getString(column));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return packet;
    }

    /**
     * Obsluga wiadomosci typu LOGIN
     *
     * @param login    Login
     * @param password Haslo
     * @return Typ konta
     */
    static String loginClient(String login, String password) {
        String sql = "SELECT login, password, type FROM accounts WHERE login = '" + login + "' AND password = '" + password + "';";
        if(database == null)
            connectDB();
        try {
            Statement st = database.createStatement();
            ResultSet result = st.executeQuery(sql);
            if (!result.next())
                return null;
            else
                return result.getString("type");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obsluga wiadomosci typu REGISTER
     *
     * @param login    Login
     * @param password Haslo
     * @param email    Email
     * @param type     Typ konta
     * @return Zwraca true, gdy sie powiedzie
     */
    static boolean registerClient(String login, String password, String email, String type) {
        String sql = "INSERT INTO accounts VALUES ('" + login + "','" + password + "','" + email + "','" + type + "');";
        if(database == null)
            connectDB();
        try {
            Statement st = database.createStatement();
            st.executeQuery(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Obsluga wiadomosci typu CONNECTIONS
     *
     * @param time         Czas
     * @param startBusStop Startowy przystanek
     * @param destBusStop  Docelowy przystanek
     * @return Pakiet wychodzacy
     */
    static Packet findConnections (String time, String startBusStop, String destBusStop) {
        String sql = "SELECT id_przystanku FROM przystanki WHERE ulica = '" + startBusStop + "';";
        Packet packet = new Packet(CONNECTIONS_SUCCESS);
        BusConnections.startBusStop = startBusStop;
        BusConnections.destBusStop = destBusStop;
        if(database == null)
            connectDB();
        try {
            Statement st = database.createStatement();
            ResultSet result = st.executeQuery(sql);
            result.next();
            startBusStop = result.getString("id_przystanku");
            sql = "SELECT id_przystanku FROM przystanki WHERE ulica = '" + destBusStop + "';";
            result = st.executeQuery(sql);
            result.next();
            destBusStop = result.getString("id_przystanku");
            sql = "SELECT id_trasy FROM połączenia WHERE id_przystanku = '" + startBusStop + "' AND id_trasy IN (SELECT id_trasy FROM połączenia WHERE id_przystanku = '" + destBusStop + "');";
            result = st.executeQuery(sql);
            ArrayList<BusConnections> lines = new ArrayList<>();
            BusConnections.startBusStopID = startBusStop;
            BusConnections.destBusStopID = destBusStop;
            BusConnections.time = time;
            int removed = 0;
            while(result.next())
                lines.add(new BusConnections(result.getString("id_trasy")));
            for(int i = 0; i < lines.size(); i++){
                if(lines.get(i).foundResult.charAt(0) == '-') {
                    lines.remove(i);
                    removed++;
                }
            }
            int size = lines.size();
            if(lines.size() > 4)
                size = 4;
            size = size - removed;
            Collections.sort(lines);
            for(int i = 0; i < size; i++) {
                packet.list.add(lines.get(i).foundResult);
            }
        }
        catch (SQLException e)
        {
            packet.code = CONNECTIONS_FAILED;
        }
        if(packet.list.size() == 0)
            packet.code = CONNECTIONS_FAILED;
        return packet;
    }

    /**
     * Polaczenie z baza danych
     */
    static void connectDB() {
        try {
            database = DriverManager.getConnection("jdbc:mariadb://localhost:3306/projekt", "root", "");
            Log4j.info("Pomyślnie połączono z bazą danych");
        } catch (SQLException throwables) {
            Log4j.error("Błąd łączenia z bazą danych");
            throwables.printStackTrace();
        }
    }
}