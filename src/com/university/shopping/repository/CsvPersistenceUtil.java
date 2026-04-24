package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Order;
import com.university.shopping.model.OrderItem;
import com.university.shopping.model.Product;
import com.university.shopping.model.User;

import java.io.FileWriter;
import java.io.IOException;

public final class CsvPersistenceUtil {
    private CsvPersistenceUtil() {}

    public static void writeUsersToCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,username,password,isAdmin,createdDate\n");

        for (int i = 0; i < MockDatabase.userCount; i++) {
            User user = MockDatabase.users[i];
            if (user == null) continue;

            sb.append(user.getUserId()).append(",")
                    .append(escapeCsv(user.getUsername())).append(",")
                    .append(escapeCsv(user.getPassword())).append(",")
                    .append(user.isAdmin()).append(",")
                    .append(escapeCsv(user.getCreatedDate()))
                    .append("\n");
        }

        writeToFile("data/users.csv", sb.toString());
    }

    public static void writeProductsToCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,name,price,category,description,stockQuantity,isDiscounted,discountPercentage\n");

        for (int i = 0; i < MockDatabase.productCount; i++) {
            Product product = MockDatabase.products[i];
            if (product == null) continue;

            sb.append(product.getProductId()).append(",")
                    .append(escapeCsv(product.getName())).append(",")
                    .append(product.getPrice()).append(",")
                    .append(escapeCsv(product.getCategory())).append(",")
                    .append(escapeCsv(product.getDescription())).append(",")
                    .append(product.getStockQuantity()).append(",")
                    .append(product.isDiscounted()).append(",")
                    .append(product.getDiscountPercentage())
                    .append("\n");
        }

        writeToFile("data/products.csv", sb.toString());
    }

    public static void writeOrdersToCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,userId,orderDate,totalPrice,status\n");

        for (int i = 0; i < MockDatabase.orderCount; i++) {
            Order order = MockDatabase.orders[i];
            if (order == null) continue;

            sb.append(order.getOrderId()).append(",")
                    .append(order.getUserId()).append(",")
                    .append(escapeCsv(order.getOrderDate())).append(",")
                    .append(order.getTotalPrice()).append(",")
                    .append(escapeCsv(order.getStatus()))
                    .append("\n");
        }

        writeToFile("data/orders.csv", sb.toString());
    }

    public static void writeOrderItemsToCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("orderId,productId,productName,quantity,priceAtPurchase\n");

        for (int i = 0; i < MockDatabase.orderCount; i++) {
            Order order = MockDatabase.orders[i];
            if (order == null || order.getItems() == null) continue;

            for (OrderItem item : order.getItems()) {
                if (item == null) continue;

                sb.append(order.getOrderId()).append(",")
                        .append(item.getProductId()).append(",")
                        .append(escapeCsv(item.getProductName())).append(",")
                        .append(item.getQuantity()).append(",")
                        .append(item.getPriceAtPurchase())
                        .append("\n");
            }
        }

        writeToFile("data/order_items.csv", sb.toString());
    }

    private static void writeToFile(String path, String content) {
        try (FileWriter writer = new FileWriter(path, false)) {
            writer.write(content);
        } catch (IOException e) {
            ErrorLogger.logError("Failed to write CSV file: " + path, e);
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        boolean shouldQuote = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String escaped = value.replace("\"", "\"\"");
        return shouldQuote ? "\"" + escaped + "\"" : escaped;
    }
}
