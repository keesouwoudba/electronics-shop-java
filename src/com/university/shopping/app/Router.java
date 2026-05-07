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
        boolean isAdmin = isLoggedIn && currentUser.isAdmin();
        boolean isCustomer = isLoggedIn && !currentUser.isAdmin();

        switch (intent.getType()) {
            case APP_START:
                if (!isLoggedIn) return new RouteDecision(Route.GUEST_PRODUCTS,true, null);
                return isAdmin ? new RouteDecision(Route.ADMIN_PRODUCTS,true,  null) : new RouteDecision(Route.CUSTOMER_PRODUCTS,true, null);

            case LOGIN_SUCCESS:
                return dispatch(NavIntent.appStart());

            case CHECKOUT_SUCCESS:
                return new RouteDecision(Route.CUSTOMER_PRODUCTS, true, "Order placed successfully!");

            case OPEN_PRODUCT_DETAILS:
                appState.setSelectedProductId(intent.getProductId());
                if (!isLoggedIn) return new RouteDecision(Route.LOGIN,true, "Please log in to view details and purchase.");
                return new RouteDecision(Route.CUSTOMER_PRODUCT_DETAILS,true, null);

            case OPEN_ROUTE:
                Route req = intent.getRequestedRoute();
                // Access Control Let's be explicit
                if (req == Route.LOGIN || req == Route.REGISTER || req == Route.GUEST_PRODUCTS) {
                    if (isLoggedIn) return dispatch(NavIntent.appStart()); // Already logged in
                    return new RouteDecision(req,true, null);
                }

                if (!isLoggedIn) {
                    return new RouteDecision(Route.LOGIN,true, "Please log in to access this page.");
                }

                if (req.name().startsWith("ADMIN_") && !isAdmin) { //to correct
                    return new RouteDecision(Route.CUSTOMER_PRODUCTS,true, "Unauthorized access.");
                }

                if (req.name().startsWith("CUSTOMER_") && !isCustomer) { //to correct
                    return new RouteDecision(Route.ADMIN_PRODUCTS,true, "Unauthorized access.");
                }

                return new RouteDecision(req,true, null);

            default:
                return new RouteDecision(Route.GUEST_PRODUCTS,true, null);
        }
    }
}
