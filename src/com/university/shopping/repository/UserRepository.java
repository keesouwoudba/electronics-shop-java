package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.User;

public class UserRepository {
    public UserRepository() {}
    static {
        if (MockDatabase.userCount == 0) {
            new User("admin", "admin123", true, "2026-01-01");
            new User("customer", "pass123", false, "2026-01-15");
        }
    }
    public User findByUsername(String username) {
        for (int i = 0; i < MockDatabase.userCount; i++) {
            User user = MockDatabase.users[i];
            if (user != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public User findById(int id) {
        for (int i = 0; i < MockDatabase.userCount; i++) {
            User user = MockDatabase.users[i];
            if (user != null && user.getUserId() == id) {
                return user;
            }
        }
        return null;
    }
    public boolean save(User user) {
        boolean result = false;
        if (user == null) {
            return result;
        }
        User u = findByUsername(user.getUsername());
        if (u == null) {
            // The User constructor already adds the user to MockDatabase.users
            // and increments userCount. However, save is often called with 
            // a user object that was already created.
            
            // Let's check if the user is already in the database
            for (int i = 0; i < MockDatabase.userCount; i++) {
                if (MockDatabase.users[i] == user) {
                    return true;
                }
            }
            
            // If not, and there's space, add it.
            if (MockDatabase.userCount < MockDatabase.users.length) {
                MockDatabase.users[MockDatabase.userCount] = user;
                MockDatabase.userCount++;
                result = true;
            }
            return result;
        }

        return result;
    }
    public boolean update(User user) {
        boolean result = false;
        if (user == null) {
            return result;
        }
        User u = findByUsername(user.getUsername());
        if (u != null) {
            u.setPassword(user.getPassword());
            u.setUsername(user.getUsername());
            u.setIsAdmin(user.isAdmin());
            result = true;
        }
        return result;
    }
    public boolean delete(User user) {
        if (user == null) return false;
        for (int i = 0; i < MockDatabase.userCount; i++) {
            if (MockDatabase.users[i].getUserId() == user.getUserId()) {
                for (int j = i; j < MockDatabase.userCount - 1; j++) {
                    MockDatabase.users[j] = MockDatabase.users[j + 1];
                }
                MockDatabase.userCount--;
                MockDatabase.users[MockDatabase.userCount] = null;
                return true;
            }
        }
        return false;
    }
    public User[] getAllUsers() {
        return MockDatabase.users;
    }
    public int getNextUserId() {
        return MockDatabase.nextUserId;
    }
    public int getUserCount() {
        return MockDatabase.userCount;
    }

}
