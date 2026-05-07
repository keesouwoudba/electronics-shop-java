package com.university.shopping.app;

import com.university.shopping.model.User;

public class AppState {
    private User currentUser;
    private Route currentRoute;
    private Integer selectedProductId;
    private Integer selectedUserId;
    private String flashMessage;
    private boolean loading;

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }

    public Route getCurrentRoute() { return currentRoute; }
    public void setCurrentRoute(Route route) { this.currentRoute = route; }

    public Integer getSelectedProductId() { return selectedProductId; }
    public void setSelectedProductId(Integer id) { this.selectedProductId = id; }

    public Integer getSelectedUserId() { return selectedUserId; }
    public void setSelectedUserId(Integer id) { this.selectedUserId = id; }

    public String consumeFlashMessage() {
        String message = this.flashMessage;
        this.flashMessage = null;
        return message;
    }
    public void setFlashMessage(String flashMessage) { this.flashMessage = flashMessage; }

    public boolean isLoading() { return loading; }
    public void setLoading(boolean loading) { this.loading = loading; }
}
