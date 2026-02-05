package com.university.shopping.model;

public class Order {
    private final int orderId;
    private final int userId;
    private final String orderDate;
    private double totalPrice;
    private String status;
    private OrderItem[] items;

    public Order(int userId, String orderDate, double totalPrice, String status, OrderItem[] items){
        this.orderId = MockDatabase.nextOrderId;
        MockDatabase.nextOrderId++;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.items = items;
    }
    public int getOrderId(){
        return orderId;
    }
    public int getUserId(){
        return userId;
    }
    public String getOrderDate(){
        return orderDate;
    }
    public double getTotalPrice(){
        return totalPrice;
    }
    public String getStatus(){
        return status;
    }
    public OrderItem[] getItems(){
        return items;
    }
    //setters

    public void setStatus(String status){
        this.status = status;
    }
}
