# 🐛 Bug Fix: NullPointerException in UserRepository

## Issue Found

When running the test suite, a NullPointerException occurred in `UserRepository.findByUsername()` at line 16:

```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke 
"com.university.shopping.model.User.getUsername()" because "user" is null
	at com.university.shopping.repository.UserRepository.findByUsername(UserRepository.java:16)
```

## Root Cause

The `findByUsername()` and `findById()` methods were using an **enhanced for loop** that iterated through the **entire array**:

```java
// ❌ WRONG: Iterates through all 100 slots, including empty ones
for (User user : MockDatabase.users) {
    if (user.getUsername().equals(username)) {  // NullPointerException when user is null!
        return user;
    }
}
```

**Why this breaks:**
1. `MockDatabase.users[]` is initialized with 100 slots
2. Initially, only 2 users are added (admin and customer)
3. The remaining 98 slots contain `null`
4. When iterating with enhanced for loop, it eventually reaches a null slot
5. Calling `.getUsername()` on null throws NullPointerException

## Solution

Change to iterate only up to `MockDatabase.userCount` and add null-check:

```java
// ✅ CORRECT: Iterates only through filled slots with null-check
for (int i = 0; i < MockDatabase.userCount; i++) {
    User user = MockDatabase.users[i];
    if (user != null && user.getUsername().equals(username)) {
        return user;
    }
}
```

**Why this works:**
1. Uses `MockDatabase.userCount` (which is 2) instead of array length (100)
2. Only iterates through actually populated slots
3. Adds `user != null` check before accessing properties
4. No NullPointerException

## Files Fixed

### UserRepository.java
```
Method 1: findByUsername()
  Line: 14-21
  Before: Enhanced for loop on entire array
  After: Indexed loop up to userCount with null-check
  
Method 2: findById()
  Line: 22-29
  Before: Enhanced for loop on entire array
  After: Indexed loop up to userCount with null-check
```

## Complete Fixed Code

```java
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
```

## Test Results

✅ After fix, the test suite runs successfully:
- Authentication tests pass
- No NullPointerException
- All 6 test categories complete successfully

## Lesson Learned

**Pattern: Array Iteration Safety**

When iterating through arrays that don't use their full capacity:
1. ❌ DON'T: Use enhanced for loops on full-sized arrays
2. ✅ DO: Use indexed loops up to a count variable
3. ✅ DO: Add null-checks before accessing objects

```java
// ❌ Bad: Uses full array length
for (User user : MockDatabase.users) {
    // May encounter nulls!
}

// ✅ Good: Uses actual count
for (int i = 0; i < MockDatabase.userCount; i++) {
    User user = MockDatabase.users[i];
    if (user != null) {
        // Safe!
    }
}
```

## Impact

- **Severity:** High (causes runtime crash)
- **Fix Difficulty:** Low (simple loop restructuring)
- **Files Affected:** 1 (UserRepository.java)
- **Lines Changed:** 2 methods (16 lines total)
- **Test Coverage:** Now passes ✅

---

## Summary

**Issue:** NullPointerException when searching users  
**Cause:** Enhanced for loop iterating through empty array slots  
**Fix:** Use indexed loop with count and null-check  
**Result:** Test suite now runs successfully ✅

This is a common pattern when using fixed-size arrays as dynamic collections. Always iterate up to a count variable, not the array length!
