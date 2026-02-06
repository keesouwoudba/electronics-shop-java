package com.university.shopping.view;

import com.university.shopping.model.*;
import com.university.shopping.service.*;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private AuthService authService;
    private ShopService shopService;
    private AdminService adminService;

    // Constructor with dependency injection
    public ConsoleUI(AuthService authService, ShopService shopService, AdminService adminService) {
        this.scanner = new Scanner(System.in);
        this.authService = authService;
        this.shopService = shopService;
        this.adminService = adminService;
    }

    // Main entry point - starts the application
    public void start() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║   WELCOME TO ELECTRONICS SHOPPING SYSTEM  ║");
        System.out.println("╚═══════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            if (!authService.isLoggedIn()) {
                running = showMainMenu();
            } else {
                if (authService.isAdmin()) {
                    showAdminMenu();
                } else {
                    showCustomerMenu();
                }
            }
        }

        System.out.println("\n✨ Thank you for using Electronics Shopping System! ✨\n");
        scanner.close();
    }

    // ==================== MAIN MENU (Not Logged In) ====================

    private boolean showMainMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│          MAIN MENU                  │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. Login                           │");
        System.out.println("│  2. Register                        │");
        System.out.println("│  3. Browse Products (Guest)         │");
        System.out.println("│  4. Exit                            │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                loginFlow();
                break;
            case 2:
                registerFlow();
                break;
            case 3:
                browseProductsAsGuest();
                break;
            case 4:
                return false; // Exit application
            default:
                System.out.println("❌ Invalid choice. Please try again.");
        }

        return true; // Continue running
    }

    // ==================== AUTHENTICATION FLOWS ====================

    private void loginFlow() {
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║            LOGIN                  ║");
        System.out.println("╚═══════════════════════════════════╝");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        String result = authService.login(username, password);

        if (result.equals("Successfull login")) {
            System.out.println("\n✅ Login successful!");
            System.out.println("Welcome, " + authService.getCurrentUser().getUsername() + "!");

            if (authService.isAdmin()) {
                System.out.println("🔑 Admin privileges detected.");
            }
        } else {
            System.out.println("\n❌ Login failed: " + result);
        }

        pauseScreen();
    }

    private void registerFlow() {
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║          REGISTRATION             ║");
        System.out.println("╚═══════════════════════════════════╝");

        System.out.print("Choose a username: ");
        String username = scanner.nextLine();

        System.out.print("Create a password (8+ chars, uppercase, number): ");
        String password = scanner.nextLine();

        String result = authService.register(username, password);

        if (result.equals("Successfully registered")) {
            System.out.println("\n✅ Registration successful!");
            System.out.println("You are now logged in as: " + authService.getCurrentUser().getUsername());
        } else {
            System.out.println("\n❌ Registration failed: " + result);
        }

        pauseScreen();
    }

    // ==================== CUSTOMER MENU ====================

    private void showCustomerMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│        CUSTOMER MENU                │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. Browse All Products             │");
        System.out.println("│  2. View Product Details            │");
        System.out.println("│  3. Add Product to Cart             │");
        System.out.println("│  4. View My Cart                    │");
        System.out.println("│  5. Remove Item from Cart           │");
        System.out.println("│  6. Checkout                        │");
        System.out.println("│  7. Logout                          │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                browseAllProducts();
                break;
            case 2:
                viewProductDetails();
                break;
            case 3:
                addProductToCart();
                break;
            case 4:
                viewCart();
                break;
            case 5:
                removeFromCart();
                break;
            case 6:
                checkout();
                break;
            case 7:
                authService.logout();
                System.out.println("\n✅ Logged out successfully.");
                pauseScreen();
                break;
            default:
                System.out.println("❌ Invalid choice. Please try again.");
        }
    }

    // ==================== CUSTOMER FEATURES ====================

    private void browseProductsAsGuest() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    AVAILABLE PRODUCTS                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        Product[] products = shopService.getAllProducts();

        if (products == null || products.length == 0) {
            System.out.println("No products available.");
        } else {
            displayProductTable(products);
        }

        System.out.println("\n💡 Please login or register to add items to cart.");
        pauseScreen();
    }

    private void browseAllProducts() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    AVAILABLE PRODUCTS                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        Product[] products = shopService.getAllProducts();

        if (products == null || products.length == 0) {
            System.out.println("No products available.");
        } else {
            displayProductTable(products);
        }

        pauseScreen();
    }

    private void viewProductDetails() {
        System.out.print("\nEnter Product ID to view details: ");
        int productId = getIntInput();

        Product product = shopService.getProductById(productId);

        if (product == null) {
            System.out.println("❌ Product not found.");
        } else {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    PRODUCT DETAILS                            ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            System.out.println("ID:          " + product.getProductId());
            System.out.println("Name:        " + product.getName());
            System.out.println("Category:    " + product.getCategory());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Price:       $" + String.format("%.2f", product.getPrice()));
            System.out.println("Stock:       " + product.getStockQuantity() + " units");

            if (product.isDiscounted()) {
                System.out.println("Discount:    " + product.getDiscountPercentage() + "% OFF");
                System.out.println("Final Price: $" + String.format("%.2f", product.getFinalPrice()));
            }
        }

        pauseScreen();
    }

    private void addProductToCart() {
        System.out.print("\nEnter Product ID: ");
        int productId = getIntInput();

        System.out.print("Enter Quantity: ");
        int quantity = getIntInput();

        if (quantity <= 0) {
            System.out.println("❌ Quantity must be positive.");
            pauseScreen();
            return;
        }

        int userId = authService.getCurrentUser().getUserId();
        boolean result = shopService.addToCart(productId, quantity, userId);

        if (result) {
            System.out.println("✅ Product added to cart successfully!");
        } else {
            System.out.println("❌ Failed to add product. Product may not exist or insufficient stock.");
        }

        pauseScreen();
    }

    private void viewCart() {
        int userId = authService.getCurrentUser().getUserId();
        Cart cart = shopService.viewCart(userId);

        if (cart == null || cart.getItemCount() == 0) {
            System.out.println("\n🛒 Your cart is empty.");
        } else {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                      YOUR CART                                ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");

            OrderItem[] items = cart.getItems();

            System.out.println("┌──────┬─────────────────────────┬──────────┬────────────┬────────────┐");
            System.out.println("│  ID  │      Product Name       │ Quantity │   Price    │   Total    │");
            System.out.println("├──────┼─────────────────────────┼──────────┼────────────┼────────────┤");

            for (int i = 0; i < cart.getItemCount(); i++) {
                if (items[i] != null) {
                    OrderItem item = items[i];
                    double itemTotal = item.getQuantity() * item.getPriceAtPurchase();
                    System.out.printf("│ %-4d │ %-23s │ %-8d │ $%-9.2f │ $%-9.2f │%n",
                            item.getProductId(),
                            truncateString(item.getProductName(), 23),
                            item.getQuantity(),
                            item.getPriceAtPurchase(),
                            itemTotal);
                }
            }

            System.out.println("└──────┴─────────────────────────┴──────────┴────────────┴────────────┘");
            System.out.printf("\n💰 TOTAL: $%.2f%n", cart.getTotalPrice());
        }

        pauseScreen();
    }

    private void removeFromCart() {
        System.out.print("\nEnter Product ID to remove: ");
        int productId = getIntInput();

        int userId = authService.getCurrentUser().getUserId();
        boolean result = shopService.removeFromCart(productId, userId);

        if (result) {
            System.out.println("✅ Product removed from cart.");
        } else {
            System.out.println("❌ Failed to remove product.");
        }

        pauseScreen();
    }

    private void checkout() {
        int userId = authService.getCurrentUser().getUserId();
        Cart cart = shopService.viewCart(userId);

        if (cart == null || cart.getItemCount() == 0) {
            System.out.println("\n❌ Your cart is empty. Add items before checkout.");
            pauseScreen();
            return;
        }

        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      CHECKOUT                                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.printf("Total Amount: $%.2f%n", cart.getTotalPrice());
        System.out.print("\nConfirm purchase? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            String result = shopService.checkout(userId);

            if (result.equals("Success")) {
                System.out.println("\n✅ Order placed successfully!");
                System.out.println("Thank you for your purchase!");
            } else {
                System.out.println("\n❌ Checkout failed: " + result);
            }
        } else {
            System.out.println("\n❌ Checkout cancelled.");
        }

        pauseScreen();
    }

    // ==================== ADMIN MENU ====================

    private void showAdminMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│       🔑 ADMIN PANEL                │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. Product Management              │");
        System.out.println("│  2. User Management                 │");
        System.out.println("│  3. View All Products               │");
        System.out.println("│  4. View All Users                  │");
        System.out.println("│  5. Logout                          │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                productManagementMenu();
                break;
            case 2:
                userManagementMenu();
                break;
            case 3:
                viewAllProductsAdmin();
                break;
            case 4:
                viewAllUsers();
                break;
            case 5:
                authService.logout();
                System.out.println("\n✅ Logged out successfully.");
                pauseScreen();
                break;
            default:
                System.out.println("❌ Invalid choice. Please try again.");
        }
    }

    // ==================== ADMIN - PRODUCT MANAGEMENT ====================

    private void productManagementMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│     PRODUCT MANAGEMENT              │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. Add New Product                 │");
        System.out.println("│  2. Update Product Price            │");
        System.out.println("│  3. Update Product Name             │");
        System.out.println("│  4. Set Product Discount            │");
        System.out.println("│  5. Add Stock                       │");
        System.out.println("│  6. Remove Stock                    │");
        System.out.println("│  7. Delete Product                  │");
        System.out.println("│  8. Back to Admin Menu              │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                addNewProduct();
                break;
            case 2:
                updateProductPrice();
                break;
            case 3:
                updateProductName();
                break;
            case 4:
                setProductDiscount();
                break;
            case 5:
                addStock();
                break;
            case 6:
                removeStock();
                break;
            case 7:
                deleteProduct();
                break;
            case 8:
                return; // Back to admin menu
            default:
                System.out.println("❌ Invalid choice. Please try again.");
        }
    }

    private void addNewProduct() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ADD NEW PRODUCT                            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        System.out.print("Product Name: ");
        String name = scanner.nextLine();

        System.out.print("Price: $");
        double price = getDoubleInput();

        System.out.print("Category: ");
        String category = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Initial Stock Quantity: ");
        int stock = getIntInput();

        System.out.print("Is Discounted? (true/false): ");
        boolean isDiscounted = scanner.nextLine().equalsIgnoreCase("true");

        double discountPercentage = 0;
        if (isDiscounted) {
            System.out.print("Discount Percentage: ");
            discountPercentage = getDoubleInput();
        }

        Product newProduct = new Product(name, price, category, description, stock, isDiscounted, discountPercentage);
        String result = adminService.addNewProduct(newProduct);

        if (result.equals("SUCCESS")) {
            System.out.println("\n✅ Product added successfully!");
        } else {
            System.out.println("\n❌ Failed to add product: " + result);
        }

        pauseScreen();
    }

    private void updateProductPrice() {
        System.out.print("\nEnter Product ID: ");
        int productId = getIntInput();

        System.out.print("Enter New Price: $");
        double newPrice = getDoubleInput();

        String result = adminService.updateProductPrice(productId, newPrice);

        if (result.equals("SUCCESS")) {
            System.out.println("✅ Product price updated successfully!");
        } else {
            System.out.println("❌ Failed to update price: " + result);
        }

        pauseScreen();
    }

    private void updateProductName() {
        System.out.print("\nEnter Product ID: ");
        int productId = getIntInput();

        System.out.print("Enter New Name: ");
        String newName = scanner.nextLine();

        String result = adminService.updateProductName(productId, newName);

        if (result.equals("SUCCESS")) {
            System.out.println("✅ Product name updated successfully!");
        } else {
            System.out.println("❌ Failed to update name: " + result);
        }

        pauseScreen();
    }

    private void setProductDiscount() {
        System.out.print("\nEnter Product ID: ");
        int productId = getIntInput();

        System.out.print("Enable Discount? (true/false): ");
        boolean isDiscounted = scanner.nextLine().equalsIgnoreCase("true");

        System.out.print("Discount Percentage: ");
        double discountPercentage = getDoubleInput();

        String result = adminService.setProductDiscount(productId, isDiscounted, discountPercentage);

        if (result.equals("SUCCESS")) {
            System.out.println("✅ Discount updated successfully!");
        } else {
            System.out.println("❌ Failed to update discount: " + result);
        }

        pauseScreen();
    }

    private void addStock() {
        System.out.print("\nEnter Product ID: ");
        int productId = getIntInput();

        System.out.print("Quantity to Add: ");
        int quantity = getIntInput();

        String result = adminService.addStockToExistingProduct(productId, quantity);

        if (result.equals("SUCCESS")) {
            System.out.println("✅ Stock added successfully!");
        } else {
            System.out.println("❌ Failed to add stock: " + result);
        }

        pauseScreen();
    }

    private void removeStock() {
        System.out.print("\nEnter Product ID: ");
        int productId = getIntInput();

        System.out.print("Quantity to Remove: ");
        int quantity = getIntInput();

        String result = adminService.removeStock(productId, quantity);

        if (result.equals("SUCCESS")) {
            System.out.println("✅ Stock removed successfully!");
        } else {
            System.out.println("❌ Failed to remove stock: " + result);
        }

        pauseScreen();
    }

    private void deleteProduct() {
        System.out.print("\nEnter Product ID to delete: ");
        int productId = getIntInput();

        System.out.print("⚠️  Are you sure? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            String result = adminService.deleteProduct(productId);

            if (result.equals("SUCCESS")) {
                System.out.println("✅ Product deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete product: " + result);
            }
        } else {
            System.out.println("❌ Deletion cancelled.");
        }

        pauseScreen();
    }

    private void viewAllProductsAdmin() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ALL PRODUCTS (ADMIN VIEW)                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        Product[] products = adminService.getAllProducts();

        if (products == null || products.length == 0) {
            System.out.println("No products available.");
        } else {
            displayProductTable(products);
        }

        pauseScreen();
    }

    // ==================== ADMIN - USER MANAGEMENT ====================

    private void userManagementMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│       USER MANAGEMENT               │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. Add New User                    │");
        System.out.println("│  2. Update User                     │");
        System.out.println("│  3. Delete User                     │");
        System.out.println("│  4. Back to Admin Menu              │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                addNewUser();
                break;
            case 2:
                updateUser();
                break;
            case 3:
                deleteUser();
                break;
            case 4:
                return; // Back to admin menu
            default:
                System.out.println("❌ Invalid choice. Please try again.");
        }
    }

    private void addNewUser() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      ADD NEW USER                             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Is Admin? (true/false): ");
        boolean isAdmin = scanner.nextLine().equalsIgnoreCase("true");

        String result = adminService.addUser(username, password, isAdmin);

        if (result.equals("SUCCESS")) {
            System.out.println("\n✅ User added successfully!");
        } else {
            System.out.println("\n❌ Failed to add user: " + result);
        }

        pauseScreen();
    }

    private void updateUser() {
        System.out.print("\nEnter User ID: ");
        int userId = getIntInput();

        System.out.print("New Username: ");
        String newUsername = scanner.nextLine();

        System.out.print("New Password: ");
        String newPassword = scanner.nextLine();

        System.out.print("Is Admin? (true/false): ");
        boolean isAdmin = scanner.nextLine().equalsIgnoreCase("true");

        String result = adminService.updateUser(userId, newUsername, newPassword, isAdmin);

        if (result.equals("SUCCESS")) {
            System.out.println("✅ User updated successfully!");
        } else {
            System.out.println("❌ Failed to update user: " + result);
        }

        pauseScreen();
    }

    private void deleteUser() {
        System.out.print("\nEnter User ID to delete: ");
        int userId = getIntInput();

        System.out.print("⚠️  Are you sure? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            String result = adminService.deleteUser(userId);

            if (result.equals("SUCCESS")) {
                System.out.println("✅ User deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete user: " + result);
            }
        } else {
            System.out.println("❌ Deletion cancelled.");
        }

        pauseScreen();
    }

    private void viewAllUsers() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      ALL USERS                                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        User[] users = adminService.getAllUsers();

        if (users == null || users.length == 0) {
            System.out.println("No users found.");
        } else {
            System.out.println("┌──────┬─────────────────────┬───────────┬──────────────┐");
            System.out.println("│  ID  │      Username       │  Is Admin │ Created Date │");
            System.out.println("├──────┼─────────────────────┼───────────┼──────────────┤");

            for (User user : users) {
                if (user != null) {
                    System.out.printf("│ %-4d │ %-19s │ %-9s │ %-12s │%n",
                            user.getUserId(),
                            truncateString(user.getUsername(), 19),
                            user.isAdmin() ? "Yes" : "No",
                            user.getCreatedDate());
                }
            }

            System.out.println("└──────┴─────────────────────┴───────────┴──────────────┘");
        }

        pauseScreen();
    }

    // ==================== HELPER METHODS ====================

    private void displayProductTable(Product[] products) {
        System.out.println("┌──────┬─────────────────────────┬────────────┬──────────┬───────────┐");
        System.out.println("│  ID  │          Name           │  Category  │  Price   │   Stock   │");
        System.out.println("├──────┼─────────────────────────┼────────────┼──────────┼───────────┤");

        for (Product product : products) {
            if (product != null) {
                System.out.printf("│ %-4d │ %-23s │ %-10s │ $%-7.2f │ %-9d │%n",
                        product.getProductId(),
                        truncateString(product.getName(), 23),
                        truncateString(product.getCategory(), 10),
                        product.getPrice(),
                        product.getStockQuantity());
            }
        }

        System.out.println("└──────┴─────────────────────────┴────────────┴──────────┴───────────┘");
    }

    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    private int getIntInput() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (Exception e) {
                System.out.print("❌ Invalid input. Please enter a number: ");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private double getDoubleInput() {
        while (true) {
            try {
                double value = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (Exception e) {
                System.out.print("❌ Invalid input. Please enter a number: ");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void pauseScreen() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}

