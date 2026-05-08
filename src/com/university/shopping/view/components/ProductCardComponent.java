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
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;

public class ProductCardComponent {
    private final Product product;

    public ProductCardComponent(Product product) {
        this.product = product;
    }

    public Node render(ScreenContext context) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(10));
        card.setPrefWidth(220);
        card.setPrefHeight(290);
        card.setMinHeight(290);
        card.getStyleClass().add("product-card");

        Node imageNode = ImageHelper.createProductImageView(product, 180, 150);
        HBox imageBox = new HBox(imageNode);
        imageBox.setAlignment(Pos.CENTER);

        Label category = new Label(product.getCategory());
        category.getStyleClass().add("product-category");

        Label name = new Label(product.getName());
        name.getStyleClass().add("product-name");
        name.setWrapText(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox actions = new VBox(8);
        actions.setAlignment(Pos.CENTER_LEFT);

        if (context.getAppState().getCurrentUser() == null) {
            Label price = new Label("Login to view");
            price.getStyleClass().add("text-primary");

            Button detailsBtn = new Button("Login");
            detailsBtn.getStyleClass().add("primary-btn");
            detailsBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.LOGIN)));

            actions.getChildren().addAll(price, detailsBtn);
        } else if (context.getAppState().getCurrentUser().isAdmin()) {
            Label price = new Label(String.format("$%.2f", product.getPrice()));
            price.getStyleClass().add("product-price");

            Button editBtn = new Button("Edit");
            editBtn.setOnAction(e -> {
                context.getAppState().setSelectedProductId(product.getProductId());
                context.getRenderer().navigate(NavIntent.open(Route.ADMIN_PRODUCT_EDITOR));
            });

            actions.getChildren().addAll(price, editBtn);
        } else {
            Label price = new Label();
            HBox priceRow = new HBox(8);
            priceRow.setAlignment(Pos.CENTER_LEFT);

            if (product.isDiscounted()) {
                price.setText(String.format("$%.2f", product.getFinalPrice()));
                price.getStyleClass().add("product-price");
                price.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);

                Label oldPrice = new Label(String.format("$%.2f", product.getPrice()));
                oldPrice.getStyleClass().add("product-price-old");
                oldPrice.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);

                priceRow.getChildren().addAll(oldPrice, price);
            } else {
                price.setText(String.format("$%.2f", product.getPrice()));
                price.getStyleClass().add("product-price-standard");
                price.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);

                priceRow.getChildren().add(price);
            }

            Label stock = new Label(product.getStockQuantity() > 0
                    ? "In Stock (" + product.getStockQuantity() + ")"
                    : "Out of Stock");
            stock.getStyleClass().add(product.getStockQuantity() > 0 ? "stock-in" : "stock-out");
            stock.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);

            priceRow.getChildren().add(stock);

            Button detailsBtn = new Button("View Details");
            detailsBtn.getStyleClass().add("secondary-btn");
            detailsBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.openProduct(product.getProductId())));

            Button addCartBtn = new Button("Add to Cart");
            addCartBtn.getStyleClass().add("primary-btn");
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

            HBox buttonRow = new HBox(8);
            buttonRow.setAlignment(Pos.CENTER);
            buttonRow.getChildren().addAll(detailsBtn, addCartBtn);

            actions.getChildren().addAll(priceRow, buttonRow);
        }

        card.getChildren().addAll(imageBox, category, name, spacer, actions);
        return card;
    }
}
