package com.university.shopping.repository;

import com.university.shopping.model.Cart;
import com.university.shopping.model.MockDatabase;

public class CartRepository {
    public Cart findCartByUserId(int userId){
        for(int i = 0; i < MockDatabase.cartCount; i++){
            if(MockDatabase.carts[i].getUserId() == userId){
                return MockDatabase.carts[i];
            }
        }

        Cart temp = new Cart(userId);
        MockDatabase.carts[MockDatabase.cartCount] = temp;
        MockDatabase.cartCount++;
        return temp;
    }
    public boolean saveCart(Cart cart){
        boolean result = false;
        int foundIndex = -1;
        for(int i = 0; i < MockDatabase.cartCount; i++){
            if (MockDatabase.carts[i].getUserId() == cart.getUserId()){
                foundIndex = i;
            }
        }
        if (foundIndex != -1){
            MockDatabase.carts[foundIndex] = cart;
            result = true;
            return  result;
        }
        else{
            MockDatabase.carts[MockDatabase.cartCount] = cart;
            MockDatabase.cartCount++;
            result = true;
            return result;
        }

    }
}
