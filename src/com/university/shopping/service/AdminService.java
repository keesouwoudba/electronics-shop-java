package com.university.shopping.service;

import com.university.shopping.model.Product;
import com.university.shopping.model.User;
import com.university.shopping.repository.OrderRepository;
import com.university.shopping.repository.ProductRepository;
import com.university.shopping.repository.UserRepository;

public class AdminService {
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private AuthService authService;

    // Constructor
    public AdminService(ProductRepository productRepo, UserRepository userRepo, OrderRepository orderRepo, AuthService authService){
        this.productRepository = productRepo;
        this.userRepository = userRepo;
        this.orderRepository = orderRepo;
        this.authService = authService;
    }

    //----------------------------------------------------
    // Product Management Methods
    //----------------------------------------------------
/*
    public String addNewProduct() { //Returns: "SUCCESS", "NOT_ADMIN", "INVALID_DATA"
        if (!this.authService.isAdmin()) return "Not Admin";

    }
    public String addStockToExistingProduct(int productId, int quantityToAdd){

    }
    public String removeStock(int productId, int quantityToRemove){

    }
    public String deleteProduct(int productId){

    }
    public String updateProductPrice(int productId, double newPrice){

    }
    public String updateProductName(int productId, String newName){

    }
    public String setProductDiscount(int productId, boolean isDiscounted, double discountPercentage){

    }
    public Product[] getAllProducts()

    //----------------------------------------------------
    // User Management Methods:
    //----------------------------------------------------
    public String addUser(String username, String password, boolean isAdmin){

    }
    public String updateUser(int userId, String newUsername, String newPassword, boolean isAdmin){

    }
    public String deleteUser(int userId){

    } //Prevents self-deletion
    public User[] getAllUsers(){

    }

 */
}
