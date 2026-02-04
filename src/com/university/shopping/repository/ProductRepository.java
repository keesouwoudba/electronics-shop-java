package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Product;

public class ProductRepository {
    static {
        MockDatabase.products[0] = new Product(1, "iPhone 15 Pro", 1199.99,
                "Phone", "Latest Apple flagship with A17 Pro chip", 25, false, 0);
        MockDatabase.products[1] = new Product(2, "Samsung Galaxy S24", 999.99,
                "Phone", "Android flagship with AI features", 30, true, 10);
        MockDatabase.products[2] = new Product(3, "MacBook Pro M3", 2499.99,
                "Laptop", "16-inch professional laptop", 15, false, 0);
        MockDatabase.products[3] = new Product(4, "Dell XPS 15", 1799.99,
                "Laptop", "Windows ultrabook", 20, true, 5);
        MockDatabase.products[4] = new Product(5, "Sony WH-1000XM5", 399.99,
                "Accessory", "Noise-cancelling headphones", 50, false, 0);
        MockDatabase.products[5] = new Product(6, "AirPods Pro 2", 249.99,
                "Accessory", "Apple wireless earbuds", 60, false, 0);
        MockDatabase.products[6] = new Product(7, "iPad Air M2", 599.99,
                "Tablet", "10.9-inch tablet", 40, true, 8);
        MockDatabase.products[7] = new Product(8, "Samsung Galaxy Tab S9", 799.99,
                "Tablet", "11-inch AMOLED display", 35, false, 0);
        MockDatabase.products[8] = new Product(9, "Logitech MX Master 3S", 99.99,
                "Accessory", "Premium wireless mouse", 80, false, 0);
        MockDatabase.products[9] = new Product(10, "Apple Magic Keyboard", 149.99,
                "Accessory", "Wireless keyboard", 45, false, 0);
        MockDatabase.products[10] = new Product(11, "LG UltraWide Monitor", 499.99,
                "Accessory", "34-inch curved display", 20, true, 12);
        MockDatabase.products[11] = new Product(12, "Bose QuietComfort 45", 329.99,
                "Accessory", "Wireless headphones", 40, false, 0);
        MockDatabase.products[12] = new Product(13, "Google Pixel 8 Pro", 899.99,
                "Phone", "Google flagship phone", 28, true, 15);
        MockDatabase.products[13] = new Product(14, "ASUS ROG Gaming Laptop", 1999.99,
                "Laptop", "RTX 4070 gaming laptop", 12, false, 0);
        MockDatabase.products[14] = new Product(15, "Apple Watch Series 9", 399.99,
                "Accessory", "Smartwatch with health tracking", 55, false, 0);

        MockDatabase.productCount = 15;
        MockDatabase.nextProductId = 16;
    }

    public Product findById(int id) {
        for (int i = 0; i < MockDatabase.productCount; i++)
            if (MockDatabase.products[i].getProductId() == id) return MockDatabase.products[i];
        return null;
    }
    public Product findByName(String name) {
        for (int i = 0; i < MockDatabase.productCount; i++)
            if (MockDatabase.products[i].getName().equals(name)) return MockDatabase.products[i];
        return null;
    }
    public Product[] findAll() {
        return MockDatabase.products;
    }
    public Product[] findByCategory(String category) {
        Product[] temp = new Product[MockDatabase.productCount];
        int count = 0;

        for (int i = 0; i < MockDatabase.productCount; i++)
            if (MockDatabase.products[i].getCategory().equals(category)) {
                temp[count] = MockDatabase.products[i];
                count++;
            }
        Product[] finalResults = new Product[count];
        for (int i = 0; i < count; i++) finalResults[i] = temp[i];

        return finalResults;
    }
    public boolean save(Product product) {
        boolean result = false;
        if (product == null) return result;
        Product p = findById(product.getProductId());
        if (p == null){
            MockDatabase.products[MockDatabase.productCount] = product;
            MockDatabase.nextProductId++;
            MockDatabase.productCount++;
            result = true;
            return result;
        }
        return result;
    }


    public boolean delete(Product product) {
        boolean result = false;
        if (product == null) return result;
        Product p = findById(product.getProductId());
        int foundIndex = -1;
        if (p != null){
            for(int i = 0; i < MockDatabase.productCount; i++)
                if (p.getProductId() == product.getProductId()) foundIndex = i;
            if (foundIndex != -1){
                for (int i = foundIndex; i < MockDatabase.productCount; i++)
                    MockDatabase.products[i] = MockDatabase.products[i + 1];
                MockDatabase.productCount--;
                MockDatabase.products[MockDatabase.productCount] = null;
                result = true;
                return result;
            }
            else return false;
        }
        return result;
    }
    public boolean deleteById(int productId) {
        boolean result1 = false;
        int foundIndex1 = -1;
        if (!(productId > MockDatabase.productCount) && !(productId < 0)) {
            for (int i = 0; i < MockDatabase.productCount; i++)
                if (MockDatabase.products[i].getProductId() == productId) foundIndex1 = i;
            if (foundIndex1 != -1){
                for (int i = foundIndex1; i < MockDatabase.productCount; i++){
                    MockDatabase.products[i] = MockDatabase.products[i + 1];
                }
                MockDatabase.productCount--;
                MockDatabase.products[MockDatabase.productCount] = null;
                result1 = true;
                return result1;
            }
        }
        return result1;
    }
    public boolean update(Product product) {
        if (product == null) return false;
        for (int i = 0; i < MockDatabase.productCount; i++) {
            if (MockDatabase.products[i].getProductId() == product.getProductId()) {
                MockDatabase.products[i] = product;
                return true;
            }
        }
        return false;
    }
    public boolean updateStock(int productId, int quantity) {
        boolean result = false;
        if (!(productId > MockDatabase.productCount) && !(productId < 0)){
            for (int i = 0; i < MockDatabase.productCount; i++){
                if (MockDatabase.products[i].getProductId() == productId){
                    MockDatabase.products[i].setStockQuantity(quantity);
                }
            }
            result = true;
            return result;
        }
        return result;
    }
    public Product[] searchByName(String name) {
        Product[] temp = new Product[MockDatabase.productCount];
        int count = 0;
        for (int i = 0; i < MockDatabase.productCount; i++)
            if (MockDatabase.products[i].getName().equals(name)) {
                temp[count] = MockDatabase.products[i];
                count++;
            }
        Product[] finalList = new Product[count];
        for (int i = 0; i < count; i++){
            finalList[i] = temp[i];
        }
        return finalList;
    }
    public int getNextProductId() {
        return MockDatabase.nextProductId;
    }


}
