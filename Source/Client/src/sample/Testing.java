package sample;

import org.junit.Test;

import static java.util.Objects.isNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Testy aplikacji za pomoca jUnit
 */
public class Testing {

    /**
     * Testowanie polaczenia z serwerem
     */
    @Test
    public void serverConnection() {
        assertEquals(0,Server.connect());
    }

    /**
     * Testowanie wysylania i odbierania wiadomosci z serwera (Domyslna wiadomosc)
     */
    @Test
    public void serverSendReceiveFirst() {
        Packet p = new Packet(Code.NONE);
        p = Server.send(p);
        assertFalse(isNull(p));
        assertEquals(Code.NOT_RECOGNIZED, p.code);
    }

    /**
     * Testowanie wysylania i odbierania wiadomosci z serwera (Logowanie)
     */
    @Test
    public void serverSendReceiveSecond() {
        Packet p = new Packet(Code.LOGIN);
        p.list.add("login");
        p.list.add("password");
        p = Server.send(p);
        assertFalse(isNull(p));
        assertEquals(Code.LOGIN_DENIED, p.code);
    }
}
