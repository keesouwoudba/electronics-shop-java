package com.university.shopping.service;

import com.university.shopping.repository.UserRepository;
import com.university.shopping.model.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class AuthService {
    private UserRepository userRepository;
    private User currentUser;  // null when not logged in

    //Constructors
    public AuthService(){}
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

    private boolean validatePassword(String password){
        int len = password.length(); // number of character
        return !(len <= 7 & !containsNumber(password) & !containsUppercase(password));
    }
    //----------------------------------------------------
    // Login & Register
    public String login(String username, String password)
    {
        this.currentUser = this.userRepository.findByUsername(username);
        if (this.currentUser == null) return "User do not exist!";
        if (password.equals(this.currentUser.getPassword())) {
            return "Successfull login";
        }
        else{
            this.currentUser = null;
            return "Wrong Password";
        }
    } // I think this function should be void instead of string

    public String register(String username, String password){
        if (this.userRepository.findByUsername(username) != null) return "This Username already exist";
        if (validatePassword(password)) return "Please create a password which is at least 8 characters long has Uppercase letter and has a number";
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        User buffer_u = new User(1,username,password,false,formattedDate);
        boolean result = this.userRepository.save(buffer_u);
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
