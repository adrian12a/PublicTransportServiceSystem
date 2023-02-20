package sample;

import org.junit.Test;

import java.sql.*;

import static java.util.Objects.isNull;
import static org.junit.Assert.assertFalse;

/**
 * Testowanie aplikacji za pomoca jUnit
 */
public class Testing {

    /**
     * Test tworzenia watku serwera
     */
    @Test
    public void initializeServer() {
        Server s = new Server();
        assertFalse(isNull(s));
    }

    /**
     * Test polaczenia z baza
     *
     * @throws SQLException the sql exception
     */
    @Test
    public void connectDatabaseTest() throws SQLException {
        Handler.database = DriverManager.getConnection("jdbc:mariadb://localhost:3306/projekt", "root", "");
        assertFalse(isNull(Handler.database));
    }

    /**
     * Test zapytan select z bazy danych
     *
     * @throws SQLException the sql exception
     */
    @Test
    public void selectTest() throws SQLException {
        if(Handler.database == null)
            connectDatabaseTest();
        Statement st = Handler.database.createStatement();
        ResultSet result = st.executeQuery("SELECT * FROM połączenia;");
        assertFalse(isNull(result.next()));
        result = st.executeQuery("SELECT * FROM przystanki;");
        assertFalse(isNull(result.next()));
        result = st.executeQuery("SELECT * FROM accounts;");
        assertFalse(isNull(result.next()));
    }
}
