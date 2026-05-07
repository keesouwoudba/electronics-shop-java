package com.university.shopping.app;

public class NavIntent {
    private final NavIntentType type;
    private final Route requestedRoute;
    private final Integer productId;
    private final String message;

    private NavIntent(NavIntentType type, Route requestedRoute, Integer productId, String message) {
        this.type = type;
        this.requestedRoute = requestedRoute;
        this.productId = productId;
        this.message = message;
    }

    public static NavIntent appStart() {
        return new NavIntent(NavIntentType.APP_START, null, null, null);
    }

    public static NavIntent open(Route route) {
        return new NavIntent(NavIntentType.OPEN_ROUTE, route, null, null);
    }

    public static NavIntent openProduct(int productId) {
        return new NavIntent(NavIntentType.OPEN_PRODUCT_DETAILS, null, productId, null);
    }

    public static NavIntent loginSuccess() {
        return new NavIntent(NavIntentType.LOGIN_SUCCESS, null, null, null);
    }
    
    public static NavIntent checkoutSuccess() {
        return new NavIntent(NavIntentType.CHECKOUT_SUCCESS, null, null, null);
    }

    public NavIntentType getType() { return type; }
    public Route getRequestedRoute() { return requestedRoute; }
    public Integer getProductId() { return productId; }
    public String getMessage() { return message; }
}
