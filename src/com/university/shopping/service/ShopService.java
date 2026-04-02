package com.university.shopping.service;

import com.university.shopping.model.*;
import com.university.shopping.repository.CartRepository;
import com.university.shopping.repository.OrderRepository;
import com.university.shopping.repository.ProductRepository;
import com.university.shopping.service.pricing.DiscountPolicy;
import com.university.shopping.service.pricing.PricingModeConstants;
import com.university.shopping.service.pricing.StandardDiscountPolicy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ShopService {
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private CartRepository cartRepository;
    private AuthService authService;
    private DiscountPolicy discountPolicy;
    private String pricingMode;

    public ShopService(ProductRepository productRepo, OrderRepository orderRepo, CartRepository cartRepo, AuthService authService)
    {
        this(productRepo, orderRepo, cartRepo, authService, new StandardDiscountPolicy());
    }

    public ShopService(ProductRepository productRepo, OrderRepository orderRepo, CartRepository cartRepo, AuthService authService,
                       DiscountPolicy discountPolicy)
    {
        this.productRepository = productRepo;
        this.orderRepository = orderRepo;
        this.cartRepository = cartRepo;
        this.authService = authService;
        this.discountPolicy = discountPolicy == null ? new StandardDiscountPolicy() : discountPolicy;
        this.pricingMode = PricingModeConstants.LOCK_AT_ADD;
    }


    public Product[] getAllProducts(){
        return productRepository.findAll();
    } // Returns every possible product which is available.

    public Product getProductById(int productId){
        return productRepository.findById(productId);
    } // Returns Product which has specific ID

    public boolean addToCart(int productId, int quantity, int userId){
        if (!authService.isLoggedIn()) return false;
        if (quantity <= 0) return false;

        Product tmp_prod = productRepository.findById(productId);
        if (tmp_prod == null) return false;
        if (quantity > tmp_prod.getStockQuantity()) return false;

        Cart temp_cart = cartRepository.findCartByUserId(userId);
        double priceAtAdd = discountPolicy.apply(tmp_prod);
        temp_cart.addItem(productId, tmp_prod.getName(), quantity, priceAtAdd);
        cartRepository.saveCart(temp_cart);
        return true;
    }

    public boolean removeFromCart(int productId, int userId){
        if (!authService.isLoggedIn()) return false;
        return cartRepository.findCartByUserId(userId).removeItem(productId);
    }

    public Cart viewCart(int userId){
        if (!authService.isLoggedIn()) return null;
        return cartRepository.findCartByUserId(userId);
    }

    public String checkout(int userId){
        if (!authService.isLoggedIn()) return "Please Log in";
        Cart checkout = cartRepository.findCartByUserId(userId);
        if (checkout.getItemCount() < 1){
            return "Please add items to your cart";
        }
        OrderItem[] items = checkout.getItems();

        // Validate stock for all items
        for (int i = 0; i < checkout.getItemCount(); i++){
            OrderItem item = items[i];
            if (item != null){
                Product product = productRepository.findById(item.getProductId());
                if (product == null) return "PRODUCT_ERROR: Product " + item.getProductName() + " not found";
                if (item.getQuantity() > product.getStockQuantity()) {
                    return "STOCK_ERROR: Stock's quantity is not enough for " + item.getProductName();
                }
            }
        }

        OrderItem[] purchasedItems = new OrderItem[checkout.getItemCount()];
        int purchaseCount = 0;
        double total = 0;

        for (int i = 0; i < checkout.getItemCount(); i++) {
            OrderItem item = items[i];
            if (item != null) {
                Product product = productRepository.findById(item.getProductId());
                if (product == null) {
                    return "PRODUCT_ERROR: Product " + item.getProductName() + " not found";
                }

                double finalPrice = PricingModeConstants.RECALCULATE_AT_CHECKOUT.equals(pricingMode)
                        ? discountPolicy.apply(product)
                        : item.getPriceAtPurchase();

                purchasedItems[purchaseCount] = new OrderItem(
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity(),
                        finalPrice
                );
                purchaseCount++;
                total += finalPrice * item.getQuantity();
            }
        }

        // Update stock for all items
        for (int i = 0; i < checkout.getItemCount(); i++){
            OrderItem item = items[i];
            if (item != null){
                Product product = productRepository.findById(item.getProductId());
                product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            }
        }

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        Order complete = new Order(userId, formattedDate, total, "COMPLETED", purchasedItems);
        orderRepository.save(complete);
        cartRepository.findCartByUserId(userId).clear();
        return "Success";
    }

    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        if (discountPolicy != null) {
            this.discountPolicy = discountPolicy;
        }
    }

    public void setPricingMode(String pricingMode) {
        if (pricingMode == null) {
            return;
        }
        String normalized = pricingMode.trim().toUpperCase();
        if (PricingModeConstants.LOCK_AT_ADD.equals(normalized)
            || PricingModeConstants.RECALCULATE_AT_CHECKOUT.equals(normalized)) {
            this.pricingMode = normalized;
        }
    }
}
