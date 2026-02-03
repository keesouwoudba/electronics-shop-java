package com.university.shopping.model;


public class Product {
    private final int productId;
    private String name;
    private double price;
    private String category;
    private String description;
    private int stockQuantity;
    private boolean isDiscounted;
    private double discountPercentage; //0-100

    public Product(int productId, String name, double price, String category, String description, int stockQuantity, boolean isDiscounted, double discountPercentage) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.isDiscounted = isDiscounted;
        this.discountPercentage = discountPercentage;

    }

    //getters
    public int getProductId() {
        return productId;
    }
    public String getName(){
        return name;
    }
    public double getPrice(){
        return price;
    }
    public String getCategory(){
        return category;
    }
    public String getDescription(){
        return description;
    }
    public int getStockQuantity(){
        return stockQuantity;
    }
    public boolean isDiscounted(){
        return isDiscounted;
    }
    public double getDiscountPercentage(){
        return discountPercentage;
    }
    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setStockQuantity(int stockQuantity){
        this.stockQuantity = stockQuantity;
    }
    public void setIsDiscounted(boolean isDiscounted){
        this.isDiscounted = isDiscounted;
    }
    public void setDiscountPercentage(double discountPercentage){
        this.discountPercentage = discountPercentage;
    }

    //methods
    public double getFinalPrice(){
        return isDiscounted ? price * (1 - Math.max(0, Math.min(discountPercentage, 100)) / 100.0) : price;
    }



}

