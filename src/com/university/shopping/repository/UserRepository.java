package com.university.shopping.repository;

import com.university.shopping.model.MockDatabase;
import com.university.shopping.model.User;

public class UserRepository {
    public UserRepository() {}

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
        if (user == null) return false;
        if (findById(user.getUserId()) != null) return false;

        if (MockDatabase.userCount >= MockDatabase.users.length) return false;

        MockDatabase.users[MockDatabase.userCount] = user;
        MockDatabase.userCount++;

        if (!CsvPersistenceUtil.writeUsersToCsv()) {
            MockDatabase.userCount--;
            MockDatabase.users[MockDatabase.userCount] = null;
            return false;
        }

        return true;
    }

    public boolean update(User user) {
        if (user == null) return false;

        for (int i = 0; i < MockDatabase.userCount; i++) {
            User current = MockDatabase.users[i];
            if (current != null && current.getUserId() == user.getUserId()) {
                String oldUsername = current.getUsername();
                String oldPassword = current.getPassword();
                boolean oldIsAdmin = current.isAdmin();

                current.setUsername(user.getUsername());
                current.setPassword(user.getPassword());
                current.setIsAdmin(user.isAdmin());

                if (!CsvPersistenceUtil.writeUsersToCsv()) {
                    current.setUsername(oldUsername);
                    current.setPassword(oldPassword);
                    current.setIsAdmin(oldIsAdmin);
                    return false;
                }

                return true;
            }
        }
        return false;
    }

    public boolean delete(User user) {
        if (user == null) return false;

        for (int i = 0; i < MockDatabase.userCount; i++) {
            if (MockDatabase.users[i] != null &&
                    MockDatabase.users[i].getUserId() == user.getUserId()) {
                User removed = MockDatabase.users[i];
                for (int j = i; j < MockDatabase.userCount - 1; j++) {
                    MockDatabase.users[j] = MockDatabase.users[j + 1];
                }
                MockDatabase.userCount--;
                MockDatabase.users[MockDatabase.userCount] = null;

                if (!CsvPersistenceUtil.writeUsersToCsv()) {
                    for (int j = MockDatabase.userCount; j > i; j--) {
                        MockDatabase.users[j] = MockDatabase.users[j - 1];
                    }
                    MockDatabase.users[i] = removed;
                    MockDatabase.userCount++;
                    return false;
                }

                return true;
            }
        }
        return false;
    }

    public User[] getAllUsers() {
        User[] result = new User[MockDatabase.userCount];
        for (int i = 0; i < MockDatabase.userCount; i++) {
            result[i] = MockDatabase.users[i];
        }
        return result;
    }

    public int getNextUserId() {
        return MockDatabase.nextUserId;
    }

    public int getUserCount() {
        return MockDatabase.userCount;
    }
}
