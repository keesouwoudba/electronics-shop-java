package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Order;
import com.university.shopping.model.OrderItem;
import com.university.shopping.model.Product;
import com.university.shopping.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class CsvBootstrapInitializer {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    private static boolean initialized = false;

    public CsvBootstrapInitializer() {
        initialize();
    }

    public void initialize() {
        if (initialized) {
            return;
        }

        ensureFilesWithHeaders();
        clearDatabase();
        loadUsers(CsvPersistenceUtil.USERS_FILE);
        loadProducts(CsvPersistenceUtil.PRODUCTS_FILE);
        loadOrders(CsvPersistenceUtil.ORDERS_FILE);
        loadOrderItems(CsvPersistenceUtil.ORDER_ITEMS_FILE);

        initialized = true;
    }

    private void ensureFilesWithHeaders() {
        ensureCsvFile(CsvPersistenceUtil.USERS_FILE, "id,username,password,isAdmin,createdDate");
        ensureCsvFile(CsvPersistenceUtil.PRODUCTS_FILE,
            "id,name,price,category,description,stockQuantity,isDiscounted,discountPercentage,imageName,imagePath");
        ensureCsvFile(CsvPersistenceUtil.ORDERS_FILE, "id,userId,orderDate,totalPrice,status");
        ensureCsvFile(CsvPersistenceUtil.ORDER_ITEMS_FILE,
                "orderId,productId,productName,quantity,priceAtPurchase");
    }

    private void ensureCsvFile(String filePath, String header) {
        Path path = Paths.get(filePath);
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (!Files.exists(path) || Files.size(path) == 0) {
                Files.write(path, (header + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            ErrorLogger.logError("Failed to ensure CSV file: " + filePath, e);
        }
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
            String header = reader.readLine();
            validateHeader(filePath, header, "id,username,password,isAdmin,createdDate");

            String line;
            int rowNumber = 1;

            while ((line = reader.readLine()) != null) {
                rowNumber++;
                if (line.isBlank()) continue;

                try {
                    String[] parts = CsvPersistenceUtil.splitCsvLine(line);
                    if (parts.length != 5) {
                        throw new IllegalArgumentException("Expected 5 columns but got " + parts.length);
                    }

                    int id = Integer.parseInt(parts[0].trim());
                    String username = CsvPersistenceUtil.unescapeCsv(parts[1].trim());
                    String password = CsvPersistenceUtil.unescapeCsv(parts[2].trim());
                    boolean isAdmin = parseBooleanStrict(parts[3].trim(), "isAdmin");
                    String createdDate = parseStrictDate(parts[4].trim(), "createdDate");

                    if (username.isEmpty() || password.isEmpty()) {
                        throw new IllegalArgumentException("username/password cannot be empty");
                    }

                    if (index < MockDatabase.users.length) {
                        MockDatabase.users[index++] = new User(id, username, password, isAdmin, createdDate);
                    }

                    if (id > maxId) maxId = id;
                } catch (NumberFormatException e) {
                    logRowIssue(filePath, rowNumber, line, "Numeric parse error", e);
                } catch (DateTimeParseException e) {
                    logRowIssue(filePath, rowNumber, line, "Date parse error", e);
                } catch (IllegalArgumentException e) {
                    logRowIssue(filePath, rowNumber, line, "Schema/state error", e);
                }
            }

            MockDatabase.userCount = index;
            MockDatabase.nextUserId = Math.max(maxId + 1, 1);

        } catch (IOException e) {
            ErrorLogger.logError("Error reading users CSV file", e);
        } catch (IllegalArgumentException e) {
            ErrorLogger.logError("Invalid users CSV schema", e);
        }
    }

    private void loadProducts(String filePath) {
        int maxId = 0;
        int index = 0;
        int rowNumber = 1;
        BufferedReader reader = null;

        // Step 3 requirement: one try with multiple catches and finally.
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String header = reader.readLine();
            validateHeader(filePath, header,
                    "id,name,price,category,description,stockQuantity,isDiscounted,discountPercentage");
            // Allow trailing columns for backward compatibility in header parsing if needed

            String line;
            while ((line = reader.readLine()) != null) {
                rowNumber++;
                if (line.isBlank()) continue;

                try {
                    String[] parts = CsvPersistenceUtil.splitCsvLine(line);
                    if (parts.length < 8) {
                        throw new IllegalArgumentException("Expected min 8 columns but got " + parts.length);
                    }

                    int id = Integer.parseInt(parts[0].trim());
                    String name = CsvPersistenceUtil.unescapeCsv(parts[1].trim());
                    double price = Double.parseDouble(parts[2].trim());
                    String category = CsvPersistenceUtil.unescapeCsv(parts[3].trim());
                    String description = CsvPersistenceUtil.unescapeCsv(parts[4].trim());
                    int stockQuantity = Integer.parseInt(parts[5].trim());
                    boolean isDiscounted = parseBooleanStrict(parts[6].trim(), "isDiscounted");
                    double discountPercentage = Double.parseDouble(parts[7].trim());
                    
                    String imageName = "";
                    String imagePath = "";
                    if (parts.length >= 10) {
                        imageName = CsvPersistenceUtil.unescapeCsv(parts[8].trim());
                        imagePath = CsvPersistenceUtil.unescapeCsv(parts[9].trim());
                    }

                    if (name.isEmpty() || category.isEmpty()) {
                        throw new IllegalArgumentException("name/category cannot be empty");
                    }

                    if (index < MockDatabase.products.length) {
                        MockDatabase.products[index++] = new Product(
                                id, name, price, category, description, stockQuantity, isDiscounted, discountPercentage, imageName, imagePath
                        );
                    }

                    if (id > maxId) maxId = id;
                } catch (NumberFormatException e) {
                    logRowIssue(filePath, rowNumber, line, "Numeric parse error", e);
                } catch (IllegalArgumentException e) {
                    logRowIssue(filePath, rowNumber, line, "Schema/state error", e);
                }
            }

            MockDatabase.productCount = index;
            MockDatabase.nextProductId = Math.max(maxId + 1, 1);
        } catch (NumberFormatException e) {
            ErrorLogger.logError("Product CSV numeric parse error", e);
        } catch (DateTimeParseException e) {
            ErrorLogger.logError("Product CSV date parse error", e);
        } catch (IOException e) {
            ErrorLogger.logError("Error reading products CSV file", e);
        } catch (IllegalArgumentException e) {
            ErrorLogger.logError("Invalid products CSV schema/state", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException closeError) {
                    ErrorLogger.logError("Failed closing products CSV reader", closeError);
                }
            }
        }
    }

    private void loadOrders(String filePath) {
        int maxId = 0;
        int index = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine();
            validateHeader(filePath, header, "id,userId,orderDate,totalPrice,status");

            String line;
            int rowNumber = 1;

            while ((line = reader.readLine()) != null) {
                rowNumber++;
                if (line.isBlank()) continue;

                try {
                    String[] parts = CsvPersistenceUtil.splitCsvLine(line);
                    if (parts.length != 5) {
                        throw new IllegalArgumentException("Expected 5 columns but got " + parts.length);
                    }

                    int orderId = Integer.parseInt(parts[0].trim());
                    int userId = Integer.parseInt(parts[1].trim());
                    String orderDate = parseStrictDate(parts[2].trim(), "orderDate");
                    double totalPrice = Double.parseDouble(parts[3].trim());
                    String status = CsvPersistenceUtil.unescapeCsv(parts[4].trim());

                    if (status.isEmpty()) {
                        throw new IllegalArgumentException("status cannot be empty");
                    }

                    if (index < MockDatabase.orders.length) {
                        MockDatabase.orders[index++] = new Order(orderId, userId, orderDate, totalPrice, status, new OrderItem[0]);
                    }

                    if (orderId > maxId) maxId = orderId;
                } catch (NumberFormatException e) {
                    logRowIssue(filePath, rowNumber, line, "Numeric parse error", e);
                } catch (DateTimeParseException e) {
                    logRowIssue(filePath, rowNumber, line, "Date parse error", e);
                } catch (IllegalArgumentException e) {
                    logRowIssue(filePath, rowNumber, line, "Schema/state error", e);
                }
            }

            MockDatabase.orderCount = index;
            MockDatabase.nextOrderId = Math.max(maxId + 1, 1);

        } catch (IOException e) {
            ErrorLogger.logError("Error reading orders CSV file", e);
        } catch (IllegalArgumentException e) {
            ErrorLogger.logError("Invalid orders CSV schema", e);
        }
    }

    private void loadOrderItems(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine();
            validateHeader(filePath, header, "orderId,productId,productName,quantity,priceAtPurchase");

            String line;
            int rowNumber = 1;

            while ((line = reader.readLine()) != null) {
                rowNumber++;
                if (line.isBlank()) continue;

                try {
                    String[] parts = CsvPersistenceUtil.splitCsvLine(line);
                    if (parts.length != 5) {
                        throw new IllegalArgumentException("Expected 5 columns but got " + parts.length);
                    }

                    int orderId = Integer.parseInt(parts[0].trim());
                    int productId = Integer.parseInt(parts[1].trim());
                    String productName = CsvPersistenceUtil.unescapeCsv(parts[2].trim());
                    int quantity = Integer.parseInt(parts[3].trim());
                    double priceAtPurchase = Double.parseDouble(parts[4].trim());

                    if (productName.isEmpty()) {
                        throw new IllegalArgumentException("productName cannot be empty");
                    }

                    OrderItem item = new OrderItem(productId, productName, quantity, priceAtPurchase);
                    Order order = findLoadedOrderById(orderId);
                    if (order == null) {
                        throw new IllegalArgumentException("Order not found for orderId " + orderId);
                    }

                    order.setItems(appendOrderItem(order.getItems(), item));
                } catch (NumberFormatException e) {
                    logRowIssue(filePath, rowNumber, line, "Numeric parse error", e);
                } catch (IllegalArgumentException e) {
                    logRowIssue(filePath, rowNumber, line, "Schema/state error", e);
                }
            }

        } catch (IOException e) {
            ErrorLogger.logError("Error reading order_items CSV file", e);
        } catch (IllegalArgumentException e) {
            ErrorLogger.logError("Invalid order_items CSV schema", e);
        }
    }

    private Order findLoadedOrderById(int orderId) {
        for (int i = 0; i < MockDatabase.orderCount; i++) {
            Order order = MockDatabase.orders[i];
            if (order != null && order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }

    private OrderItem[] appendOrderItem(OrderItem[] existingItems, OrderItem item) {
        if (existingItems == null || existingItems.length == 0) {
            return new OrderItem[] { item };
        }

        OrderItem[] expanded = new OrderItem[existingItems.length + 1];
        System.arraycopy(existingItems, 0, expanded, 0, existingItems.length);
        expanded[existingItems.length] = item;
        return expanded;
    }

    private void validateHeader(String filePath, String header, String expectedHeader) {
        if (header == null) {
            throw new IllegalArgumentException("Missing header in " + filePath);
        }
        if (!expectedHeader.equals(header.trim())) {
            throw new IllegalArgumentException(
                    "Header mismatch in " + filePath + ". expected='" + expectedHeader + "' actual='" + header + "'");
        }
    }

    private boolean parseBooleanStrict(String value, String columnName) {
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        throw new IllegalArgumentException("Invalid boolean for " + columnName + ": " + value);
    }

    private String parseStrictDate(String value, String columnName) {
        try {
            LocalDate parsed = LocalDate.parse(value, DATE_FORMATTER);
            return parsed.format(DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Invalid " + columnName + " value: " + value, value, e.getErrorIndex(), e);
        }
    }

    private void logRowIssue(String filePath, int rowNumber, String rowContent, String reason, Exception e) {
        ErrorLogger.logError(filePath + " row " + rowNumber + " - " + reason + " | content: " + rowContent, e);
    }

}
