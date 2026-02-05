package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.User;

public class UserRepository {
    public UserRepository() {}
    static {
        MockDatabase.users[0] = new User("admin", "admin123", true, "2026-01-01");
        MockDatabase.userCount++;
        MockDatabase.users[1] = new User( "customer", "pass123", false, "2026-01-15");
        MockDatabase.userCount++;
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
            MockDatabase.users[MockDatabase.nextUserId-1] = user;
            MockDatabase.userCount++;
            result = true;
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
