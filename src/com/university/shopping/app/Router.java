package com.university.shopping.app;

import com.university.shopping.model.User;

public class Router {
    private final AppState appState;

    public Router(AppState appState) {
        this.appState = appState;
    }

    public RouteDecision dispatch(NavIntent intent) {
        User currentUser = appState.getCurrentUser();
        boolean isLoggedIn = currentUser != null;
        boolean isAdmin = isLoggedIn && currentUser.getRole() == User.Role.ADMIN;
        boolean isCustomer = isLoggedIn && currentUser.getRole() == User.Role.CUSTOMER;

        switch (intent.getType()) {
            case APP_START:
                if (!isLoggedIn) return new RouteDecision(Route.GUEST_PRODUCTS, null);
                return isAdmin ? new RouteDecision(Route.ADMIN_PRODUCTS, null) : new RouteDecision(Route.CUSTOMER_PRODUCTS, null);

            case LOGIN_SUCCESS:
                return dispatch(NavIntent.appStart());

            case CHECKOUT_SUCCESS:
                return new RouteDecision(Route.CUSTOMER_PRODUCTS, "Order placed successfully!");

            case OPEN_PRODUCT_DETAILS:
                appState.setSelectedProductId(intent.getProductId());
                if (!isLoggedIn) return new RouteDecision(Route.LOGIN, "Please log in to view details and purchase.");
                return new RouteDecision(Route.CUSTOMER_PRODUCT_DETAILS, null);

            case OPEN_ROUTE:
                Route req = intent.getRequestedRoute();
                // Access Control Let's be explicit
                if (req == Route.LOGIN || req == Route.REGISTER || req == Route.GUEST_PRODUCTS) {
                    if (isLoggedIn) return dispatch(NavIntent.appStart()); // Already logged in
                    return new RouteDecision(req, null);
                }

                if (!isLoggedIn) {
                    return new RouteDecision(Route.LOGIN, "Please log in to access this page.");
                }

                if (req.name().startsWith("ADMIN_") && !isAdmin) {
                    return new RouteDecision(Route.CUSTOMER_PRODUCTS, "Unauthorized access.");
                }

                if (req.name().startsWith("CUSTOMER_") && !isCustomer) {
                    return new RouteDecision(Route.ADMIN_PRODUCTS, "Unauthorized access.");
                }

                return new RouteDecision(req, null);

            default:
                return new RouteDecision(Route.GUEST_PRODUCTS, null);
        }
    }
}
