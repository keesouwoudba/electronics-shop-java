package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.model.Product;
import com.university.shopping.view.components.AdminNavComponent;
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

public class AdminProductsPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox();
        
        // Admin navigation tabs
        root.getChildren().add(AdminNavComponent.render(context, Route.ADMIN_PRODUCTS));
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("Product Management");
        title.getStyleClass().add("page-title");
        
        // Spacer
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addBtn = new Button("+ Add New Product");
        addBtn.getStyleClass().add("primary-btn");
        addBtn.setStyle("-fx-font-weight: bold; -fx-padding: 8px 16px;"); // Keep specific padding
        addBtn.setOnAction(e -> {
            context.getAppState().setSelectedProductId(null); // Clear selected product for a new one
            context.getRenderer().navigate(NavIntent.open(Route.ADMIN_PRODUCT_EDITOR));
        });

        headerBox.getChildren().addAll(title, spacer, addBtn);
        content.getChildren().add(headerBox);

        VBox listContainer = new VBox(10);
        listContainer.getStyleClass().add("list-container");
        listContainer.setStyle("-fx-padding: 20px;"); // Keep specific padding

        // Header Row
        HBox tableHeader = new HBox(15);
        tableHeader.getStyleClass().add("table-header");
        tableHeader.setStyle("-fx-padding: 0 0 10 0;");
        
        Label idHeader = new Label("ID"); idHeader.setPrefWidth(50); idHeader.setStyle("-fx-font-weight: bold;");
        Label thumbHeader = new Label("IMG"); thumbHeader.setPrefWidth(60); thumbHeader.setStyle("-fx-font-weight: bold;");
        Label nameHeader = new Label("NAME"); nameHeader.setPrefWidth(200); nameHeader.setStyle("-fx-font-weight: bold;");
        Label priceHeader = new Label("PRICE"); priceHeader.setPrefWidth(100); priceHeader.setStyle("-fx-font-weight: bold;");
        Label stockHeader = new Label("STOCK"); stockHeader.setPrefWidth(80); stockHeader.setStyle("-fx-font-weight: bold;");
        Label actionHeader = new Label("ACTIONS"); actionHeader.setPrefWidth(120); actionHeader.setStyle("-fx-font-weight: bold;");

        tableHeader.getChildren().addAll(idHeader, thumbHeader, nameHeader, priceHeader, stockHeader, actionHeader);
        listContainer.getChildren().add(tableHeader);

        Product[] products = context.getShopService().getAllProducts();
        if (products == null || products.length == 0) {
            listContainer.getChildren().add(new Label("No products found in the system."));
        } else {
            for (Product p : products) {
                HBox row = new HBox(15);
                row.setAlignment(Pos.CENTER_LEFT);
                row.getStyleClass().add("table-row");
                row.setStyle("-fx-padding: 10 0;");

                Label idCol = new Label(String.valueOf(p.getProductId())); 
                idCol.setPrefWidth(50);
                
                Node thumbCol = com.university.shopping.view.util.ImageHelper.createProductImageView(p, 48, 48);
                // ensure consistent cell width
                thumbCol.setStyle("-fx-text-fill: grey;");
                if (thumbCol instanceof javafx.scene.layout.Region) {
                    ((javafx.scene.layout.Region) thumbCol).setPrefWidth(60);
                }
                
                Label nameCol = new Label(p.getName()); 
                nameCol.setPrefWidth(200); nameCol.setStyle("-fx-font-weight: bold;");
                nameCol.setWrapText(true);
                
                Label priceCol = new Label(String.format("$%.2f", p.getPrice())); 
                priceCol.setPrefWidth(100);
                
                Label stockCol = new Label(String.valueOf(p.getStockQuantity())); 
                stockCol.setPrefWidth(80);
                
                HBox actionCol = new HBox(10);
                actionCol.setPrefWidth(120);
                
                Button editBtn = new Button("Edit");
                editBtn.setStyle("-fx-background-color: #f2f3fd; -fx-text-fill: #005bbf; -fx-cursor: hand;");
                editBtn.setOnAction(e -> {
                    context.getAppState().setSelectedProductId(p.getProductId());
                    context.getRenderer().navigate(NavIntent.open(Route.ADMIN_PRODUCT_EDITOR));
                });

                Button delBtn = new Button("Del");
                delBtn.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #ba1a1a; -fx-cursor: hand;");
                delBtn.setOnAction(e -> {
                    String result = context.getAdminService().deleteProduct(p.getProductId());
                    context.getRenderer().setNotification("Delete: " + result, "SUCCESS".equals(result) ? false : true);
                    if ("SUCCESS".equals(result)) {
                        context.getRenderer().navigate(NavIntent.open(Route.ADMIN_PRODUCTS));
                    }
                });

                actionCol.getChildren().addAll(editBtn, delBtn);
                
                row.getChildren().addAll(idCol, thumbCol, nameCol, priceCol, stockCol, actionCol);
                listContainer.getChildren().add(row);
            }
        }

        content.getChildren().add(listContainer);
        root.getChildren().add(content);
        return root;
    }
}
