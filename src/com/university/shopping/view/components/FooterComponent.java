package com.university.shopping.view.components;

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
        
        Label aboutLink = new Hyperlink("About Us");
        Label contactLink = new Hyperlink("Contact");
        Label policyLink = new Hyperlink("Privacy Policy");
        
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
