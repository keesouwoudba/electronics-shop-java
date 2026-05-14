package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.view.components.AuthFormComponent;
import com.university.shopping.view.components.AuthFormComponent.AuthField;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextInputControl;

public class LoginPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        // Build form footer with link to register page
        HBox footer = new HBox(5);
        footer.setAlignment(Pos.CENTER);
        Label registerPrompt = new Label("Don't have an account?");
        Hyperlink registerLink = new Hyperlink("Register here");
        registerLink.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.REGISTER)));
        footer.getChildren().addAll(registerPrompt, registerLink);
        
        // Define form fields
        AuthField[] fields = new AuthField[] {
            new AuthField("Username", "admin or customer", null, false),
            new AuthField("Password", "••••••••", null, true)
        };
        
        // Build and render form using AuthFormComponent
        AuthFormComponent.AuthFormResult form = AuthFormComponent.builder()
            .withTitle("Sign in to TechVolt Electronics")
            .withFields(fields)
            .withButtonText("Login")
            .withOnSubmit(inputs -> {
                String username = inputs[0].getText();
                String password = inputs[1].getText();
                String result = context.getAuthService().login(username, password);
                if ("Successfull login".equals(result)) {
                    context.getRenderer().navigate(NavIntent.loginSuccess());
                } else {
                    context.getRenderer().setNotification("Login failed: " + result, true);
                }
            })
            .withFooter(footer)
            .build();
        
        javafx.scene.control.Button backBtn = new javafx.scene.control.Button("← Back to Catalog");
        backBtn.getStyleClass().add("secondary-btn");
        backBtn.setStyle("-fx-border-color: transparent; -fx-padding: 10 20;");
        backBtn.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.GUEST_PRODUCTS)));

        javafx.scene.layout.VBox wrapper = new javafx.scene.layout.VBox();
        wrapper.getChildren().addAll(backBtn, form.root);
        
        return wrapper;
    }
}
