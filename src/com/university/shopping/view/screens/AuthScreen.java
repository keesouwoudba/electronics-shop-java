package com.university.shopping.view.screens;

import com.university.shopping.model.Product;
import com.university.shopping.service.AuthService;
import com.university.shopping.service.ShopService;

import java.util.Scanner;

public class AuthScreen {
    private final Scanner scanner;
    private final AuthService authService;
    private final ShopService shopService;

    public AuthScreen(Scanner scanner, AuthService authService, ShopService shopService) {
        this.scanner = scanner;
        this.authService = authService;
        this.shopService = shopService;
    }

    public boolean showMainMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│          MAIN MENU                  │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. Login                           │");
        System.out.println("│  2. Register                        │");
        System.out.println("│  3. Browse Products (Guest)         │");
        System.out.println("│  4. Exit                            │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Choose an option: ");

        int choice = readIntSafe();
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
                return false;
            default:
                System.out.println("❌ Invalid choice. Please try again.");
        }

        return true;
    }

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
            if (authService.getCurrentUser() != null) {
                System.out.println("You are now logged in as: " + authService.getCurrentUser().getUsername());
            } else {
                System.out.println("Registration succeeded, but auto-login did not complete.");
            }
        } else {
            System.out.println("\n❌ Registration failed: " + result);
        }

        pauseScreen();
    }

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

    private int readIntSafe() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (Exception e) {
                System.out.print("❌ Invalid input. Please enter a number: ");
                scanner.nextLine();
            }
        }
    }

    private void pauseScreen() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
