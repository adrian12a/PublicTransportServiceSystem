package sample;

import org.apache.log4j.Logger;

/**
 * Logowanie zdarzen za pomoca Log4j
 */
public class Log4j {
    /**
     * Logger
     */
    final static Logger logger = Logger.getLogger(Log4j.class);

    /**
     * Logowanie zdarzenia typu informacja
     *
     * @param text Informacja o zdarzeniu
     */
    public static void info(String text) {
        logger.info(text);
        Main.c.insert("INFO  " + text);
    }

    /**
     * Logowanie zdarzenia typu blad
     *
     * @param text Informacja o zdarzeniu
     */
    public static void error(String text) {
        logger.error(text);
        Main.c.insert("ERROR  " + text);
    }
}
