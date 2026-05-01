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

public class LoginPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        // Build form footer with link to register page
        HBox footer = new HBox(5);
        footer.setAlignment(Pos.CENTER);
        Label registerPrompt = new Label("Don't have an account?");
        Hyperlink registerLink = new Hyperlink("Register here");
        registerLink.setOnAction(e -> context.getRouter().dispatch(NavIntent.open(Route.AUTH_REGISTER)));
        footer.getChildren().addAll(registerPrompt, registerLink);
        
        // Define form fields
        AuthField[] fields = new AuthField[] {
            new AuthField("Username", "admin@techvolt.com", null, false),
            new AuthField("Password", "••••••••", null, true)
        };
        
        // Build and render form using AuthFormComponent
        AuthFormComponent.AuthFormResult form = AuthFormComponent.builder()
            .withTitle("Sign in to TechVolt Electronics")
            .withFields(fields)
            .withButtonText("Login")
            .withOnSubmit(() -> {
                String username = form.fields[0].getText();
                String password = form.fields[1].getText();
                String result = context.getAuthService().login(username, password);
                if ("Successfull login".equals(result)) {
                    context.getRouter().dispatch(NavIntent.loginSuccess());
                } else {
                    context.getRenderer().setNotification("Login failed: " + result, true);
                }
            })
            .withFooter(footer)
            .build();
        
        return form.root;
    }
}
