package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Order;
import com.university.shopping.model.OrderItem;
import com.university.shopping.model.Product;
import com.university.shopping.model.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class CsvPersistenceUtil {
    public static final String DATA_DIR = "data";
    public static final String USERS_FILE = DATA_DIR + "/users.csv";
    public static final String PRODUCTS_FILE = DATA_DIR + "/products.csv";
    public static final String ORDERS_FILE = DATA_DIR + "/orders.csv";
    public static final String ORDER_ITEMS_FILE = DATA_DIR + "/order_items.csv";

    private CsvPersistenceUtil() {}

    public static boolean writeUsersToCsv() {
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

        return writeToFile(USERS_FILE, sb.toString());
    }

    public static boolean writeProductsToCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,name,price,category,description,stockQuantity,isDiscounted,discountPercentage,imageName,imagePath\n");

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
                    .append(product.getDiscountPercentage()).append(",")
                    .append(escapeCsv(product.getImageName())).append(",")
                    .append(escapeCsv(product.getImagePath()))
                    .append("\n");
        }

        return writeToFile(PRODUCTS_FILE, sb.toString());
    }

    public static boolean writeOrdersToCsv() {
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

        return writeToFile(ORDERS_FILE, sb.toString());
    }

    public static boolean writeOrderItemsToCsv() {
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

        return writeToFile(ORDER_ITEMS_FILE, sb.toString());
    }

    public static String[] splitCsvLine(String line) {
        if (line == null) {
            return new String[0];
        }

        String[] result = new String[8];
        int resultCount = 0;
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                if (resultCount >= result.length) {
                    result = growArray(result);
                }
                result[resultCount++] = current.toString();
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        if (resultCount >= result.length) {
            result = growArray(result);
        }
        result[resultCount++] = current.toString();

        String[] compact = new String[resultCount];
        System.arraycopy(result, 0, compact, 0, resultCount);
        return compact;
    }

    private static String[] growArray(String[] source) {
        String[] expanded = new String[source.length * 2];
        System.arraycopy(source, 0, expanded, 0, source.length);
        return expanded;
    }

    public static String unescapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    private static boolean writeToFile(String path, String content) {
        Path targetPath = Paths.get(path);
        Path tempPath = Paths.get(path + ".tmp");

        try {
            Path parent = targetPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(tempPath, StandardCharsets.UTF_8)) {
                writer.write(content);
            }

            try {
                Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException moveNotSupportedException) {
                Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return true;
        } catch (IOException e) {
            ErrorLogger.logError("Failed to write CSV file: " + path, e);
            try {
                Files.deleteIfExists(tempPath);
            } catch (IOException ignored) {
                // Best-effort cleanup of temporary file
            }
            return false;
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        boolean shouldQuote = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String escaped = value.replace("\"", "\"\"");
        return shouldQuote ? "\"" + escaped + "\"" : escaped;
    }
}
