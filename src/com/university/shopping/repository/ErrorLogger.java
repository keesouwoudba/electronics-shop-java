package com.university.shopping.repository;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public final class ErrorLogger {
    private ErrorLogger() {}

    public static void logError(String message, Exception e) {
        try (FileWriter writer = new FileWriter("data/errors.log", true)) {
            writer.write("[" + LocalDateTime.now() + "] " + message + System.lineSeparator());
            writer.write(e.toString() + System.lineSeparator());
            writer.write(System.lineSeparator());
        } catch (IOException logException) {
            logException.printStackTrace();
        }
    }
}
