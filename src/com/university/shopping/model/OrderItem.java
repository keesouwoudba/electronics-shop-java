package com.university.shopping.model;


public class OrderItem {
    private final int productId;
    private final String productName;
    private int quantity;
    private final double priceAtPurchase;

    public OrderItem(int productId, String productName, int quantity, double priceAtPurchase){
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }
    //getters
    public int getProductId(){
        return this.productId;
    }
    public String getProductName(){
        return this.productName;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public double getPriceAtPurchase(){
        return this.priceAtPurchase;
    }

    //setter

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void addQuantity(int quantity){
        this.quantity += quantity;
    }
}
