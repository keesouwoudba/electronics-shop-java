package com.university.shopping.view.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * Reusable styling utilities and helpers for creating consistent page layouts,
 * buttons, cards, and containers across the shopping application.
 */
public class StyleHelper {
    
    // Spacing constants
    public static final double PADDING_LARGE = 30;
    public static final double PADDING_MEDIUM = 20;
    public static final double PADDING_SMALL = 10;
    public static final double SPACING_LARGE = 20;
    public static final double SPACING_MEDIUM = 15;
    public static final double SPACING_SMALL = 10;
    
    // Color constants
    public static final String COLOR_PRIMARY_BLUE = "#005bbf";
    public static final String COLOR_BORDER_LIGHT = "#e0e2ec";
    public static final String COLOR_BACKGROUND_LIGHT = "#f2f5f9";
    public static final String COLOR_TEXT_WHITE = "white";
    public static final String COLOR_TEXT_DARK = "black";
    
    // Style strings
    private static final String TITLE_STYLE = "-fx-font-size: 28px; -fx-font-weight: bold;";
    private static final String SUBTITLE_STYLE = "-fx-font-size: 24px; -fx-font-weight: bold;";
    private static final String BUTTON_PRIMARY_STYLE = "-fx-background-color: " + COLOR_PRIMARY_BLUE + 
        "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px;";
    private static final String CARD_STYLE = "-fx-border-color: " + COLOR_BORDER_LIGHT + 
        "; -fx-border-radius: 8px; -fx-padding: 20px; -fx-background-color: white;";
    
    /**
     * Creates a styled page container with standard padding and spacing.
     */
    public static VBox createPageContainer() {
        VBox container = new VBox(SPACING_LARGE);
        container.setPadding(new Insets(PADDING_LARGE));
        return container;
    }
    
    /**
     * Creates a styled title label.
     */
    public static Label createPageTitle(String text) {
        Label label = new Label(text);
        label.setStyle(TITLE_STYLE);
        return label;
    }
    
    /**
     * Creates a styled subtitle label.
     */
    public static Label createSubtitle(String text) {
        Label label = new Label(text);
        label.setStyle(SUBTITLE_STYLE);
        return label;
    }
    
    /**
     * Creates a styled card container for content.
     */
    public static VBox createCard() {
        VBox card = new VBox(SPACING_SMALL);
        card.setStyle(CARD_STYLE);
        return card;
    }
    
    /**
     * Creates a styled card with given content.
     */
    public static VBox createCardWithContent(javafx.scene.Node... content) {
        VBox card = createCard();
        for (javafx.scene.Node node : content) {
            card.getChildren().add(node);
        }
        return card;
    }
    
    /**
     * Creates a styled primary action button.
     */
    public static Button createPrimaryButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle(BUTTON_PRIMARY_STYLE);
        return button;
    }
    
    /**
     * Creates a styled secondary button.
     */
    public static Button createSecondaryButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: white; -fx-border-color: " + COLOR_BORDER_LIGHT + 
            "; -fx-text-fill: " + COLOR_TEXT_DARK + "; -fx-padding: 10px;");
        return button;
    }
    
    /**
     * Creates a horizontal spacer that grows to fill available space.
     */
    public static Region createHorizontalSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        return spacer;
    }
    
    /**
     * Creates a vertical spacer with specified height.
     */
    public static Region createVerticalSpacer(double height) {
        Region spacer = new Region();
        spacer.setMinHeight(height);
        return spacer;
    }
    
    /**
     * Creates an empty state panel for pages with no data.
     */
    public static VBox createEmptyStatePanel(String title, String message) {
        VBox panel = createCard();
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: grey;");
        
        panel.getChildren().addAll(titleLabel, messageLabel);
        return panel;
    }
    
    /**
     * Applies consistent page background styling.
     */
    public static void applyPageBackground(VBox page) {
        page.setStyle("-fx-background-color: white;");
    }
    
    /**
     * Applies a row/section separator with spacing.
     */
    public static HBox createFormRow(javafx.scene.Node... children) {
        HBox row = new HBox(SPACING_MEDIUM);
        row.setAlignment(Pos.CENTER_LEFT);
        for (javafx.scene.Node child : children) {
            row.getChildren().add(child);
        }
        return row;
    }
}
