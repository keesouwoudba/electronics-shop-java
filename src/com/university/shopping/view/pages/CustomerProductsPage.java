package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.model.Product;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import com.university.shopping.view.util.ImageHelper;
import javafx.scene.Node;

public class CustomerProductsPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        VBox headerBox = new VBox(5);
        Label title = new Label("Product Catalog");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label subtitle = new Label("Explore our high-fidelity electronics and components.");
        headerBox.getChildren().addAll(title, subtitle);

        FlowPane grid = new FlowPane();
        grid.setHgap(15);
        grid.setVgap(15);

        Product[] products = context.getShopService().getAllProducts();
        if (products == null || products.length == 0) {
            grid.getChildren().add(new Label("No products available."));
        } else {
            for (Product p : products) {
                if (p != null) {
                    VBox card = new VBox(10);
                    card.setStyle("-fx-border-color: #c1c6d6; -fx-border-radius: 8px; -fx-padding: 15px; -fx-background-color: white;");
                    card.setPrefWidth(220);
                    
                    Node img = ImageHelper.createProductImageView(p, 200, 120);

                    Label cat = new Label(p.getCategory());
                    cat.setStyle("-fx-font-size: 11px; -fx-text-fill: grey; -fx-padding: 3px 6px; -fx-background-color: #f2f3fd; -fx-background-radius: 4px;");
                    
                    Label name = new Label(p.getName());
                    name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    name.setWrapText(true);

                    Label price = new Label(String.format("$%.2f", p.getPrice()));
                    price.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                    Label stock = new Label("In Stock (" + p.getStockQuantity() + ")");
                    stock.setStyle("-fx-text-fill: " + (p.getStockQuantity() > 0 ? "#00c853" : "red") + "; -fx-font-size: 11px;");

                    Button detailsBtn = new Button("View Details");
                    detailsBtn.setStyle("-fx-background-color: white; -fx-border-color: #005bbf; -fx-text-fill: #005bbf; -fx-border-radius: 4px;");
                    detailsBtn.setMaxWidth(Double.MAX_VALUE);
                    detailsBtn.setOnAction(e -> context.getRouter().dispatch(NavIntent.openProduct(p.getProductId())));

                    Button addCartBtn = new Button("Add to Cart");
                    addCartBtn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-background-radius: 4px;");
                    addCartBtn.setMaxWidth(Double.MAX_VALUE);
                    addCartBtn.setDisable(p.getStockQuantity() == 0);
                    addCartBtn.setOnAction(e -> {
                        int userId = context.getAppState().getCurrentUser().getUserId();
                        boolean success = context.getShopService().addToCart(p.getProductId(), 1, userId);
                        if (success) {
                            context.getRenderer().setNotification("Added " + p.getName() + " to cart.", false);
                        } else {
                            context.getRenderer().setNotification("Failed to add to cart. Insufficient stock.", true);
                        }
                    });

                    card.getChildren().addAll(img, cat, name, price, stock, detailsBtn, addCartBtn);
                    grid.getChildren().add(card);
                }
            }
        }

        root.getChildren().addAll(headerBox, grid);
        return root;
    }
}
