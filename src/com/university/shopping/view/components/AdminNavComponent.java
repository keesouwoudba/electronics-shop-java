package com.university.shopping.view.components;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Admin navigation component - provides tabs to switch between admin sections
 */
public class AdminNavComponent {
    public static Node render(ScreenContext context, Route currentRoute) {
        HBox nav = new HBox(10);
        nav.setPadding(new Insets(12, 20, 12, 20));
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #e0e2ec transparent #e0e2ec transparent;");

        // Products Tab
        Button productsBtn = new Button("📦 Products");
        productsBtn.setStyle(currentRoute == Route.ADMIN_PRODUCTS 
            ? "-fx-background-color: #005bbf; -fx-text-fill: white; -fx-padding: 8px 16px;" 
            : "-fx-background-color: transparent; -fx-text-fill: #005bbf; -fx-padding: 8px 16px;");
        productsBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.ADMIN_PRODUCTS)));

        // Users Tab
        Button usersBtn = new Button("👥 Users");
        usersBtn.setStyle(currentRoute == Route.ADMIN_USERS 
            ? "-fx-background-color: #005bbf; -fx-text-fill: white; -fx-padding: 8px 16px;" 
            : "-fx-background-color: transparent; -fx-text-fill: #005bbf; -fx-padding: 8px 16px;");
        usersBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.ADMIN_USERS)));

        // Reports Tab
        Button reportsBtn = new Button("📊 Reports");
        reportsBtn.setStyle(currentRoute == Route.ADMIN_REPORTS 
            ? "-fx-background-color: #005bbf; -fx-text-fill: white; -fx-padding: 8px 16px;" 
            : "-fx-background-color: transparent; -fx-text-fill: #005bbf; -fx-padding: 8px 16px;");
        reportsBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.ADMIN_REPORTS)));

        nav.getChildren().addAll(productsBtn, usersBtn, reportsBtn);
        return nav;
    }
}
