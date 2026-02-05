package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Order;

public class OrderRepository {
    public boolean save(Order order) {
        boolean result =  false;
        if (order == null) {
            return result;
        }
        for (int i = 0; i < MockDatabase.orderCount; i++){
            if (MockDatabase.orders[i].getOrderId() ==  order.getOrderId()) {
                MockDatabase.orders[i] = order;
                return true;
            }
        }
        MockDatabase.orders[MockDatabase.orderCount] = order;
        MockDatabase.orderCount++;
        result = true;
        return result;
    }

    public Order findOrderById(int orderId) {
        if (!(orderId > MockDatabase.orderCount) && !(orderId < 0)){
            for (int i = 0; i < MockDatabase.orderCount; i++){
                if (MockDatabase.orders[i].getOrderId() == orderId){
                    return MockDatabase.orders[i];
                }
            }
        }
        return null;
    }
    public Order[] findAllByUserId(int userId) {
        Order[] temp1 = new Order[MockDatabase.orderCount];
        int count = 0;
        if (userId >= 0 && userId < MockDatabase.userCount){
            for (int i = 0; i < MockDatabase.orderCount;  i++){
                if (MockDatabase.orders[i].getUserId() == userId){
                    temp1[count++] = MockDatabase.orders[i];
                }
            }
            Order[] final1 = new Order[count];
            for (int i = 0; i < count; i++){
                if (temp1[i] != null){
                    final1[i] = temp1[i];
                }

            }
            return final1;
        }
        return null;
    }
    public Order[] findAll(){
        return MockDatabase.orders;
    }
    public int getNextOrderId() {
        return MockDatabase.nextOrderId;
    }
    public int getUserCount() {
        return MockDatabase.userCount;
    }
    public int getProductCount() {
        return MockDatabase.productCount;
    }
}
