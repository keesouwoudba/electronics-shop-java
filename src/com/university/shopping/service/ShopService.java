package com.university.shopping.service;

import com.university.shopping.model.*;
import com.university.shopping.repository.CartRepository;
import com.university.shopping.repository.OrderRepository;
import com.university.shopping.repository.ProductRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ShopService {
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private CartRepository cartRepository;
    private AuthService authService;

    public ShopService(ProductRepository productRepo, OrderRepository orderRepo, CartRepository cartRepo, AuthService authService)
    {
        this.productRepository = productRepo;
        this.orderRepository = orderRepo;
        this.cartRepository = cartRepo;
        this.authService = authService;
    }


    public Product[] getAllProducts(){
        return productRepository.findAll();
    } // Returns every possible product which is available.

    public Product getProductById(int productId){
        return productRepository.findById(productId);
    } // Returns Product which has specific ID

    public boolean addToCart(int productId, int quantity, int userId){
        if (!authService.isLoggedIn()) return false;
        Cart temp_cart = cartRepository.findCartByUserId(userId);
        Product tmp_prod = productRepository.findById(productId);
        temp_cart.addItem(productId, tmp_prod.getName(), quantity, tmp_prod.getPrice());
        cartRepository.saveCart(temp_cart);
        return true;
        //need to add try-catch, and false
    }

    public boolean removeFromCart(int productId, int userId){
        if (!authService.isLoggedIn()) return false;
        cartRepository.findCartByUserId(userId).removeItem(productId);
        return true;
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
        for (OrderItem item : items){
            if (item.getQuantity() > productRepository.findById(item.getProductId()).getStockQuantity()) return "STOCK_ERROR: Stock's quantity is not enough for " + item.getProductName();
        }

        for (OrderItem item : items){
            productRepository.findById(item.getProductId()).setStockQuantity(
                    productRepository.findById(item.getProductId()).getStockQuantity() - item.getQuantity()
            );
        }
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        Order complete = new Order(userId,formattedDate,checkout.getTotalPrice(),"COMPLETED",items);
        orderRepository.save(complete);
        cartRepository.findCartByUserId(userId).clear();
        return "Success";
    }
}
