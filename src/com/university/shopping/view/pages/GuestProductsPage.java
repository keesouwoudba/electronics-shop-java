package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.model.Product;
import com.university.shopping.view.components.ProductCardComponent;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.util.StyleHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GuestProductsPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox();
        StyleHelper.applyPageBackground(root);

        VBox content = StyleHelper.createPageContainer();
        content.setPadding(new Insets(20));

        // Guest Banner
        HBox banner = new HBox(15);
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setStyle("-fx-background-color: #f2f3fd; -fx-border-color: #adc7ff; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        banner.setPadding(new Insets(15));
        Label infoTxt = new Label("Please login or register to add items to cart and view pricing details.");
        infoTxt.setWrapText(true);
        infoTxt.setStyle("-fx-font-size: 13px; -fx-text-fill: #16324f;");
        
        Button loginBtn = StyleHelper.createPrimaryButton("Login");
        loginBtn.setPrefWidth(110);
        loginBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.LOGIN)));
        
        Button regBtn = StyleHelper.createSecondaryButton("Register");
        regBtn.setPrefWidth(110);
        regBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.REGISTER)));
        
        banner.getChildren().addAll(infoTxt, loginBtn, regBtn);

        // Header
        VBox headerBox = new VBox(5);
        Label title = new Label("Product Catalog");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label subtitle = new Label("Browse our extensive collection of premium electronics.");
        subtitle.setStyle("-fx-text-fill: grey;");
        headerBox.getChildren().addAll(title, subtitle);

        // Products Grid
        FlowPane grid = new FlowPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPrefWrapLength(1000);
        grid.setAlignment(Pos.TOP_LEFT);

        Product[] products = context.getShopService().getAllProducts();
        if (products == null || products.length == 0) {
            grid.getChildren().add(new Label("No products available."));
        } else {
            for (Product p : products) {
                if (p != null) {
                    grid.getChildren().add(new ProductCardComponent(p).render(context));
                }
            }
        }

        content.getChildren().addAll(banner, headerBox, grid);
        root.getChildren().add(content);
        return root;
    }
}
