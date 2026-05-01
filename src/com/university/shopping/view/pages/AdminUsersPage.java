package com.university.shopping.view.pages;

import com.university.shopping.model.User;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AdminUsersPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));

        Label title = new Label("User Management");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        root.getChildren().add(title);
        
        // Use a styled placeholder until the UserRepository exposes getAllUsers()
        VBox listContainer = new VBox(10);
        listContainer.setStyle("-fx-border-color: #e0e2ec; -fx-border-radius: 8px; -fx-padding: 20px; -fx-background-color: white;");
        listContainer.getChildren().add(new Label("User list API must be connected via AdminService to be displayed here."));

        root.getChildren().add(listContainer);

        return root;
    }
}
