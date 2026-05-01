package com.university.shopping.view.contracts;

import com.university.shopping.app.AppState;
import com.university.shopping.app.Router;
import com.university.shopping.service.AdminService;
import com.university.shopping.service.AuthService;
import com.university.shopping.service.ShopService;
import com.university.shopping.service.media.ProductImageService;
import com.university.shopping.view.renderer.UiRenderer;

public class ScreenContext {
    private final AppState appState;
    private final Router router;
    private final UiRenderer renderer;
    private final AuthService authService;
    private final ShopService shopService;
    private final AdminService adminService;
    private final ProductImageService productImageService;

    public ScreenContext(AppState appState, Router router, UiRenderer renderer,
                         AuthService authService, ShopService shopService,
                         AdminService adminService, ProductImageService productImageService) {
        this.appState = appState;
        this.router = router;
        this.renderer = renderer;
        this.authService = authService;
        this.shopService = shopService;
        this.adminService = adminService;
        this.productImageService = productImageService;
    }

    public AppState getAppState() { return appState; }
    public Router getRouter() { return router; }
    public UiRenderer getRenderer() { return renderer; }
    public AuthService getAuthService() { return authService; }
    public ShopService getShopService() { return shopService; }
    public AdminService getAdminService() { return adminService; }
    public ProductImageService getProductImageService() { return productImageService; }
}
