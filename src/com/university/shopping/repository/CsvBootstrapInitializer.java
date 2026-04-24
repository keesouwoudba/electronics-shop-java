package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Order;
import com.university.shopping.model.OrderItem;
import com.university.shopping.model.Product;
import com.university.shopping.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvBootstrapInitializer {
    public CsvBootstrapInitializer() {}

    public void initialize() {
        clearDatabase();
        loadUsers("data/users.csv");
        loadProducts("data/products.csv");
        loadOrders("data/orders.csv");
        loadOrderItems("data/order_items.csv");
    }

    private void clearDatabase() {
        for (int i = 0; i < MockDatabase.users.length; i++) MockDatabase.users[i] = null;
        for (int i = 0; i < MockDatabase.products.length; i++) MockDatabase.products[i] = null;
        for (int i = 0; i < MockDatabase.orders.length; i++) MockDatabase.orders[i] = null;
        for (int i = 0; i < MockDatabase.carts.length; i++) MockDatabase.carts[i] = null;

        MockDatabase.userCount = 0;
        MockDatabase.productCount = 0;
        MockDatabase.orderCount = 0;
        MockDatabase.cartCount = 0;

        MockDatabase.nextUserId = 1;
        MockDatabase.nextProductId = 1;
        MockDatabase.nextOrderId = 1;
    }

    private void loadUsers(String filePath) {
        int maxId = 0;
        int index = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;

                int id = Integer.parseInt(parts[0].trim());
                String username = parts[1].trim();
                String password = parts[2].trim();
                boolean isAdmin = Boolean.parseBoolean(parts[3].trim());
                String createdDate = parts[4].trim();

                if (index < MockDatabase.users.length) {
                    MockDatabase.users[index++] = new User(id, username, password, isAdmin, createdDate);
                }

                if (id > maxId) maxId = id;
            }

            MockDatabase.userCount = index;
            MockDatabase.nextUserId = maxId + 1;

        } catch (IOException e) {
            e.printStackTrace();
            ErrorLogger.logError("Error reading users CSV file", e);
        }
    }

    private void loadProducts(String filePath) {
        int maxId = 0;
        int index = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 8) continue;

                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());
                String category = parts[3].trim();
                String description = parts[4].trim();
                int stockQuantity = Integer.parseInt(parts[5].trim());
                boolean isDiscounted = Boolean.parseBoolean(parts[6].trim());
                double discountPercentage = Double.parseDouble(parts[7].trim());

                if (index < MockDatabase.products.length) {
                    MockDatabase.products[index++] = new Product(
                            id, name, price, category, description, stockQuantity, isDiscounted, discountPercentage
                    );
                }

                if (id > maxId) maxId = id;
            }

            MockDatabase.productCount = index;
            MockDatabase.nextProductId = maxId + 1;

        } catch (IOException e) {
            e.printStackTrace();
            ErrorLogger.logError("Error reading users CSV file", e);
        }
    }

    private void loadOrders(String filePath) {
        int maxId = 0;
        int index = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;

                int orderId = Integer.parseInt(parts[0].trim());
                int userId = Integer.parseInt(parts[1].trim());
                String orderDate = parts[2].trim();
                double totalPrice = Double.parseDouble(parts[3].trim());
                String status = parts[4].trim();

                if (index < MockDatabase.orders.length) {
                    MockDatabase.orders[index++] = new Order(orderId, userId, orderDate, totalPrice, status, new OrderItem[0]);
                }

                if (orderId > maxId) maxId = orderId;
            }

            MockDatabase.orderCount = index;
            MockDatabase.nextOrderId = maxId + 1;

        } catch (IOException e) {
            e.printStackTrace();
            ErrorLogger.logError("Error reading users CSV file", e);
        }
    }

    private void loadOrderItems(String filePath) {
        Map<Integer, List<OrderItem>> itemsByOrderId = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;

                int orderId = Integer.parseInt(parts[0].trim());
                int productId = Integer.parseInt(parts[1].trim());
                String productName = parts[2].trim();
                int quantity = Integer.parseInt(parts[3].trim());
                double priceAtPurchase = Double.parseDouble(parts[4].trim());

                OrderItem item = new OrderItem(productId, productName, quantity, priceAtPurchase);
                itemsByOrderId.computeIfAbsent(orderId, key -> new java.util.ArrayList<>()).add(item);
            }

            for (int i = 0; i < MockDatabase.orderCount; i++) {
                Order order = MockDatabase.orders[i];
                if (order == null) continue;

                java.util.List<OrderItem> items = itemsByOrderId.get(order.getOrderId());
                order.setItems(items == null ? new OrderItem[0] : items.toArray(new OrderItem[0]));
            }

        } catch (IOException e) {
            e.printStackTrace();
            ErrorLogger.logError("Error reading users CSV file", e);
        }
    }

}
