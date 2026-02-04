package com.university.shopping.model;


public class User {
    private final int id;
    private String username;
    private String password;
    private boolean isAdmin;
    private final String createdDate;

    public User(String username, String password, boolean isAdmin, String createdDate) {
        this.id = MockDatabase.nextUserId;
        MockDatabase.nextUserId++;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.createdDate = createdDate;
    }
    //getters
    public int getUserId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public String getCreatedDate() {
        return createdDate;
    }
    //setters

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }




}
