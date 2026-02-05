package com.university.shopping;

import com.university.shopping.model.*;
import com.university.shopping.repository.*;
import com.university.shopping.service.*;

public class Main {
    private static UserRepository userRepository;
    private static ProductRepository productRepository;
    private static OrderRepository orderRepository;
    private static CartRepository cartRepository;
    private static AuthService authService;
    private static ShopService shopService;
    private static AdminService adminService;

    public static void main(String[] args) {
        System.out.println("========== ELECTRONICS SHOPPING SYSTEM - TEST SUITE ==========\n");

        // Initialize repositories and services
        initializeSystem();

        // Run all tests
        testAuthentication();
        testProductRepository();
        testCartOperations();
        testShoppingFeatures();
        testOrderOperations();
        testUserRepository();

        System.out.println("\n========== TEST SUITE COMPLETED ==========");
    }

    private static void initializeSystem() {
        System.out.println("[SETUP] Initializing system...");
        userRepository = new UserRepository();
        productRepository = new ProductRepository();
        orderRepository = new OrderRepository();
        cartRepository = new CartRepository();
        authService = new AuthService(userRepository);
        shopService = new ShopService(productRepository, orderRepository, cartRepository, authService);
        adminService = new AdminService(productRepository, userRepository, orderRepository, authService);
        System.out.println("[SETUP] System initialized successfully!\n");
    }

    private static void testAuthentication() {
        System.out.println("========== TEST 1: AUTHENTICATION ==========");

        // Test Login
        System.out.println("\n[TEST] Admin Login");
        String loginResult = authService.login("admin", "admin123");
        System.out.println("Result: " + loginResult);
        System.out.println("Current User: " + (authService.getCurrentUser() != null ? authService.getCurrentUser().getUsername() : "None"));
        System.out.println("Is Admin: " + authService.isAdmin());

        // Test Logout
        System.out.println("\n[TEST] Logout");
        authService.logout();
        System.out.println("Is Logged In: " + authService.isLoggedIn());

        // Test Customer Login
        System.out.println("\n[TEST] Customer Login");
        loginResult = authService.login("customer", "pass123");
        System.out.println("Result: " + loginResult);
        System.out.println("Is Admin: " + authService.isAdmin());

        // Test Wrong Password
        System.out.println("\n[TEST] Login with Wrong Password");
        authService.logout();
        loginResult = authService.login("customer", "wrongpass");
        System.out.println("Result: " + loginResult);

        // Test Non-existent User
        System.out.println("\n[TEST] Login with Non-existent User");
        loginResult = authService.login("nonexistent", "pass");
        System.out.println("Result: " + loginResult);

        // Test Register
        System.out.println("\n[TEST] Register New User");
        String registerResult = authService.register("newuser", "SecurePass123");
        System.out.println("Result: " + registerResult);
        System.out.println("New User Count: " + userRepository.getUserCount());

        authService.logout();
    }

    private static void testProductRepository() {
        System.out.println("\n========== TEST 2: PRODUCT REPOSITORY ==========");

        // Test Find All Products
        System.out.println("\n[TEST] Find All Products");
        Product[] allProducts = productRepository.findAll();
        System.out.println("Total Products: " + productRepository.getProductCount());
        System.out.println("First 3 Products:");
        for (int i = 0; i < 3 && i < productRepository.getProductCount(); i++) {
            System.out.println("  - " + allProducts[i].getName() + " (ID: " + allProducts[i].getProductId() + ", Price: $" + allProducts[i].getPrice() + ")");
        }

        // Test Find by ID
        System.out.println("\n[TEST] Find Product by ID");
        Product product = productRepository.findById(1);
        if (product != null) {
            System.out.println("Found: " + product.getName() + " - $" + product.getPrice());
        }

        // Test Find by Name
        System.out.println("\n[TEST] Find Product by Name");
        product = productRepository.findByName("iPhone 15 Pro");
        if (product != null) {
            System.out.println("Found: " + product.getName() + " - Stock: " + product.getStockQuantity());
        }

        // Test Find by Category
        System.out.println("\n[TEST] Find Products by Category");
        Product[] phoneProducts = productRepository.findByCategory("Phone");
        System.out.println("Phone Products: " + phoneProducts.length);
        for (Product p : phoneProducts) {
            if (p != null) System.out.println("  - " + p.getName());
        }

        // Test Update Stock
        System.out.println("\n[TEST] Update Stock for Product");
        int productId = productRepository.findById(1).getProductId();
        boolean updateResult = productRepository.updateStock(productId, 50);
        System.out.println("Update Result: " + updateResult);
        System.out.println("New Stock: " + productRepository.findById(1).getStockQuantity());

        // Test Update Product
        System.out.println("\n[TEST] Update Product Details");
        Product updateProduct = productRepository.findById(1);
        updateProduct.setPrice(1299.99);
        boolean result = productRepository.update(updateProduct);
        System.out.println("Update Result: " + result);
        System.out.println("New Price: $" + productRepository.findById(1).getPrice());

        // Test Save New Product
        System.out.println("\n[TEST] Save New Product");
        Product newProduct = new Product("Test Laptop", 999.99, "Laptop", "Test Description", 10, false, 0);
        result = productRepository.save(newProduct);
        System.out.println("Save Result: " + result);
        System.out.println("New Product Count: " + productRepository.getProductCount());
    }

    private static void testCartOperations() {
        System.out.println("\n========== TEST 3: CART OPERATIONS ==========");

        // Login as customer
        authService.login("customer", "pass123");
        int userId = authService.getCurrentUser().getUserId();

        System.out.println("\n[TEST] Find/Create Cart for User " + userId);
        Cart cart = cartRepository.findCartByUserId(userId);
        System.out.println("Cart created/found for User ID: " + cart.getUserId());
        System.out.println("Initial Item Count: " + cart.getItemCount());

        // Add items to cart
        System.out.println("\n[TEST] Add Items to Cart");
        cart.addItem(1, "iPhone 15 Pro", 1, 1199.99);
        System.out.println("Item added. Item Count: " + cart.getItemCount());

        cart.addItem(2, "Samsung Galaxy S24", 2, 999.99);
        System.out.println("Item added. Item Count: " + cart.getItemCount());

        // Add same item again (should increase quantity)
        System.out.println("\n[TEST] Add Duplicate Item (should increase quantity)");
        cart.addItem(1, "iPhone 15 Pro", 1, 1199.99);
        System.out.println("Item quantity updated. Item Count: " + cart.getItemCount());
        System.out.println("iPhone quantity: " + cart.getItems()[0].getQuantity());

        // Calculate total
        System.out.println("\n[TEST] Calculate Cart Total");
        double total = cart.getTotalPrice();
        System.out.println("Total Price: $" + total);

        // Save Cart
        System.out.println("\n[TEST] Save Cart");
        boolean saveResult = cartRepository.saveCart(cart);
        System.out.println("Save Result: " + saveResult);

        // Remove Item
        System.out.println("\n[TEST] Remove Item from Cart");
        boolean removeResult = cart.removeItem(2);
        System.out.println("Remove Result: " + removeResult);
        System.out.println("Item Count after removal: " + cart.getItemCount());

        // Clear Cart
        System.out.println("\n[TEST] Clear Cart");
        cart.clear();
        System.out.println("Item Count after clear: " + cart.getItemCount());

        authService.logout();
    }

    private static void testShoppingFeatures() {
        System.out.println("\n========== TEST 4: SHOPPING FEATURES ==========");

        // Login as customer
        authService.login("customer", "pass123");
        int userId = authService.getCurrentUser().getUserId();

        // Get all products
        System.out.println("\n[TEST] Get All Products");
        Product[] products = shopService.getAllProducts();
        System.out.println("Total Products Available: " + productRepository.getProductCount());

        // Get specific product
        System.out.println("\n[TEST] Get Product by ID");
        Product product = shopService.getProductById(1);
        if (product != null) {
            System.out.println("Product: " + product.getName() + " - $" + product.getPrice());
        }

        // Add to Cart
        System.out.println("\n[TEST] Add Products to Cart");
        boolean addResult = shopService.addToCart(1, 1, userId);
        System.out.println("Add Product 1 Result: " + addResult);

        addResult = shopService.addToCart(2, 3, userId);
        System.out.println("Add Product 2 Result: " + addResult);

        // View Cart
        System.out.println("\n[TEST] View Cart");
        Cart cart = shopService.viewCart(userId);
        if (cart != null) {
            System.out.println("Items in cart: " + cart.getItemCount());
            System.out.println("Total: $" + cart.getTotalPrice());
        }

        // Remove from Cart
        System.out.println("\n[TEST] Remove from Cart");
        boolean removeResult = shopService.removeFromCart(2, userId);
        System.out.println("Remove Result: " + removeResult);
        System.out.println("Items after removal: " + shopService.viewCart(userId).getItemCount());

        // Checkout
        System.out.println("\n[TEST] Checkout");
        shopService.addToCart(1, 2, userId);
        String checkoutResult = shopService.checkout(userId);
        System.out.println("Checkout Result: " + checkoutResult);
        System.out.println("Cart after checkout: " + shopService.viewCart(userId).getItemCount() + " items");

        authService.logout();
    }

    private static void testOrderOperations() {
        System.out.println("\n========== TEST 5: ORDER OPERATIONS ==========");

        System.out.println("\n[TEST] Find All Orders");
        Order[] allOrders = orderRepository.findAll();
        System.out.println("Total Orders: " + orderRepository.getOrderCount());

        System.out.println("\n[TEST] Find Order by ID");
        if (orderRepository.getOrderCount() > 0) {
            Order order = orderRepository.findOrderById(1);
            if (order != null) {
                System.out.println("Order ID: " + order.getOrderId() + " - Total: $" + order.getTotalPrice() + " - Status: " + order.getStatus());
            }
        }

        System.out.println("\n[TEST] Find Orders by User ID");
        Order[] userOrders = orderRepository.findAllByUserId(2);
        if (userOrders != null && userOrders.length > 0) {
            System.out.println("User 2 Orders: " + userOrders.length);
            for (Order order : userOrders) {
                if (order != null) {
                    System.out.println("  - Order " + order.getOrderId() + ": $" + order.getTotalPrice());
                }
            }
        } else {
            System.out.println("No orders found for User 2");
        }

        System.out.println("\n[TEST] Save Order");
        OrderItem[] items = new OrderItem[1];
        items[0] = new OrderItem(1, "iPhone 15 Pro", 1, 1199.99);
        Order newOrder = new Order(1, "2026-02-05", 1199.99, "PENDING", items);
        boolean saveResult = orderRepository.save(newOrder);
        System.out.println("Save Result: " + saveResult);
        System.out.println("New Order Count: " + orderRepository.getOrderCount());

        System.out.println("\n[TEST] Update Order Status");
        Order order = orderRepository.findOrderById(newOrder.getOrderId());
        if (order != null) {
            order.setStatus("SHIPPED");
            System.out.println("Order Status Updated: " + order.getStatus());
        }
    }

    private static void testUserRepository() {
        System.out.println("\n========== TEST 6: USER REPOSITORY ==========");

        System.out.println("\n[TEST] Get All Users");
        System.out.println("Total Users: " + userRepository.getUserCount());

        System.out.println("\n[TEST] Find User by ID");
        User user = userRepository.findById(1);
        if (user != null) {
            System.out.println("User: " + user.getUsername() + " (ID: " + user.getUserId() + ", Admin: " + user.isAdmin() + ")");
        }

        System.out.println("\n[TEST] Find User by Username");
        user = userRepository.findByUsername("admin");
        if (user != null) {
            System.out.println("Found: " + user.getUsername() + " - Created: " + user.getCreatedDate());
        }

        System.out.println("\n[TEST] Update User");
        user = userRepository.findByUsername("customer");
        if (user != null) {
            String oldPassword = user.getPassword();
            user.setPassword("newpass456");
            boolean updateResult = userRepository.update(user);
            System.out.println("Update Result: " + updateResult);
            System.out.println("Password Changed: " + oldPassword + " -> " + user.getPassword());
        }

        System.out.println("\n[TEST] Save New User");
        User newUser = new User("testuser", "TestPass123", false, "2026-02-05");
        boolean saveResult = userRepository.save(newUser);
        System.out.println("Save Result: " + saveResult);
        System.out.println("New User Count: " + userRepository.getUserCount());

        System.out.println("\n[TEST] Verify System State");
        System.out.println("Total Users: " + userRepository.getUserCount());
        System.out.println("Total Products: " + productRepository.getProductCount());
        System.out.println("Total Orders: " + orderRepository.getOrderCount());
        System.out.println("Total Carts: " + MockDatabase.cartCount);
    }
}
