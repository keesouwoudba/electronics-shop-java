package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.User;

public class UserRepository {
    public UserRepository() {}
    static {
        MockDatabase.users[0] = new User(1, "admin", "admin123", true, "2026-01-01");
        MockDatabase.users[1] = new User(2, "customer", "pass123", false, "2026-01-15");
        MockDatabase.userCount = 2;
        MockDatabase.nextUserId = 3;
    }
    public User findByUsername(String username) {
        for (User user : MockDatabase.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public User findById(int id) {
        for (User user : MockDatabase.users) {
            if (user.getUserId() == id) {
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
            MockDatabase.users[MockDatabase.nextUserId] = user;
            MockDatabase.nextUserId++;
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
        boolean result = false;
        if (user == null) {
            return result;
        }
        User u = findByUsername(user.getUsername());
        int foundIndex = -1;
        if (u != null) {
            for (int i = 0; i <= MockDatabase.userCount; i++) {
                if(MockDatabase.users[i].getUserId() == user.getUserId()) {
                    foundIndex = i;
                }
            }
            if (foundIndex != -1) {
                for (int i = foundIndex; i < MockDatabase.userCount; i++) {
                    MockDatabase.users[i] = MockDatabase.users[i + 1];
                }
            }

            MockDatabase.userCount--;
            MockDatabase.users[MockDatabase.userCount] = null;
            result = true;
            return result;
        }
        return result;
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
