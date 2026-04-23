package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class CsvBootstrapInitializer {
    public CsvBootstrapInitializer() {}
    public void initialize() {
        loadUsers("data/users.csv");
        //loadProducts("data/products.csv");
        //loadOrders("data/orders.csv");
        //loadOrderItems("data/order_items.csv");
    }
    private void loadUsers(String filePath) {
        int maxId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                if (firstLine) {
                    firstLine = false;
                    if (line.toLowerCase().contains("id")) {
                        continue;
                    }
                }

                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                int id = Integer.parseInt(parts[0].trim());
                String username = parts[1].trim();
                String password = parts[2].trim();
                boolean isAdmin = Boolean.parseBoolean(parts[3].trim());
                String createdDate = parts[4].trim();

                new User(id, username, password, isAdmin, createdDate);

                if (id > maxId) {
                    maxId = id;
                }
            }

            MockDatabase.nextUserId = maxId + 1;

        } catch (IOException e) {
            e.printStackTrace();
            logError("Error reading CSV file", e);
        }


    }
    private void logError(String message, Exception e) {
        try (FileWriter writer = new FileWriter("data/errors.log", true)) {
            writer.write("[" + LocalDateTime.now() + "] " + message + System.lineSeparator());
            writer.write(e.toString() + System.lineSeparator());
            writer.write(System.lineSeparator());
        } catch (IOException logException) {
            logException.printStackTrace();
        }
    }
}
