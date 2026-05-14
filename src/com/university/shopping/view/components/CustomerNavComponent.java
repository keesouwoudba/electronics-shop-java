package com.university.shopping.view.components;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public final class CustomerNavComponent {
    private CustomerNavComponent() {
    }

    public static Node render(ScreenContext context) {
        HBox nav = new HBox(12);
        nav.setPadding(new Insets(14, 20, 14, 20));
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setStyle("-fx-background-color: #f5f7fb; -fx-border-color: #e0e2ec transparent #e0e2ec transparent;");

        Label brand = new Label("TechVolt Electronics");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #16324f;");
        nav.getChildren().add(brand);

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        nav.getChildren().add(spacer);

        Route currentRoute = context.getAppState().getCurrentRoute();

        Button browseBtn = new Button("Browse");
        browseBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.CUSTOMER_PRODUCTS)));
        if (currentRoute == Route.CUSTOMER_PRODUCTS) {
            browseBtn.setDisable(true);
            browseBtn.setStyle("-fx-background-color: #dfe7f3; -fx-text-fill: #16324f;");
        }

        Button cartBtn = new Button("Cart");
        cartBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.CUSTOMER_CART)));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            context.getAuthService().logout();
            context.getRenderer().navigate(NavIntent.open(Route.GUEST_PRODUCTS));
        });

        nav.getChildren().addAll(browseBtn, cartBtn, logoutBtn);
        return nav;
    }
}