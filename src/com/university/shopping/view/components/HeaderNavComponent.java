package com.university.shopping.view.components;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.util.StyleHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * Navigation header component providing branding, navigation links, and user status.
 */
public class HeaderNavComponent {
    private final HBox root;
    private final Label userBadge;
    private ScreenContext context;

    public HeaderNavComponent() {
        this.root = new HBox(12);
        this.root.setPadding(new Insets(14, 20, 14, 20));
        this.root.setAlignment(Pos.CENTER_LEFT);
        this.root.setStyle("-fx-background-color: linear-gradient(to right, #16324f, #2d5f89); -fx-text-fill: white;");
        this.userBadge = new Label();
    }

    public Node render(ScreenContext context) {
        this.context = context;
        root.getChildren().clear();
        
        // Brand logo/title
        Label brand = new Label("TechVolt Electronics");
        brand.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        root.getChildren().add(brand);
        
        // Spacer
        Region spacer = StyleHelper.createHorizontalSpacer();
        root.getChildren().add(spacer);
        
        // User info display
        String userInfo = context.getAuthService().isLoggedIn()
            ? context.getAuthService().getCurrentUser().getUsername()
            : "Guest";
        
        userBadge.setText(userInfo);
        userBadge.setStyle("-fx-font-size: 13px; -fx-text-fill: white;");
        root.getChildren().add(userBadge);
        
        // Logout button (only if logged in)
        if (context.getAuthService().isLoggedIn()) {
            Button logoutBtn = new Button("Logout");
            logoutBtn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-weight: bold;");
            logoutBtn.setOnAction(e -> handleLogout());
            root.getChildren().add(logoutBtn);
        }
        
        return root;
    }

    public void refresh(ScreenContext context) {
        if (context != null) {
            this.context = context;
            render(context);
        }
    }
    
    private void handleLogout() {
        if (context != null) {
            context.getAuthService().logout();
            context.getRenderer().navigate(NavIntent.open(Route.GUEST_PRODUCTS));
        }
    }
}
