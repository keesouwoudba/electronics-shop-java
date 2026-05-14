package com.university.shopping.app;

public class RouteDecision {
    private final Route route;
    private final String reason;
    private final boolean allowed;

    public RouteDecision(Route route, boolean allowed, String reason) {
        this.route = route;
        this.allowed = allowed;
        this.reason = reason;
    }

    public Route getRoute() { return route; }
    public String getReason() { return reason; }
    public String getFlashMessage() { return reason; }
    public boolean isAllowed() { return allowed; }
}
