package com.university.shopping.model;

public class MockDatabase {
    public static User[] users = new User[100];
    public static int userCount = 0; //total number of users
    public static int nextUserId = 1; //when new user is added, this id will be assigned to him and he will increment by one this after

    public static Product[] products = new Product[200];
    public static int productCount = 0;
    public static int nextProductId = 1;

    public static Order[] orders = new Order[500];
    public static int orderCount = 0;
    public static int nextOrderId = 1;

    public static Cart[] carts = new Cart[100];
    public static int cartCount = 0;






    private MockDatabase() {}
}
