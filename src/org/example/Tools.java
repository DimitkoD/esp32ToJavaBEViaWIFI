package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Tools {
    private static final Logger logger = Logger.getLogger(Tools.class.getName());

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static final String loggTime = LocalDateTime.now().format(formatter);

    public static List<ClientHandler> clients = new ArrayList<>();

    public static void log(String origin, String message) {
        logger.info("[" + loggTime + " at " + origin + "] " + message);
    }

    public static void log(String origin, String[] messages) {
        for (String msg : messages) logger.info("[" + loggTime + " at " + origin + "] " + msg);
    }

    public static void log(String message) {
        logger.info("[" + loggTime + "] " + message);
    }


}