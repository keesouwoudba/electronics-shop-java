package com.university.shopping.service;

import com.university.shopping.repository.UserRepository;
import com.university.shopping.model.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class AuthService {
    private UserRepository userRepository;
    private User currentUser;  // null when not logged in

    //Constructors
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentUser = null;
    }

    // Password validation
    private boolean containsNumber(String input) {
        return input.matches(".*\\d.*");
    }

    private boolean containsUppercase(String input) {
        return input.matches(".*[A-Z].*");
    }

    public boolean validatePassword(String password){
        if (password == null) return false;
        int len = password.length();
        return len >= 8 && containsNumber(password) && containsUppercase(password);
    }
    //----------------------------------------------------



    // Login & Register
    public String login(String username, String password)
    {
        User user = this.userRepository.findByUsername(username);
        if (user == null) return "User do not exist!";
        if (password.equals(user.getPassword())) {
            this.currentUser = user;
            return "Successfull login";
        }
        else{
            this.currentUser = null;
            return "Wrong Password";
        }
    } // I think this function should be void instead of string

    public String register(String username, String password){
        if (this.userRepository.findByUsername(username) != null) return "This Username already exist";
        if (!validatePassword(password)) return "Please create a password which is at least 8 characters long has Uppercase letter and has a number";
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        User buffer_u = new User(username,password,false,formattedDate);
        // The user is already saved by the constructor in the current implementation
        return "Successfully registered";
    }
    //----------------------------------------------------

    public void logout(){
        this.currentUser = null;
    }


    public User getCurrentUser()
    {
        return this.currentUser;
    }

    public boolean isLoggedIn(){
        return this.currentUser != null;
    }

    public boolean isAdmin(){
        if (isLoggedIn()) return this.currentUser.isAdmin();
        else return false;
    }



}
