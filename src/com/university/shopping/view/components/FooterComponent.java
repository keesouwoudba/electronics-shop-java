package com.university.shopping.view.components;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

/**
 * Footer component providing copyright, links, and company information.
 */
public class FooterComponent {
    private final VBox root;

    public FooterComponent() {
        this.root = new VBox(12);
        this.root.setPadding(new Insets(20));
        this.root.setAlignment(Pos.CENTER);
        this.root.setStyle("-fx-background-color: #f2f5f9; -fx-border-color: #e0e2ec; -fx-border-width: 1 0 0 0;");
    }

    public Node render(ScreenContext context) {
        root.getChildren().clear();
        
        // Footer links row
        HBox linksRow = new HBox(20);
        linksRow.setAlignment(Pos.CENTER);
        
        Hyperlink aboutLink = new Hyperlink("About Us");
        Hyperlink contactLink = new Hyperlink("Contact");
        Hyperlink policyLink = new Hyperlink("Privacy Policy");

        aboutLink.setOnAction(e -> {
            if (context != null && context.getRenderer() != null) {
                context.getRenderer().setNotification("About Us page is not implemented yet.", false);
            }
        });
        contactLink.setOnAction(e -> {
            if (context != null && context.getRenderer() != null) {
                context.getRenderer().navigate(NavIntent.open(Route.GUEST_PRODUCTS));
            }
        });
        policyLink.setOnAction(e -> {
            if (context != null && context.getRenderer() != null) {
                context.getRenderer().setNotification("Privacy Policy page is not implemented yet.", false);
            }
        });
        
        linksRow.getChildren().addAll(aboutLink, contactLink, policyLink);
        root.getChildren().add(linksRow);
        
        // Copyright info
        Label copyright = new Label("© 2026 TechVolt Electronics. All rights reserved.");
        copyright.setStyle("-fx-font-size: 11px; -fx-text-fill: grey;");
        root.getChildren().add(copyright);
        
        // Additional info
        Label tagline = new Label("Powered by JavaFX | Version 1.0");
        tagline.setStyle("-fx-font-size: 10px; -fx-text-fill: grey; -fx-opacity: 0.7;");
        root.getChildren().add(tagline);
        
        return root;
    }
}
