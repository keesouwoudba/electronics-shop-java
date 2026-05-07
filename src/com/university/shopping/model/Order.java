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
        //difference is that the second is based on order id.
    public Order(int orderId, int userId, String orderDate, double totalPrice, String status, OrderItem[] items){
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.items = items;
        if (orderId >= MockDatabase.nextOrderId) {
            MockDatabase.nextOrderId = orderId + 1;
        }
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

    public void setItems(OrderItem[] items) {
        this.items = items;
    }
}
