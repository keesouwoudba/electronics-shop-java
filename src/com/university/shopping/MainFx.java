package com.university.shopping;

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
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainFx extends Application {
    private AuthService authService;
    private ShopService shopService;
    private AdminService adminService;

    @Override
    public void start(Stage stage) {
        initializeServices();

        BorderPane shell = new BorderPane();
        shell.setTop(buildHeader());
        shell.setCenter(buildScrollablePageFlow());

        Scene scene = new Scene(shell, 1100, 760);
        stage.setTitle("Electronics Shop - JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initializeServices() {
        CsvBootstrapInitializer bootstrapInitializer = new CsvBootstrapInitializer();

        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();
        OrderRepository orderRepository = new OrderRepository();
        CartRepository cartRepository = new CartRepository();

        this.authService = new AuthService(userRepository);
        DiscountPolicy discountPolicy = new StandardDiscountPolicy();
        this.shopService = new ShopService(productRepository, orderRepository, cartRepository, authService, discountPolicy);

        String[] reportFormats = new String[] {"console", "csv"};
        AbstractReportService[] reportServices = new AbstractReportService[] {
            new ConsoleReportService(productRepository, userRepository, orderRepository),
            new CsvReportService(productRepository, userRepository, orderRepository,
                "src/com/university/shopping/service/report/system.csv")
        };
        this.adminService = new AdminService(productRepository, userRepository, orderRepository, authService,
            reportFormats, reportServices);
    }

    private HBox buildHeader() {
        Label brand = new Label("Electronics Shop");
        brand.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label mode = new Label("JavaFX bootstrap mode");
        mode.setStyle("-fx-font-size: 13px; -fx-opacity: 0.8;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(12, brand, spacer, mode);
        header.setPadding(new Insets(14, 20, 14, 20));
        header.setStyle("-fx-background-color: linear-gradient(to right, #16324f, #2d5f89); -fx-text-fill: white;");
        for (javafx.scene.Node child : header.getChildren()) {
            if (child instanceof Label) {
                ((Label) child).setStyle(((Label) child).getStyle() + " -fx-text-fill: white;");
            }
        }
        return header;
    }

    private ScrollPane buildScrollablePageFlow() {
        Product[] products = shopService.getAllProducts();
        int productCount = products == null ? 0 : products.length;

        Label title = new Label("JavaFX is wired successfully");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label body = new Label(
            "This screen confirms your existing repositories and services can run inside a JavaFX app.\n" +
            "Next step is replacing this placeholder with Router + UiRenderer + page components."
        );
        body.setWrapText(true);
        body.setStyle("-fx-font-size: 14px;");

        Label stats = new Label("Loaded products from CSV: " + productCount);
        stats.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        VBox content = new VBox(12, title, body, stats);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        VBox footer = new VBox();
        footer.setPadding(new Insets(20));
        footer.setStyle("-fx-background-color: #f2f5f9;");
        footer.getChildren().add(new Label("Footer is injected at content bottom and reached by scroll on long pages."));

        Region longSpacer = new Region();
        longSpacer.setMinHeight(520);

        VBox pageFlow = new VBox(16, content, longSpacer, footer);
        pageFlow.setPadding(new Insets(20));
        pageFlow.setStyle("-fx-background-color: #e9eef5;");

        ScrollPane scrollPane = new ScrollPane(pageFlow);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }
}
