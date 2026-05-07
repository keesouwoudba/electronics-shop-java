package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.model.Product;
import com.university.shopping.view.components.CustomerNavComponent;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductDetailsPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox();
        root.getChildren().add(CustomerNavComponent.render(context));

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Button backBtn = new Button("← Back to Catalog");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #005bbf; -fx-cursor: hand;");
        backBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.CUSTOMER_PRODUCTS)));

        Integer pid = context.getAppState().getSelectedProductId();
        if (pid == null) {
            content.getChildren().addAll(backBtn, new Label("No product selected."));
            root.getChildren().add(content);
            return root;
        }

        Product p = context.getShopService().getProductById(pid);
        if (p == null) {
            content.getChildren().addAll(backBtn, new Label("Product not found."));
            root.getChildren().add(content);
            return root;
        }

        HBox mainLayout = new HBox(40);
        
        // Image (with placeholder fallback)
        VBox imageBox = new VBox();
        imageBox.setPrefSize(400, 400);
        imageBox.setStyle("-fx-background-color: #f2f3fd; -fx-border-color: #e0e2ec; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        imageBox.setAlignment(Pos.CENTER);
        Node imgNode = com.university.shopping.view.util.ImageHelper.createProductImageView(p, 400, 400);
        imageBox.getChildren().add(imgNode);

        // Details Column
        VBox detailsBox = new VBox(15);
        detailsBox.setMaxWidth(500);

        Label skuLabel = new Label("SKU: PRD-" + p.getProductId());
        skuLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 12px;");

        Label title = new Label(p.getName());
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        title.setWrapText(true);

        Label desc = new Label(p.getDescription());
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #414754;");
        desc.setWrapText(true);

        // Pricing
        VBox pricingBox = new VBox(5);
        pricingBox.setStyle("-fx-border-color: #e0e2ec; -fx-border-radius: 8px; -fx-padding: 20px;");
        
        if (p.isDiscounted()) {
            Label oldPrice = new Label(String.format("$%.2f", p.getPrice()));
            oldPrice.setStyle("-fx-strikethrough: true; -fx-text-fill: grey; -fx-font-size: 14px;");
            
            Label newPrice = new Label(String.format("$%.2f", p.getFinalPrice()));
            newPrice.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #005bbf;");
            
            Label discountBadge = new Label(String.format("-%.0f%% OFF", p.getDiscountPercentage()));
            discountBadge.setStyle("-fx-background-color: #ba1a1a; -fx-text-fill: white; -fx-padding: 2px 6px; -fx-background-radius: 4px; -fx-font-size: 12px;");
            
            pricingBox.getChildren().addAll(discountBadge, oldPrice, newPrice);
        } else {
            Label price = new Label(String.format("$%.2f", p.getPrice()));
            price.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #005bbf;");
            pricingBox.getChildren().add(price);
        }

        Label stockLabel = new Label();
        int stock = p.getStockQuantity();
        if (stock > 0) {
            stockLabel.setText("In Stock (" + stock + " available)");
            stockLabel.setStyle("-fx-text-fill: #00c853; -fx-font-weight: bold;");
        } else {
            stockLabel.setText("Out of Stock");
            stockLabel.setStyle("-fx-text-fill: #ba1a1a; -fx-font-weight: bold;");
        }
        pricingBox.getChildren().add(stockLabel);

        // Action Row
        HBox actionRow = new HBox(15);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        // Quantity options represented as a primitive array (no other data structures)
        int[] qtyOptions;
        if (stock > 0) {
            qtyOptions = new int[stock];
            for (int i = 0; i < stock; i++) qtyOptions[i] = i + 1;
        } else {
            qtyOptions = new int[0];
        }

        // Buttons array mirrors qtyOptions so we can update styles on selection
        Button[] qtyButtons = new Button[qtyOptions.length];
        final int[] selectedQty = new int[]{ qtyOptions.length > 0 ? qtyOptions[0] : 0 };

        HBox qtyBox = new HBox(6);
        for (int i = 0; i < qtyOptions.length; i++) {
            int q = qtyOptions[i];
            Button btn = new Button(String.valueOf(q));
            btn.setStyle("-fx-background-color: transparent; -fx-border-color: #e0e2ec; -fx-padding: 6px;");
            if (q == selectedQty[0]) {
                btn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-padding: 6px;");
            }
            btn.setOnAction(ev -> {
                selectedQty[0] = q;
                // update styles for all buttons using the array
                for (int j = 0; j < qtyButtons.length; j++) {
                    if (qtyButtons[j] != null) {
                        int val = Integer.parseInt(qtyButtons[j].getText());
                        if (val == selectedQty[0]) {
                            qtyButtons[j].setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-padding: 6px;");
                        } else {
                            qtyButtons[j].setStyle("-fx-background-color: transparent; -fx-border-color: #e0e2ec; -fx-padding: 6px;");
                        }
                    }
                }
            });
            qtyButtons[i] = btn;
            qtyBox.getChildren().add(btn);
        }

        if (qtyOptions.length == 0) {
            Label noQty = new Label("0");
            noQty.setDisable(true);
            qtyBox.getChildren().add(noQty);
        }

        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        addToCartBtn.setDisable(stock == 0);
        addToCartBtn.setOnAction(e -> {
            int qty = selectedQty[0];
            int userId = context.getAppState().getCurrentUser().getUserId();
            boolean success = context.getShopService().addToCart(p.getProductId(), qty, userId);
            if (success) {
                context.getRenderer().setNotification("Successfully added " + qty + " of " + p.getName() + " to cart.", false);
                context.getRenderer().navigate(NavIntent.open(Route.CUSTOMER_CART));
            } else {
                context.getRenderer().setNotification("Failed to add to cart.", true);
            }
        });

        actionRow.getChildren().addAll(new Label("Quantity:"), qtyBox, addToCartBtn);
        pricingBox.getChildren().add(actionRow);

        detailsBox.getChildren().addAll(skuLabel, title, desc, pricingBox);
        mainLayout.getChildren().addAll(imageBox, detailsBox);

        content.getChildren().addAll(backBtn, mainLayout);
        root.getChildren().add(content);
        return root;
    }
}
