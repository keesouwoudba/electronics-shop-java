package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.Product;

public class ProductRepository {

    public Product findById(int id) {
        for (int i = 0; i < MockDatabase.productCount; i++) {
            Product product = MockDatabase.products[i];
            if (product != null && product.getProductId() == id) return product;
        }
        return null;
    }

    public Product findByName(String name) {
        for (int i = 0; i < MockDatabase.productCount; i++) {
            Product product = MockDatabase.products[i];
            if (product != null && product.getName().equals(name)) return product;
        }
        return null;
    }

    public Product[] findAll() {
        Product[] result = new Product[MockDatabase.productCount];
        for (int i = 0; i < MockDatabase.productCount; i++) {
            result[i] = MockDatabase.products[i];
        }
        return result;
    }

    public Product[] findByCategory(String category) {
        Product[] temp = new Product[MockDatabase.productCount];
        int count = 0;

        for (int i = 0; i < MockDatabase.productCount; i++) {
            Product product = MockDatabase.products[i];
            if (product != null && product.getCategory().equals(category)) {
                temp[count++] = product;
            }
        }

        Product[] result = new Product[count];
        for (int i = 0; i < count; i++) {
            result[i] = temp[i];
        }
        return result;
    }

    public boolean save(Product product) {
        if (product == null) return false;
        if (findById(product.getProductId()) != null) return false;

        if (MockDatabase.productCount >= MockDatabase.products.length) return false;

        MockDatabase.products[MockDatabase.productCount++] = product;
        CsvPersistenceUtil.writeProductsToCsv();
        return true;
    }

    public boolean update(Product product) {
        if (product == null) return false;

        for (int i = 0; i < MockDatabase.productCount; i++) {
            if (MockDatabase.products[i] != null &&
                    MockDatabase.products[i].getProductId() == product.getProductId()) {
                MockDatabase.products[i] = product;
                CsvPersistenceUtil.writeProductsToCsv();
                return true;
            }
        }
        return false;
    }

    public boolean delete(Product product) {
        if (product == null) return false;

        for (int i = 0; i < MockDatabase.productCount; i++) {
            if (MockDatabase.products[i] != null &&
                    MockDatabase.products[i].getProductId() == product.getProductId()) {
                for (int j = i; j < MockDatabase.productCount - 1; j++) {
                    MockDatabase.products[j] = MockDatabase.products[j + 1];
                }
                MockDatabase.productCount--;
                MockDatabase.products[MockDatabase.productCount] = null;
                CsvPersistenceUtil.writeProductsToCsv();
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(int productId) {
        for (int i = 0; i < MockDatabase.productCount; i++) {
            if (MockDatabase.products[i] != null &&
                    MockDatabase.products[i].getProductId() == productId) {
                for (int j = i; j < MockDatabase.productCount - 1; j++) {
                    MockDatabase.products[j] = MockDatabase.products[j + 1];
                }
                MockDatabase.productCount--;
                MockDatabase.products[MockDatabase.productCount] = null;
                CsvPersistenceUtil.writeProductsToCsv();
                return true;
            }
        }
        return false;
    }

    public boolean updateStock(int productId, int quantity) {
        for (int i = 0; i < MockDatabase.productCount; i++) {
            if (MockDatabase.products[i] != null &&
                    MockDatabase.products[i].getProductId() == productId) {
                MockDatabase.products[i].setStockQuantity(quantity);
                CsvPersistenceUtil.writeProductsToCsv();
                return true;
            }
        }
        return false;
    }

    public Product[] searchByName(String name) {
        Product[] temp = new Product[MockDatabase.productCount];
        int count = 0;

        for (int i = 0; i < MockDatabase.productCount; i++) {
            Product product = MockDatabase.products[i];
            if (product != null && product.getName().equals(name)) {
                temp[count++] = product;
            }
        }

        Product[] result = new Product[count];
        for (int i = 0; i < count; i++) {
            result[i] = temp[i];
        }
        return result;
    }

    public int getNextProductId() {
        return MockDatabase.nextProductId;
    }

    public int getProductCount() {
        return MockDatabase.productCount;
    }
}
