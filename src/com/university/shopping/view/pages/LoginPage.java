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

public class LoginPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setMaxWidth(400);

        Label titleLabel = new Label("Sign in to TechVolt Electronics");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox form = new VBox(15);
        
        VBox userBox = new VBox(5);
        Label userLabel = new Label("Username");
        TextField userField = new TextField();
        userField.setPromptText("admin@techvolt.com");
        userBox.getChildren().addAll(userLabel, userField);

        VBox passBox = new VBox(5);
        Label passLabel = new Label("Password");
        PasswordField passField = new PasswordField();
        passField.setPromptText("••••••••");
        passBox.getChildren().addAll(passLabel, passField);

        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-font-weight: bold;");

        loginBtn.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            String result = context.getAuthService().login(username, password);
            if ("Successfull login".equals(result)) {
                context.getRouter().dispatch(NavIntent.loginSuccess());
            } else {
                context.getRenderer().setNotification("Login failed: " + result, true);
            }
        });

        HBox footer = new HBox(5);
        footer.setAlignment(Pos.CENTER);
        Label registerPrompt = new Label("Don't have an account?");
        Hyperlink registerLink = new Hyperlink("Register here");
        registerLink.setOnAction(e -> context.getRouter().dispatch(NavIntent.open(Route.AUTH_REGISTER)));
        
        footer.getChildren().addAll(registerPrompt, registerLink);

        form.getChildren().addAll(userBox, passBox, loginBtn, footer);
        root.getChildren().addAll(titleLabel, form);

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50, 0, 0, 0));
        container.getChildren().add(root);

        return container;
    }
}
