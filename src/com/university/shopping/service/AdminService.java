package com.university.shopping.service;

import com.university.shopping.model.Product;
import com.university.shopping.model.User;
import com.university.shopping.repository.OrderRepository;
import com.university.shopping.repository.ProductRepository;
import com.university.shopping.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public String addNewProduct(Product product) { //Returns: "SUCCESS", "NOT_ADMIN", "INVALID_DATA"
        if (!this.authService.isAdmin()) return "NOT_ADMIN";
        productRepository.save(product);
        return "SUCCESS";
    }
    public String addStockToExistingProduct(int productId, int quantityToAdd){
        if (!this.authService.isAdmin()) return "Not Admin";
        if (productRepository.findById(productId) == null) return "Product not exists";

        productRepository.updateStock(productId,
                productRepository.findById(productId).getStockQuantity() + quantityToAdd
        );
        return "SUCCESS";
    }
    public String removeStock(int productId, int quantityToRemove){
        if (!this.authService.isAdmin()) return "Not Admin";
        if (productRepository.findById(productId) == null) return "Product not exists";

        if (productRepository.findById(productId).getStockQuantity() < quantityToRemove){
            productRepository.updateStock(productId, 0);
        }
        else {
            productRepository.updateStock(productId,
                    productRepository.findById(productId).getStockQuantity() - quantityToRemove
            );
        }
        return "SUCCESS";
    }
    public String deleteProduct(int productId){
        if (!this.authService.isAdmin()) return "Not Admin";
        if (productRepository.findById(productId) == null) return "Product not exists";

        productRepository.deleteById(productId);
        return "SUCCESS";
    }

    public String updateProductPrice(int productId, double newPrice){
        if (!this.authService.isAdmin()) return "Not Admin";
        if (productRepository.findById(productId) == null) return "Product not exists";

        Product tmp_prod = productRepository.findById(productId);
        tmp_prod.setPrice(newPrice);
        productRepository.update(tmp_prod);
        return "SUCCESS";
    }

    public String updateProductName(int productId, String newName){
        if (!this.authService.isAdmin()) return "Not Admin";
        if (productRepository.findById(productId) == null) return "Product not exists";

        Product tmp_prod = productRepository.findById(productId);
        tmp_prod.setName(newName);
        productRepository.update(tmp_prod);
        return "SUCCESS";
    }

    public String setProductDiscount(int productId, boolean isDiscounted, double discountPercentage){
        if (!this.authService.isAdmin()) return "Not Admin";
        if (productRepository.findById(productId) == null) return "Product not exists";

        Product tmp_prod = productRepository.findById(productId);
        tmp_prod.setIsDiscounted(true);
        tmp_prod.setDiscountPercentage(discountPercentage);
        productRepository.update(tmp_prod);
        return "SUCCESS";
    }
    public Product[] getAllProducts(){
        if (!this.authService.isAdmin()) return null;
        return productRepository.findAll();
    }

    //----------------------------------------------------
    // User Management Methods:
    //----------------------------------------------------
    public String addUser(String username, String password, boolean isAdmin){
        if (!this.authService.isAdmin()) return "Not Admin";
        if (!authService.validatePassword(password)) return "Create  better Password";
        if (userRepository.findByUsername(username)!=null) return "This Username already exists!";

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        User tmp_user = new User(username, password, isAdmin, formattedDate);
        userRepository.save(tmp_user);
        return "SUCCESS";
    }
    public String updateUser(int userId, String newUsername, String newPassword, boolean isAdmin){
        if (!this.authService.isAdmin()) return "Not Admin";
        if (!authService.validatePassword(newPassword)) return "Create  better Password";
        if (userRepository.findByUsername(newUsername)!=null) return "This Username already exists!";

        User tmp_user = userRepository.findById(userId);
        tmp_user.setUsername(newUsername);
        tmp_user.setPassword(newPassword);
        tmp_user.setIsAdmin(isAdmin);
        userRepository.update(tmp_user);
        return "SUCCESS";
    }
    public String deleteUser(int userId){
        if (!this.authService.isAdmin()) return "Not Admin";
        if (userRepository.findById(userId)==null) return "This User doesn't exist!";
        if (userRepository.findById(userId).getUsername().equals(authService.getCurrentUser().getUsername()))
            return "Unable to delete yourself";
        userRepository.delete(userRepository.findById(userId));
        return "SUCCESS";
    } //Prevents self-deletion


    public User[] getAllUsers(){
        if (!this.authService.isAdmin()) return null;
        return userRepository.getAllUsers();
    }


}
