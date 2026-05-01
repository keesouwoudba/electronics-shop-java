package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.model.Product;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import com.university.shopping.view.util.ImageHelper;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class GuestProductsPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        // Guest Banner
        HBox banner = new HBox(15);
        banner.setStyle("-fx-background-color: #f2f3fd; -fx-border-color: #adc7ff; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        banner.setPadding(new Insets(15));
        Label infoTxt = new Label("Please login or register to add items to cart and view pricing details.");
        
        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(e -> context.getRouter().dispatch(NavIntent.open(Route.AUTH_LOGIN)));
        
        Button regBtn = new Button("Register");
        regBtn.setOnAction(e -> context.getRouter().dispatch(NavIntent.open(Route.AUTH_REGISTER)));
        
        banner.getChildren().addAll(infoTxt, loginBtn, regBtn);

        // Header
        VBox headerBox = new VBox(5);
        Label title = new Label("Product Catalog");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label subtitle = new Label("Browse our extensive collection of premium electronics.");
        headerBox.getChildren().addAll(title, subtitle);

        // Products Grid
        FlowPane grid = new FlowPane();
        grid.setHgap(15);
        grid.setVgap(15);

        Product[] products = context.getShopService().getAllProducts();
        if (products == null || products.length == 0) {
            grid.getChildren().add(new Label("No products available."));
        } else {
            for (Product p : products) {
                if (p != null) {
                    VBox card = new VBox(5);
                    card.setStyle("-fx-border-color: #c1c6d6; -fx-border-radius: 8px; -fx-padding: 10px;");
                    card.setPrefWidth(200);
                    
                    Node img = ImageHelper.createProductImageView(p, 180, 120);

                    Label cat = new Label(p.getCategory());
                    cat.setStyle("-fx-font-size: 10px; -fx-text-fill: grey;");
                    Label name = new Label(p.getName());
                    name.setStyle("-fx-font-weight: bold;");
                    
                    // Specific to guest catalog format
                    Label mockPrice = new Label("Login to view");
                    mockPrice.setStyle("-fx-text-fill: #005bbf;");
                    
                    card.getChildren().addAll(img, cat, name, mockPrice);
                    grid.getChildren().add(card);
                }
            }
        }

        root.getChildren().addAll(banner, headerBox, grid);
        return root;
    }
}
