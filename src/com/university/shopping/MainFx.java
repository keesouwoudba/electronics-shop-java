package com.university.shopping;

import com.university.shopping.app.AppState;
import com.university.shopping.model.Product;
import com.university.shopping.repository.CartRepository;
import com.university.shopping.repository.CsvBootstrapInitializer;
import com.university.shopping.repository.OrderRepository;
import com.university.shopping.repository.ProductRepository;
import com.university.shopping.repository.UserRepository;
import com.university.shopping.service.AdminService;
import com.university.shopping.service.AuthService;
import com.university.shopping.service.ShopService;
import com.university.shopping.service.pricing.DiscountPolicy;
import com.university.shopping.service.pricing.StandardDiscountPolicy;
import com.university.shopping.service.report.AbstractReportService;
import com.university.shopping.service.report.ConsoleReportService;
import com.university.shopping.service.report.CsvReportService;
import com.university.shopping.app.Router;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.layout.AppShell;
import com.university.shopping.view.renderer.UiRenderer;
import com.university.shopping.service.media.ProductImageService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main JavaFX application entry point.
 * Initializes all services, builds the AppShell, and launches the SPA router system.
 */
public class MainFx extends Application {
    private AppState appState;
    private AuthService authService;
    private ShopService shopService;
    private AdminService adminService;
    private ProductImageService productImageService;
    private Router router;
    private UiRenderer renderer;

    @Override
    public void start(Stage stage) {
        initializeServices();
        buildAndShowSPA(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initializeServices() {
        // Bootstrap data from CSV files
        CsvBootstrapInitializer bootstrapInitializer = new CsvBootstrapInitializer();

        this.appState = new AppState();
        this.productImageService = new ProductImageService();

        // Initialize repositories
        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();
        OrderRepository orderRepository = new OrderRepository();
        CartRepository cartRepository = new CartRepository();

        // Initialize core services
        this.authService = new AuthService(userRepository, appState);
        DiscountPolicy discountPolicy = new StandardDiscountPolicy();
        this.shopService = new ShopService(productRepository, orderRepository, cartRepository, authService, discountPolicy);

        // Initialize reporting services
        String[] reportFormats = new String[] {"console", "csv"};
        AbstractReportService[] reportServices = new AbstractReportService[] {
            new ConsoleReportService(productRepository, userRepository, orderRepository),
            new CsvReportService(productRepository, userRepository, orderRepository,
                "src/com/university/shopping/service/report/system.csv")
        };
        this.adminService = new AdminService(productRepository, userRepository, orderRepository, authService,
            reportFormats, reportServices);
        
        // Initialize router and renderer
        this.router = new Router(appState);
        this.renderer = null; // Will be created after AppShell is built
    }

    private void buildAndShowSPA(Stage stage) {
        // Create screen context with all services
        AppShell shell = new AppShell();

        // Create screen context that will be passed to all pages
        ScreenContext context = new ScreenContext(appState, router, null, authService, shopService, adminService, productImageService);
        
        // Create renderer with shell and context
        this.renderer = new UiRenderer(context, shell);
        context.setRenderer(renderer);
        
        // Set up the primary stage
        Scene scene = new Scene(shell.buildRoot(context), 1100, 760);
        stage.setTitle("TechVolt Electronics - JavaFX");
        stage.setScene(scene);
        
        // Initialize the SPA router and render the first page
        renderer.initialize(scene);
        
        stage.show();
    }
}
