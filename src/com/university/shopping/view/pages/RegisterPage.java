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

public class RegisterPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        // Build form footer with link to login page
        HBox footer = new HBox(5);
        footer.setAlignment(Pos.CENTER);
        Label loginPrompt = new Label("Already have an account?");
        Hyperlink loginLink = new Hyperlink("Log in");
        loginLink.setOnAction(e -> context.getRouter().dispatch(NavIntent.open(Route.AUTH_LOGIN)));
        footer.getChildren().addAll(loginPrompt, loginLink);
        
        // Define form fields with hints
        AuthField[] fields = new AuthField[] {
            new AuthField("Username", null, null, false),
            new AuthField("Password", null, "8+ chars, uppercase, number", true)
        };
        
        // Build and render form using AuthFormComponent
        AuthFormComponent.AuthFormResult form = AuthFormComponent.builder()
            .withTitle("Create an Account")
            .withSubtitle("Join TechVolt Electronics today.")
            .withFields(fields)
            .withButtonText("Register")
            .withOnSubmit(() -> {
                String username = form.fields[0].getText();
                String password = form.fields[1].getText();
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
            })
            .withFooter(footer)
            .build();
        
        return form.root;
    }
}
