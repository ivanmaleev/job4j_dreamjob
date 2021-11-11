package ru.job4j.dream.log;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class WebLogger {
    private static final Logger LOG = LogManager.getLogger(WebLogger.class.getName());

    public static Logger getLogger() {
        return LOG;
    }
}
