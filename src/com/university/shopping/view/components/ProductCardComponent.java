package com.university.shopping.view.components;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.model.Product;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.util.ImageHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductCardComponent {
    private final Product product;

    public ProductCardComponent(Product product) {
        this.product = product;
    }

    public Node render(ScreenContext context) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(10));
        card.setPrefWidth(220);
        card.setStyle("-fx-border-color: #c1c6d6; -fx-border-radius: 8px; -fx-background-color: white; -fx-background-radius: 8px;");

        Node imageNode = ImageHelper.createProductImageView(product, 180, 120);
        HBox imageBox = new HBox(imageNode);
        imageBox.setAlignment(Pos.CENTER);

        Label category = new Label(product.getCategory());
        category.setStyle("-fx-font-size: 10px; -fx-text-fill: grey;");

        Label name = new Label(product.getName());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        name.setWrapText(true);

        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_LEFT);

        if (context.getAppState().getCurrentUser() == null) {
            Label price = new Label("Login to view");
            price.setStyle("-fx-text-fill: #005bbf;");

            Button detailsBtn = new Button("Login");
            detailsBtn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white;");
            detailsBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.LOGIN)));

            actions.getChildren().addAll(price, detailsBtn);
        } else if (context.getAppState().getCurrentUser().isAdmin()) {
            Label price = new Label(String.format("$%.2f", product.getPrice()));
            price.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #005bbf;");

            Button editBtn = new Button("Edit");
            editBtn.setOnAction(e -> {
                context.getAppState().setSelectedProductId(product.getProductId());
                context.getRenderer().navigate(NavIntent.open(Route.ADMIN_PRODUCT_EDITOR));
            });

            actions.getChildren().addAll(price, editBtn);
        } else {
            Label price = new Label();
            if (product.isDiscounted()) {
                price.setText(String.format("$%.2f", product.getFinalPrice()));
                price.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #005bbf;");
                Label oldPrice = new Label(String.format("$%.2f", product.getPrice()));
                oldPrice.setStyle("-fx-strikethrough: true; -fx-text-fill: grey; -fx-font-size: 11px;");
                actions.getChildren().add(oldPrice);
            } else {
                price.setText(String.format("$%.2f", product.getPrice()));
                price.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            }

            Label stock = new Label(product.getStockQuantity() > 0
                    ? "In Stock (" + product.getStockQuantity() + ")"
                    : "Out of Stock");
            stock.setStyle("-fx-font-size: 11px; -fx-text-fill: " + (product.getStockQuantity() > 0 ? "#00c853" : "#ba1a1a") + ";");

            Button detailsBtn = new Button("View Details");
            detailsBtn.setStyle("-fx-background-color: white; -fx-border-color: #005bbf; -fx-text-fill: #005bbf;");
            detailsBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.openProduct(product.getProductId())));

            Button addCartBtn = new Button("Add to Cart");
            addCartBtn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white;");
            addCartBtn.setDisable(product.getStockQuantity() == 0);
            addCartBtn.setOnAction(e -> {
                int userId = context.getAppState().getCurrentUser().getUserId();
                boolean success = context.getShopService().addToCart(product.getProductId(), 1, userId);
                if (success) {
                    context.getRenderer().setNotification("Added " + product.getName() + " to cart.", false);
                    context.getRenderer().navigate(NavIntent.open(Route.CUSTOMER_CART));
                } else {
                    context.getRenderer().setNotification("Failed to add to cart. Insufficient stock.", true);
                }
            });

            actions.getChildren().addAll(price, stock, detailsBtn, addCartBtn);
        }

        card.getChildren().addAll(imageBox, category, name, actions);
        return card;
    }
}