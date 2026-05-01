package com.university.shopping.view.renderer;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.app.RouteDecision;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.layout.AppShell;
import com.university.shopping.view.pages.*;
import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.util.Duration;

public class UiRenderer {

    private final ScreenContext context;
    private final AppShell shell;

    public UiRenderer(ScreenContext context, AppShell shell) {
        this.context = context;
        this.shell = shell;
    }

    public void initialize(Scene scene) {
        scene.setRoot(shell.getRoot());
        shell.updateHeader(context);
        navigate(NavIntent.appStart());
    }

    public void navigate(NavIntent intent) {
        RouteDecision decision = context.getRouter().dispatch(intent);
        
        if (decision.getFlashMessage() != null) {
            setNotification(decision.getFlashMessage(), false); // Defaulting to info for routing flash
        }

        Route route = decision.getRoute();
        Node content = null;

        switch (route) {
            case GUEST_PRODUCTS:
                content = new GuestProductsPage().render(context);
                break;
            case LOGIN:
                content = new LoginPage().render(context);
                break;
            case REGISTER:
                content = new RegisterPage().render(context);
                break;
            case CUSTOMER_PRODUCTS:
                content = new CustomerProductsPage().render(context);
                break;
            case CUSTOMER_PRODUCT_DETAILS:
                content = new ProductDetailsPage().render(context);
                break;
            case CUSTOMER_CART:
                content = new CartPage().render(context);
                break;
            case CUSTOMER_CHECKOUT:
                content = new CheckoutPage().render(context);
                break;
            case ADMIN_PRODUCTS:
                content = new AdminProductsPage().render(context);
                break;
            case ADMIN_PRODUCT_EDITOR:
                content = new AdminProductEditorPage().render(context);
                break;
            case ADMIN_USERS:
                content = new AdminUsersPage().render(context);
                break;
            case ADMIN_REPORTS:
                content = new AdminReportsPage().render(context);
                break;
            default:
                content = new GuestProductsPage().render(context); // Fallback
        }

        // Always update header upon navigation incase user state changed
        shell.updateHeader(context);
        shell.setContent(content);
    }

    public void setNotification(String message, boolean isError) {
        shell.showNotification(message, isError);
        
        // Auto-hide after 3 seconds
        PauseTransition pt = new PauseTransition(Duration.seconds(3));
        pt.setOnFinished(e -> shell.hideNotification());
        pt.play();
    }
}
