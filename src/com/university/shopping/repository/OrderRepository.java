package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Order;
import com.university.shopping.model.OrderItem;

public class OrderRepository {

    public boolean save(Order order) {
        if (order == null) return false;

        Order[] snapshot = snapshotOrders();
        int snapshotCount = MockDatabase.orderCount;

        for (int i = 0; i < MockDatabase.orderCount; i++) {
            if (MockDatabase.orders[i] != null &&
                    MockDatabase.orders[i].getOrderId() == order.getOrderId()) {
                MockDatabase.orders[i] = order;

                if (!persistOrdersAndItems()) {
                    restoreSnapshot(snapshot, snapshotCount);
                    return false;
                }

                return true;
            }
        }

        if (MockDatabase.orderCount >= MockDatabase.orders.length) return false;

        MockDatabase.orders[MockDatabase.orderCount++] = order;

        if (!persistOrdersAndItems()) {
            restoreSnapshot(snapshot, snapshotCount);
            return false;
        }

        return true;
    }

    public boolean update(Order order) {
        if (order == null) return false;

        Order[] snapshot = snapshotOrders();
        int snapshotCount = MockDatabase.orderCount;

        for (int i = 0; i < MockDatabase.orderCount; i++) {
            if (MockDatabase.orders[i] != null &&
                    MockDatabase.orders[i].getOrderId() == order.getOrderId()) {
                MockDatabase.orders[i] = order;

                if (!persistOrdersAndItems()) {
                    restoreSnapshot(snapshot, snapshotCount);
                    return false;
                }

                return true;
            }
        }
        return false;
    }

    public boolean deleteById(int orderId) {
        Order[] snapshot = snapshotOrders();
        int snapshotCount = MockDatabase.orderCount;

        for (int i = 0; i < MockDatabase.orderCount; i++) {
            if (MockDatabase.orders[i] != null &&
                    MockDatabase.orders[i].getOrderId() == orderId) {
                for (int j = i; j < MockDatabase.orderCount - 1; j++) {
                    MockDatabase.orders[j] = MockDatabase.orders[j + 1];
                }
                MockDatabase.orderCount--;
                MockDatabase.orders[MockDatabase.orderCount] = null;

                if (!persistOrdersAndItems()) {
                    restoreSnapshot(snapshot, snapshotCount);
                    return false;
                }

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

    private boolean persistOrdersAndItems() {
        boolean ordersSaved = CsvPersistenceUtil.writeOrdersToCsv();
        boolean itemsSaved = ordersSaved && CsvPersistenceUtil.writeOrderItemsToCsv();
        return ordersSaved && itemsSaved;
    }

    private Order[] snapshotOrders() {
        Order[] snapshot = new Order[MockDatabase.orders.length];
        System.arraycopy(MockDatabase.orders, 0, snapshot, 0, MockDatabase.orders.length);
        return snapshot;
    }

    private void restoreSnapshot(Order[] snapshot, int snapshotCount) {
        System.arraycopy(snapshot, 0, MockDatabase.orders, 0, MockDatabase.orders.length);
        MockDatabase.orderCount = snapshotCount;
    }
}
