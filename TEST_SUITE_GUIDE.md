# How to Run the Test Suite

## In IntelliJ IDEA

### Method 1: Using IDE Run Button
1. Open `Main.java` in the editor
2. Click the green **▶ Run** button in the top toolbar
3. Select **Run 'Main.main()'** from the dropdown
4. View results in the **Run** console tab at the bottom

### Method 2: Right-Click Menu
1. Right-click on `Main.java` in the Project Explorer
2. Select **Run 'Main.main()'**
3. View results in the console

### Method 3: Using Keyboard Shortcut
1. Open `Main.java`
2. Press **Shift + F10** (Windows)
3. View results in the console

---

## What the Test Suite Tests

The test suite (`Main.java`) runs 6 comprehensive test categories:

### ✅ TEST 1: AUTHENTICATION
- Admin login
- Customer login
- Logout
- Wrong password handling
- Non-existent user handling
- User registration

### ✅ TEST 2: PRODUCT REPOSITORY
- Find all products
- Find by ID
- Find by name
- Find by category
- Update stock
- Update product details
- Save new product

### ✅ TEST 3: CART OPERATIONS
- Create/find cart
- Add items
- Add duplicate items (quantity increase)
- Calculate total
- Save cart
- Remove items
- Clear cart

### ✅ TEST 4: SHOPPING FEATURES
- Get all products
- Get product by ID
- Add to cart
- View cart
- Remove from cart
- Checkout

### ✅ TEST 5: ORDER OPERATIONS
- Find all orders
- Find by ID
- Find by user ID
- Save order
- Update order status

### ✅ TEST 6: USER REPOSITORY
- Get all users
- Find by ID
- Find by username
- Update user
- Save user
- Verify system state

---

## Expected Output Format

Each test will display:
```
========== TEST 1: AUTHENTICATION ==========

[TEST] Admin Login
Result: Successfull login
Current User: admin
Is Admin: true

[TEST] Logout
Is Logged In: false

...and so on for all 6 test categories...

========== TEST SUITE COMPLETED ==========
```

---

## Troubleshooting

### Issue: "Cannot find symbol" errors
**Solution:** Ensure all files are saved (Ctrl+S) and rebuild the project (Build > Rebuild Project)

### Issue: NullPointerException during tests
**Solution:** This should not happen with the applied fixes. If it does, ensure:
- All files have been properly saved
- No compilation errors exist
- Dependencies are properly imported

### Issue: Tests fail or don't run
**Solution:** 
1. Clean the project: Build > Clean Project
2. Rebuild: Build > Rebuild Project
3. Run again: Shift + F10

---

## Sample Test Output

```
========== ELECTRONICS SHOPPING SYSTEM - TEST SUITE ==========

[SETUP] Initializing system...
[SETUP] System initialized successfully!

========== TEST 1: AUTHENTICATION ==========

[TEST] Admin Login
Result: Successfull login
Current User: admin
Is Admin: true

[TEST] Logout
Is Logged In: false

[TEST] Customer Login
Result: Successfull login
Is Admin: false

...continues with all tests...

========== TEST SUITE COMPLETED ==========
```

---

## Notes

- All tests are non-destructive and don't require external resources
- The test suite uses pre-initialized data from repositories
- Tests verify both positive and negative scenarios
- Console output is color-coded for easy reading in IntelliJ IDEA
