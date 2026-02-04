package com.university.shopping.service;

import com.university.shopping.model.Cart;
import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Order;
import com.university.shopping.model.Product;
import com.university.shopping.repository.CartRepository;
import com.university.shopping.repository.OrderRepository;
import com.university.shopping.repository.ProductRepository;

public class ShopService {
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private CartRepository cartRepository;
    private AuthService authService;

    public ShopService(ProductRepository productRepo, OrderRepository orderRepo, CartRepository cartRepo, AuthService authService)
    {

    }


    public Product[] getAllProducts(){
        return productRepository.findAll();
    } // Returns every possible product which is available.

    public Product getProductById(int productId){
        return productRepository.findById(productId);
    } // Returns Product which has specific ID

    public String addToCart(int productId, int quantity, int user_id){
        MockDatabase.carts[user_id].addItem();
    }

    public boolean removeFromCart(int productId){

    }

    public Cart viewCart(int userId){
        return cartRepository.findCartByUserId(userId);
    }

    public String checkout(int userId){
        if (!authService.isLoggedIn()) return "Please Log in";
        Cart checkout = cartRepository.findCartByUserId(userId);
        checkout.
    }

    /*
    1. Check authService.isLoggedIn()
        If false, return "NOT_LOGGED_IN"

    2. Get user's cart via cartRepository.findByUserId()

    3. Check cart.getItemCount() > 0
       If 0, return "EMPTY_CART"

    4. VALIDATION PHASE (check all items before modifying anything)
       For each item in cart.getItems()[0...itemCount-1]:
         - Fetch fresh Product from productRepository.findById()
         - Check if product still exists (not deleted)
         - Check if product.stockQuantity >= item.quantity
         - If ANY item fails, return "STOCK_ERROR:"

    5. STOCK DEDUCTION PHASE (only if all validations passed)
       For each item in cart:
         - productRepository.updateStock(item.productId, -item.quantity)

    6. ORDER CREATION PHASE
       - Generate orderId via orderRepository.getNextOrderId()
       - Get current timestamp
       - Calculate totalPrice via cart.getTotalPrice()
       - Copy cart items to new OrderItem[] array
       - Create Order object with status="COMPLETED"
       - orderRepository.save(order)

    7. CART CLEARING PHASE
       - cartRepository.clear(currentUser.getUserId())

    8. Return "SUCCESS"


    public Order[] getUserOrders(){

    }

}
