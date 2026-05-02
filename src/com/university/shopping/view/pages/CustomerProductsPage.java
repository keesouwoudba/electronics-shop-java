package com.university.shopping.view.pages;

import com.university.shopping.model.Product;
import com.university.shopping.view.components.CustomerNavComponent;
import com.university.shopping.view.components.ProductCardComponent;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class CustomerProductsPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox();
        root.getChildren().add(CustomerNavComponent.render(context));

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        VBox headerBox = new VBox(5);
        Label title = new Label("Product Catalog");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label subtitle = new Label("Explore our high-fidelity electronics and components.");
        headerBox.getChildren().addAll(title, subtitle);

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

        content.getChildren().addAll(headerBox, grid);
        root.getChildren().add(content);
        return root;
    }
}
