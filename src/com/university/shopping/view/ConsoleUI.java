package com.university.shopping.view;

import com.university.shopping.service.*;
import com.university.shopping.view.contracts.MenuActions;
import com.university.shopping.view.screens.AdminScreen;
import com.university.shopping.view.screens.AuthScreen;
import com.university.shopping.view.screens.CustomerScreen;

import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final AuthService authService;
    private final MenuActions customerScreen;
    private final MenuActions adminScreen;
    private final AuthScreen authScreen;

    // Constructor with dependency injection
    public ConsoleUI(AuthService authService, ShopService shopService, AdminService adminService) {
        this.scanner = new Scanner(System.in);
        this.authService = authService;
        this.customerScreen = new CustomerScreen(scanner, authService, shopService, adminService);
        this.adminScreen = new AdminScreen(scanner, authService, shopService, adminService);
        this.authScreen = new AuthScreen(scanner, authService, shopService);
    }

    // Main entry point - starts the application
    public void start() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║   WELCOME TO ELECTRONICS SHOPPING SYSTEM  ║");
        System.out.println("╚═══════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            if (!authService.isLoggedIn()) {
                running = authScreen.showMainMenu();
            } else {
                MenuActions active = authService.isAdmin() ? adminScreen : customerScreen;
                active.showMenu();
                int choice = getIntInput();
                active.handleOption(choice);
            }
        }

        System.out.println("\n✨ Thank you for using Electronics Shopping System! ✨\n");
        scanner.close();
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
}

