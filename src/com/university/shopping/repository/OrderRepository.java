package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Order;
import com.university.shopping.model.OrderItem;

public class OrderRepository {

    public boolean save(Order order) {
        if (order == null) return false;

        for (int i = 0; i < MockDatabase.orderCount; i++) {
            if (MockDatabase.orders[i] != null &&
                    MockDatabase.orders[i].getOrderId() == order.getOrderId()) {
                MockDatabase.orders[i] = order;
                CsvPersistenceUtil.writeOrdersToCsv();
                CsvPersistenceUtil.writeOrderItemsToCsv();
                return true;
            }
        }

        if (MockDatabase.orderCount >= MockDatabase.orders.length) return false;

        MockDatabase.orders[MockDatabase.orderCount++] = order;
        CsvPersistenceUtil.writeOrdersToCsv();
        CsvPersistenceUtil.writeOrderItemsToCsv();
        return true;
    }

    public boolean update(Order order) {
        if (order == null) return false;

        for (int i = 0; i < MockDatabase.orderCount; i++) {
            if (MockDatabase.orders[i] != null &&
                    MockDatabase.orders[i].getOrderId() == order.getOrderId()) {
                MockDatabase.orders[i] = order;
                CsvPersistenceUtil.writeOrdersToCsv();
                CsvPersistenceUtil.writeOrderItemsToCsv();
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(int orderId) {
        for (int i = 0; i < MockDatabase.orderCount; i++) {
            if (MockDatabase.orders[i] != null &&
                    MockDatabase.orders[i].getOrderId() == orderId) {
                for (int j = i; j < MockDatabase.orderCount - 1; j++) {
                    MockDatabase.orders[j] = MockDatabase.orders[j + 1];
                }
                MockDatabase.orderCount--;
                MockDatabase.orders[MockDatabase.orderCount] = null;

                CsvPersistenceUtil.writeOrdersToCsv();
                CsvPersistenceUtil.writeOrderItemsToCsv();
                return true;
            }
        }
        return false;
    }

    public Order findOrderById(int orderId) {
        for (int i = 0; i < MockDatabase.orderCount; i++) {
            if (MockDatabase.orders[i] != null &&
                    MockDatabase.orders[i].getOrderId() == orderId) {
                return MockDatabase.orders[i];
            }
        }
        return null;
    }

    public Order[] findAllByUserId(int userId) {
        Order[] temp = new Order[MockDatabase.orderCount];
        int count = 0;

        for (int i = 0; i < MockDatabase.orderCount; i++) {
            Order order = MockDatabase.orders[i];
            if (order != null && order.getUserId() == userId) {
                temp[count++] = order;
            }
        }

        Order[] result = new Order[count];
        for (int i = 0; i < count; i++) {
            result[i] = temp[i];
        }
        return result;
    }

    public Order[] findAll() {
        Order[] result = new Order[MockDatabase.orderCount];
        for (int i = 0; i < MockDatabase.orderCount; i++) {
            result[i] = MockDatabase.orders[i];
        }
        return result;
    }

    public int getOrderCount() {
        return MockDatabase.orderCount;
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
