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

public class RegisterPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        // Build form footer with link to login page
        HBox footer = new HBox(5);
        footer.setAlignment(Pos.CENTER);
        Label loginPrompt = new Label("Already have an account?");
        Hyperlink loginLink = new Hyperlink("Log in");
        loginLink.setOnAction(e -> context.getRenderer().navigate(NavIntent.open(Route.LOGIN)));
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
            .withOnSubmit(inputs -> {
                String username = inputs[0].getText();
                String password = inputs[1].getText();
                String result = context.getAuthService().register(username, password);
                if ("Successfully registered".equals(result)) {
                    // If auto-logged in by auth service
                    if (context.getAuthService().isLoggedIn()) {
                        context.getRenderer().navigate(NavIntent.loginSuccess());
                    } else {
                        context.getRenderer().setNotification("Registered successfully, please login", false);
                        context.getRenderer().navigate(NavIntent.open(Route.LOGIN));
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
