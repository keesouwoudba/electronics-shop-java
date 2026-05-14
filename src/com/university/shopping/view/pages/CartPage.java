package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.model.Cart;
import com.university.shopping.model.OrderItem;
import com.university.shopping.view.components.CustomerNavComponent;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

public class CartPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox();
        root.getChildren().add(CustomerNavComponent.render(context));

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Your Cart");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        content.getChildren().add(title);

        int userId = context.getAppState().getCurrentUser().getUserId();
        Cart cart = context.getShopService().viewCart(userId);

        if (cart == null || cart.getItemCount() == 0) {
            Label emptyLbl = new Label("Your cart is empty. Time to start shopping!");
            Button browseBtn = new Button("Browse Products");
            browseBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.CUSTOMER_PRODUCTS)));
            content.getChildren().addAll(emptyLbl, browseBtn);
            root.getChildren().add(content);
            return root;
        }

        HBox mainLayout = new HBox(30);

        // Cart Items List
        VBox itemsBox = new VBox(15);
        itemsBox.setStyle("-fx-border-color: #e0e2ec; -fx-border-radius: 8px; -fx-padding: 20px;");
        HBox.setHgrow(itemsBox, Priority.ALWAYS);

        // Table Header
        HBox header = new HBox();
        header.setStyle("-fx-border-color: transparent transparent #e0e2ec transparent; -fx-padding: 0 0 10 0;");
        Label prodHeader = new Label("PRODUCT");
        prodHeader.setPrefWidth(250);
        Label qtyHeader = new Label("QUANTITY");
        qtyHeader.setPrefWidth(100);
        Label priceHeader = new Label("PRICE");
        priceHeader.setPrefWidth(100);
        Label totalHeader = new Label("TOTAL");
        totalHeader.setPrefWidth(100);
        header.getChildren().addAll(prodHeader, qtyHeader, priceHeader, totalHeader);
        itemsBox.getChildren().add(header);

        OrderItem[] items = cart.getItems();
        for (int i = 0; i < cart.getItemCount(); i++) {
            OrderItem item = items[i];
            if (item != null) {
                HBox row = new HBox();
                row.setAlignment(Pos.CENTER_LEFT);
                row.setStyle("-fx-padding: 10 0;");

                VBox pCol = new VBox(5);
                pCol.setPrefWidth(250);
                Label name = new Label(item.getProductName());
                name.setStyle("-fx-font-weight: bold;");
                Label pId = new Label("ID: " + item.getProductId());
                pId.setStyle("-fx-text-fill: grey; -fx-font-size: 11px;");
                pCol.getChildren().addAll(name, pId);

                Label qCol = new Label(String.valueOf(item.getQuantity()));
                qCol.setPrefWidth(100);
                
                Label priceCol = new Label(String.format("$%.2f", item.getPriceAtPurchase()));
                priceCol.setPrefWidth(100);
                
                double rowTotal = item.getQuantity() * item.getPriceAtPurchase();
                Label totalCol = new Label(String.format("$%.2f", rowTotal));
                totalCol.setPrefWidth(100);

                Button delBtn = new Button("🗑");
                delBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-cursor: hand;");
                delBtn.setOnAction(e -> {
                    context.getShopService().removeFromCart(item.getProductId(), userId);
                    context.getRenderer().navigate(NavIntent.open(Route.CUSTOMER_CART)); // reload
                });

                row.getChildren().addAll(pCol, qCol, priceCol, totalCol, delBtn);
                itemsBox.getChildren().add(row);
            }
        }

        // Summary Box
        VBox summaryBox = new VBox(15);
        summaryBox.setPrefWidth(300);
        summaryBox.setStyle("-fx-border-color: #e0e2ec; -fx-border-radius: 8px; -fx-padding: 20px;");
        
        Label sumTitle = new Label("Order Summary");
        sumTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox totalRow = new HBox();
        Label totalLbl = new Label("Grand Total");
        totalLbl.setStyle("-fx-font-size: 18px;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label totalVal = new Label(String.format("$%.2f", cart.getTotalPrice()));
        totalVal.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        totalRow.getChildren().addAll(totalLbl, spacer, totalVal);

        Button checkoutBtn = new Button("Proceed to Checkout →");
        checkoutBtn.setMaxWidth(Double.MAX_VALUE);
        checkoutBtn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12px;");
        checkoutBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.CUSTOMER_CHECKOUT)));

        summaryBox.getChildren().addAll(sumTitle, totalRow, checkoutBtn);

        mainLayout.getChildren().addAll(itemsBox, summaryBox);
        content.getChildren().add(mainLayout);
        root.getChildren().add(content);

        return root;
    }
}
