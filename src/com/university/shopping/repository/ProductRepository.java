package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Product;

public class ProductRepository {
    static {
        MockDatabase.products[0] = new Product( "iPhone 15 Pro", 1199.99,
                "Phone", "Latest Apple flagship with A17 Pro chip", 25, false, 0);
        MockDatabase.products[1] = new Product( "Samsung Galaxy S24", 999.99,
                "Phone", "Android flagship with AI features", 30, true, 10);
        MockDatabase.products[2] = new Product( "MacBook Pro M3", 2499.99,
                "Laptop", "16-inch professional laptop", 15, false, 0);
        MockDatabase.products[3] = new Product( "Dell XPS 15", 1799.99,
                "Laptop", "Windows ultrabook", 20, true, 5);
        MockDatabase.products[4] = new Product( "Sony WH-1000XM5", 399.99,
                "Accessory", "Noise-cancelling headphones", 50, false, 0);
        MockDatabase.products[5] = new Product( "AirPods Pro 2", 249.99,
                "Accessory", "Apple wireless earbuds", 60, false, 0);
        MockDatabase.products[6] = new Product( "iPad Air M2", 599.99,
                "Tablet", "10.9-inch tablet", 40, true, 8);
        MockDatabase.products[7] = new Product( "Samsung Galaxy Tab S9", 799.99,
                "Tablet", "11-inch AMOLED display", 35, false, 0);
        MockDatabase.products[8] = new Product( "Logitech MX Master 3S", 99.99,
                "Accessory", "Premium wireless mouse", 80, false, 0);
        MockDatabase.products[9] = new Product( "Apple Magic Keyboard", 149.99,
                "Accessory", "Wireless keyboard", 45, false, 0);
        MockDatabase.products[10] = new Product( "LG UltraWide Monitor", 499.99,
                "Accessory", "34-inch curved display", 20, true, 12);
        MockDatabase.products[11] = new Product( "Bose QuietComfort 45", 329.99,
                "Accessory", "Wireless headphones", 40, false, 0);
        MockDatabase.products[12] = new Product( "Google Pixel 8 Pro", 899.99,
                "Phone", "Google flagship phone", 28, true, 15);
        MockDatabase.products[13] = new Product( "ASUS ROG Gaming Laptop", 1999.99,
                "Laptop", "RTX 4070 gaming laptop", 12, false, 0);
        MockDatabase.products[14] = new Product("Apple Watch Series 9", 399.99,
                "Accessory", "Smartwatch with health tracking", 55, false, 0);

        MockDatabase.productCount = 15;
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
        if (product == null) return false;
        Product p = findById(product.getProductId());
        if (p == null){
            if (MockDatabase.productCount < MockDatabase.products.length) {
                MockDatabase.products[MockDatabase.productCount] = product;
                MockDatabase.productCount++;
                return true;
            }
        }
        return false;
    }


    public boolean delete(Product product) {
        if (product == null) return false;
        for(int i = 0; i < MockDatabase.productCount; i++){
            if (MockDatabase.products[i].getProductId() == product.getProductId()) {
                for (int j = i; j < MockDatabase.productCount - 1; j++){
                    MockDatabase.products[j] = MockDatabase.products[j + 1];
                }
                MockDatabase.productCount--;
                MockDatabase.products[MockDatabase.productCount] = null;
                return true;
            }
        }
        return false;
    }
    public boolean deleteById(int productId) {
        if (productId >= 0 && productId < MockDatabase.productCount) {
            for (int i = 0; i < MockDatabase.productCount; i++) {
                if (MockDatabase.products[i].getProductId() == productId) {
                    for (int j = i; j < MockDatabase.productCount - 1; j++){
                        MockDatabase.products[j] = MockDatabase.products[j + 1];
                    }
                    MockDatabase.productCount--;
                    MockDatabase.products[MockDatabase.productCount] = null;
                    return true;
                }
            }
        }
        return false;
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
        if (productId >= 0 && productId < MockDatabase.productCount){
            for (int i = 0; i < MockDatabase.productCount; i++){
                if (MockDatabase.products[i].getProductId() == productId){
                    MockDatabase.products[i].setStockQuantity(quantity);
                    return true;
                }
            }
        }
        return false;
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
    // ...existing code...
    public int getProductCount() {
        return MockDatabase.productCount;
    }
}
