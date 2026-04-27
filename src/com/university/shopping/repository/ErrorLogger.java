package com.university.shopping.repository;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public final class ErrorLogger {
    private static final String ERROR_LOG_PATH = "data/errors.log";

    private ErrorLogger() {}

    public static void logMessage(String message) {
        appendLog(message, null);
    }

    public static void logError(String message, Exception e) {
        appendLog(message, e);
    }

    private static void appendLog(String message, Exception e) {
        try {
            Path parent = Paths.get(ERROR_LOG_PATH).getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (FileWriter writer = new FileWriter(ERROR_LOG_PATH, true)) {
                writer.write("[" + LocalDateTime.now() + "] " + message + System.lineSeparator());
                if (e != null) {
                    writer.write(e.toString() + System.lineSeparator());
                }
                writer.write(System.lineSeparator());
            }
        } catch (IOException logException) {
            logException.printStackTrace();
        }
    }
}
