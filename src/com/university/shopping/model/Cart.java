package com.university.shopping.model;

import java.util.Arrays;

public class Cart {
    private int userId;
    private OrderItem[] items;
    private int itemCount;
    private int foundIndexR;
    private int foundIndex;

    public Cart(int userId) {
        this.userId = userId;
        this.items = new OrderItem[50];
        this.itemCount = 0;
    }
    //methods

    public void addItem(int productId, String productName, int quantity, double price){
        boolean exists = false;

        for (int i = 0; i < itemCount; i++) {
            if (items[i] != null && productId == items[i].getProductId() && productName.equals(items[i].getProductName())) {
                foundIndex = i;
                exists = true;
            }
        }
        if (exists){
            items[foundIndex].addQuantity(quantity);
        }
        else {
            //add verification if item exists in database(i dont know should i add or not)
            items[itemCount++] = new OrderItem(productId, productName, quantity, price);
        }
    }
    public boolean removeItem(int productId){
        boolean exists = false;
        for (int i = 0; i < itemCount; i++) {
            if (items[i] != null && productId == items[i].getProductId()) {
                foundIndexR = i;
                exists = true;
                break;
            }
        }
        if (exists){
            for (int i =  foundIndexR; i < itemCount -1; i++){
                items[i] = items[i+1];
            }
            items[itemCount-1] = null;
            itemCount--;
            return true;
        }
        else {
            return false;
        }
    }
    public void clear(){
        for (int i = 0; i < itemCount; i++){
            items[i] = null;
        }
        itemCount = 0;
    }
    public double getTotalPrice(){
        double totalPrice = 0;
        for (int i = 0; i < itemCount; i++){
            if (items[i] != null){
                double tempPrice = (int) items[i].getPriceAtPurchase();
                double tempQuantity = (int) items[i].getQuantity();
                double price = tempPrice*tempQuantity;
                totalPrice += price;
            }
        }
        return totalPrice;
    }








    //getters
    public int getUserId(){
        return this.userId;
    }
    public OrderItem[] getItems(){
        return items;
    }
    public int getItemCount(){
        return this.itemCount;
    }
}
