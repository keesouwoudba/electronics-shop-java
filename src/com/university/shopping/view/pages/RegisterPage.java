package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class RegisterPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setMaxWidth(400);

        Label titleLabel = new Label("Create an Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox form = new VBox(15);
        
        VBox userBox = new VBox(5);
        Label userLabel = new Label("Username");
        TextField userField = new TextField();
        userBox.getChildren().addAll(userLabel, userField);

        VBox passBox = new VBox(5);
        Label passLabel = new Label("Password");
        PasswordField passField = new PasswordField();
        Label passHint = new Label("8+ chars, uppercase, number");
        passHint.setStyle("-fx-font-size: 11px; -fx-text-fill: grey;");
        passBox.getChildren().addAll(passLabel, passField, passHint);

        Button registerBtn = new Button("Register");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-font-weight: bold;");

        registerBtn.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            String result = context.getAuthService().register(username, password);
            if ("Successfully registered".equals(result)) {
                // If auto-logged in by auth service
                if (context.getAuthService().isLoggedIn()) {
                    context.getRouter().dispatch(NavIntent.loginSuccess());
                } else {
                    context.getRenderer().setNotification("Registered successfully, please login", false);
                    context.getRouter().dispatch(NavIntent.open(Route.AUTH_LOGIN));
                }
            } else {
                context.getRenderer().setNotification("Registration failed: " + result, true);
            }
        });

        HBox footer = new HBox(5);
        footer.setAlignment(Pos.CENTER);
        Label loginPrompt = new Label("Already have an account?");
        Hyperlink loginLink = new Hyperlink("Log in");
        loginLink.setOnAction(e -> context.getRouter().dispatch(NavIntent.open(Route.AUTH_LOGIN)));
        
        footer.getChildren().addAll(loginPrompt, loginLink);

        form.getChildren().addAll(userBox, passBox, registerBtn, footer);
        root.getChildren().addAll(titleLabel, new Label("Join TechVolt Electronics today."), form);

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50, 0, 0, 0));
        container.getChildren().add(root);

        return container;
    }
}
