package com.university.shopping;

import com.university.shopping.repository.*;
import com.university.shopping.service.*;
import com.university.shopping.service.pricing.DiscountPolicy;
import com.university.shopping.service.pricing.StandardDiscountPolicy;
import com.university.shopping.service.report.AbstractReportService;
import com.university.shopping.service.report.ConsoleReportService;
import com.university.shopping.service.report.CsvReportService;
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
        DiscountPolicy discountPolicy = new StandardDiscountPolicy();
        ShopService shopService = new ShopService(productRepository, orderRepository, cartRepository, authService, discountPolicy);
        String[] reportFormats = new String[] {"console", "csv"};
        AbstractReportService[] reportServices = new AbstractReportService[] {
            new ConsoleReportService(productRepository, userRepository, orderRepository),
            new CsvReportService(productRepository, userRepository, orderRepository,
                "src/com/university/shopping/service/report/system.csv")
        };
        AdminService adminService = new AdminService(productRepository, userRepository, orderRepository, authService,
            reportFormats, reportServices);

        // Initialize and start the UI
        ConsoleUI ui = new ConsoleUI(authService, shopService, adminService);
        ui.start();
    }
}
