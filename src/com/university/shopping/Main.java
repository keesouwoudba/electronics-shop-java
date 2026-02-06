package com.university.shopping;

import com.university.shopping.repository.*;
import com.university.shopping.service.*;
import com.university.shopping.view.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        // Initialize repositories
        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();
        OrderRepository orderRepository = new OrderRepository();
        CartRepository cartRepository = new CartRepository();

        // Initialize services with dependency injection
        AuthService authService = new AuthService(userRepository);
        ShopService shopService = new ShopService(productRepository, orderRepository, cartRepository, authService);
        AdminService adminService = new AdminService(productRepository, userRepository, orderRepository, authService);

        // Initialize and start the UI
        ConsoleUI ui = new ConsoleUI(authService, shopService, adminService);
        ui.start();
    }
}
