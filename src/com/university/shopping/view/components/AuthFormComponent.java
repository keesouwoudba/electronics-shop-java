package com.university.shopping.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.util.function.Consumer;

/**
 * Reusable form component for authentication pages (login/register).
 * Provides a common structure: title, form fields, button, and footer link.
 */
public class AuthFormComponent {
    
    public static class AuthField {
        public final String label;
        public final String placeholder;
        public final String hint;
        public final boolean isPassword;
        
        public AuthField(String label, String placeholder, String hint, boolean isPassword) {
            this.label = label;
            this.placeholder = placeholder;
            this.hint = hint;
            this.isPassword = isPassword;
        }
    }
    
    public static class AuthFormBuilder {
        private String title;
        private String subtitle;
        private AuthField[] fields;
        private String buttonText;
        private Consumer<TextInputControl[]> onSubmit;
        private Node footerNode;
        
        public AuthFormBuilder withTitle(String title) {
            this.title = title;
            return this;
        }
        
        public AuthFormBuilder withSubtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }
        
        public AuthFormBuilder withFields(AuthField... fields) {
            this.fields = fields;
            return this;
        }
        
        public AuthFormBuilder withButtonText(String text) {
            this.buttonText = text;
            return this;
        }
        
        public AuthFormBuilder withOnSubmit(Consumer<TextInputControl[]> onSubmit) {
            this.onSubmit = onSubmit;
            return this;
        }
        
        public AuthFormBuilder withFooter(Node footer) {
            this.footerNode = footer;
            return this;
        }
        
        public AuthFormResult build() {
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(40));
            root.setMaxWidth(400);
            
            // Title
            Label titleLabel = new Label(title != null ? title : "Auth");
            titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            root.getChildren().add(titleLabel);
            
            // Subtitle
            if (subtitle != null && !subtitle.isEmpty()) {
                root.getChildren().add(new Label(subtitle));
            }
            
            // Form container
            VBox form = new VBox(15);
            TextInputControl[] textFields = new TextInputControl[fields != null ? fields.length : 0];
            
            if (fields != null) {
                for (int i = 0; i < fields.length; i++) {
                    AuthField field = fields[i];
                    VBox fieldBox = new VBox(5);
                    
                    Label fieldLabel = new Label(field.label);
                    Node fieldInput;
                    
                    if (field.isPassword) {
                        PasswordField passwordField = new PasswordField();
                        if (field.placeholder != null) {
                            passwordField.setPromptText(field.placeholder);
                        }
                        fieldInput = passwordField;
                        textFields[i] = passwordField;
                    } else {
                        TextField textField = new TextField();
                        if (field.placeholder != null) {
                            textField.setPromptText(field.placeholder);
                        }
                        fieldInput = textField;
                        textFields[i] = textField;
                    }
                    
                    fieldBox.getChildren().add(fieldLabel);
                    fieldBox.getChildren().add(fieldInput);
                    
                    if (field.hint != null && !field.hint.isEmpty()) {
                        Label hintLabel = new Label(field.hint);
                        hintLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: grey;");
                        fieldBox.getChildren().add(hintLabel);
                    }
                    
                    form.getChildren().add(fieldBox);
                }
            }
            
            // Button
            Button submitButton = new Button(buttonText != null ? buttonText : "Submit");
            submitButton.setMaxWidth(Double.MAX_VALUE);
            submitButton.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px;");
            submitButton.setOnAction(e -> {
                if (onSubmit != null) {
                    onSubmit.accept(textFields);
                }
            });
            form.getChildren().add(submitButton);
            
            // Footer
            if (footerNode != null) {
                form.getChildren().add(footerNode);
            }
            
            root.getChildren().add(form);
            
            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.setPadding(new Insets(50, 0, 0, 0));
            container.getChildren().add(root);
            
            return new AuthFormResult(container, textFields);
        }
    }
    
    public static class AuthFormResult {
        public final Node root;
        public final TextInputControl[] fields;
        
        public AuthFormResult(Node root, TextInputControl[] fields) {
            this.root = root;
            this.fields = fields;
        }
    }
    
    public static AuthFormBuilder builder() {
        return new AuthFormBuilder();
    }
}
