package sample;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalTime;

/**
 * Wyszukiwanie polaczen
 */
public class BusConnections implements Comparable<BusConnections> {
    /**
     * Id linii
     */
    String lineId;
    /**
     * Czas oczekiwania
     */
    Long timeToWait;
    /**
     * Znaleznione polaczenie
     */
    String foundResult;
    /**
     * Przystanek startowy
     */
    static String startBusStop;
    /**
     * Przystanek docelowy
     */
    static String destBusStop;
    /**
     * Id przystanku startowego
     */
    static String startBusStopID;
    /**
     * Id przystanku docelowego
     */
    static String destBusStopID;
    /**
     * Czas
     */
    static String time;

    /**
     * Konstruktor BusConnections, wyszukiwanie polaczen
     *
     * @param lineId Id linii
     */
    BusConnections(String lineId) {
        this.lineId = lineId;
        if(Handler.database == null)
            Handler.connectDB();
        try {
            String sql = "SELECT godzina_odjazdu FROM połączenia WHERE id_trasy = '" + lineId + "' AND id_przystanku = '" + startBusStopID + "';";
            Statement st = Handler.database.createStatement();
            ResultSet result = st.executeQuery(sql);
            result.next();
            LocalTime timeStart = LocalTime.parse(result.getString("godzina_odjazdu"));
            sql = "SELECT godzina_odjazdu FROM połączenia WHERE id_trasy = '" + lineId + "' AND id_przystanku = '" + destBusStopID + "';";
            result = st.executeQuery(sql);
            result.next();
            LocalTime timeEnd = LocalTime.parse(result.getString("godzina_odjazdu"));
            long travelDuration = Duration.between(timeStart, timeEnd).toMinutes();
            if(travelDuration < 0)
                foundResult = "-";
            else {
                timeToWait = Duration.between(LocalTime.parse(time), timeStart).toMinutes();
                foundResult = lineId + " " + startBusStop +  " " + timeStart +  " " + destBusStop +  " " + timeEnd;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            foundResult = "-";
        }
    }

    /**
     * porownywanie polaczen na podstawie czasu oczekiwania
     */
    @Override
    public int compareTo(BusConnections o) {
        if(o.timeToWait < 0)
            o.timeToWait *= -1;
        if(this.timeToWait < 0)
            this.timeToWait *= -1;
        if(this.timeToWait > o.timeToWait)
            return 1;
        else if(this.timeToWait == o.timeToWait)
            return 0;
        else
            return -1;
    }
}
