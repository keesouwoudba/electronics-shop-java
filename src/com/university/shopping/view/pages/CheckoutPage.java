package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.model.Cart;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

public class CheckoutPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));

        Button backBtn = new Button("← Return to Cart");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #005bbf; -fx-cursor: hand;");
        backBtn.setOnAction(e -> context.getRouter().dispatch(NavIntent.open(Route.CUSTOMER_CART)));

        Label title = new Label("Checkout");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        Label subtitle = new Label("Review your order and confirm purchase.");
        subtitle.setStyle("-fx-text-fill: grey;");

        int userId = context.getAppState().getCurrentUser().getUserId();
        Cart cart = context.getShopService().viewCart(userId);

        if (cart == null || cart.getItemCount() == 0) {
            root.getChildren().addAll(title, new Label("Your cart is empty."));
            return root;
        }

        HBox mainLayout = new HBox(30);

        // Form (Dummy fields for UI completeness based on checkout style)
        VBox formBox = new VBox(20);
        HBox.setHgrow(formBox, Priority.ALWAYS);

        VBox shippingBox = new VBox(10);
        shippingBox.setStyle("-fx-border-color: #e0e2ec; -fx-border-radius: 8px; -fx-padding: 20px;");
        Label shipTitle = new Label("Shipping Information");
        shipTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        shippingBox.getChildren().addAll(shipTitle, 
            new TextField() {{ setPromptText("First Name"); }},
            new TextField() {{ setPromptText("Last Name"); }},
            new TextField() {{ setPromptText("Address"); }}
        );

        VBox payBox = new VBox(10);
        payBox.setStyle("-fx-border-color: #e0e2ec; -fx-border-radius: 8px; -fx-padding: 20px;");
        Label payTitle = new Label("Payment Details");
        payTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        payBox.getChildren().addAll(payTitle, 
            new TextField() {{ setPromptText("Card Number"); }},
            new TextField() {{ setPromptText("MM/YY"); }}
        );

        formBox.getChildren().addAll(shippingBox, payBox);

        // Summary Box
        VBox summaryBox = new VBox(15);
        summaryBox.setPrefWidth(350);
        summaryBox.setStyle("-fx-border-color: #e0e2ec; -fx-border-radius: 8px; -fx-padding: 20px; -fx-background-color: #f9f9ff;");
        
        Label sumTitle = new Label("Order Summary");
        sumTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox totalRow = new HBox();
        Label totalLbl = new Label("Grand Total");
        totalLbl.setStyle("-fx-font-size: 18px;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label totalVal = new Label(String.format("$%.2f", cart.getTotalPrice()));
        totalVal.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #005bbf;");
        totalRow.getChildren().addAll(totalLbl, spacer, totalVal);

        Button confirmBtn = new Button("\uD83D\uDD12 Confirm Purchase");
        confirmBtn.setMaxWidth(Double.MAX_VALUE);
        confirmBtn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12px;");
        confirmBtn.setOnAction(e -> {
            String result = context.getShopService().checkout(userId);
            if ("Success".equals(result)) {
                // Flash message is handled by CHECKOUT_SUCCESS intent in router
                context.getRouter().dispatch(NavIntent.checkoutSuccess());
            } else {
                context.getRenderer().setNotification("Checkout failed: " + result, true);
            }
        });

        Label secureLbl = new Label("Secure, encrypted transaction.");
        secureLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: grey;");
        secureLbl.setAlignment(Pos.CENTER);
        secureLbl.setMaxWidth(Double.MAX_VALUE);

        summaryBox.getChildren().addAll(sumTitle, totalRow, confirmBtn, secureLbl);

        mainLayout.getChildren().addAll(formBox, summaryBox);
        root.getChildren().addAll(backBtn, title, subtitle, mainLayout);

        return root;
    }
}
