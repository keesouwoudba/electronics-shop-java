# Step 3 Implementation Specification (CSV Persistence + Error Handling)

## 0) Goal and Requirement Mapping
This document defines a complete implementation for Step 3, based on the current codebase and prior discussion.

Step 3 requirements coverage:
1. Create 3 different files containing entity data.
2. Use 4 different try-catch patterns for different error types.
3. Use one try-catch that contains multiple `catch` blocks and a `finally` block.

Planned fulfillment:
- Entity storage files: `products.csv`, `users.csv`, `orders.csv` (plus optional `order_items.csv` for clean normalization).
- CSV report exporter hardening with try-catch and resource-safe behavior.
- Startup bootstrap: CSV files -> in-memory arrays (`MockDatabase`) before business flow.
- Mutation trigger: every repository write updates corresponding CSV immediately.

## 1) New Files Tree (modified `*`, new `**`)
```text
.
|-- STEP3_CSV_PERSISTENCE_IMPLEMENTATION.md **
|-- data **
|   |-- products.csv **
|   |-- users.csv **
|   |-- orders.csv **
|   |-- order_items.csv **
|   `-- errors.log **
`-- src
    `-- com/university/shopping
        |-- Main.java *
        |-- model
        |   |-- MockDatabase.java *
        |   |-- User.java *
        |   |-- Product.java *
        |   `-- Order.java *
        |-- repository
        |   |-- UserRepository.java *
        |   |-- ProductRepository.java *
        |   |-- OrderRepository.java *
        |   `-- CartRepository.java *
        |-- persistence **
        |   |-- CsvPaths.java **
        |   |-- CsvErrorLogger.java **
        |   |-- CsvEscaper.java **
        |   |-- CsvBootstrapInitializer.java **
        |   |-- UserCsvStore.java **
        |   |-- ProductCsvStore.java **
        |   |-- OrderCsvStore.java **
        |   `-- OrderItemCsvStore.java **
        `-- service/report
            `-- CsvReportService.java *
```

Notes:
- `order_items.csv` is recommended for clean object persistence because `Order` contains `OrderItem[]`.
- `errors.log` is used for non-fatal parse and IO issues.

## 2) New Workflow and Data Flow

## 2.1 Startup flow
1. `Main.main` creates repositories.
2. Repository static initialization delegates to `CsvBootstrapInitializer.initializeIfNeeded()`.
3. Initializer ensures data directory and CSV files exist with headers.
4. Initializer clears in-memory arrays/counts safely.
5. Initializer loads `users.csv`, `products.csv`, `orders.csv`, `order_items.csv`.
6. Initializer computes and sets `nextUserId`, `nextProductId`, `nextOrderId` from max loaded ids.
7. Application continues with normal service/UI startup.

Outcome: app uses in-memory arrays exactly as today, but now backed by persistent CSV.

## 2.2 Runtime read flow
1. Services call repositories.
2. Repositories read from `MockDatabase` arrays.
3. No per-request disk read.

Outcome: fast business logic, minimal refactor risk.

## 2.3 Runtime write flow (triggered persistence)
1. Service asks repository to mutate entity (`save`, `update`, `delete`, stock update, checkout order save).
2. Repository updates array/count.
3. Repository immediately calls the corresponding CSV store `writeAll(...)`.
4. CSV store writes temp file then replaces real file.
5. On failure, repository returns failure result and logs error.

Outcome: each mutation is persisted immediately, avoiding memory-file divergence.

## 2.4 Report export flow
1. `AdminService.exportSystemReport("csv")` calls `CsvReportService.exportReport()`.
2. CSV report service writes metrics report with robust try-catch and `finally` cleanup.
3. Result contract remains `SUCCESS:<path>` or `ERROR:<message>`.

## 3) CSV Schemas

## 3.1 `data/products.csv`
Header:
```csv
id,name,price,category,description,stockQuantity,isDiscounted,discountPercentage
```

## 3.2 `data/users.csv`
Header:
```csv
id,username,password,isAdmin,createdDate
```

## 3.3 `data/orders.csv`
Header:
```csv
id,userId,orderDate,totalPrice,status
```

## 3.4 `data/order_items.csv`
Header:
```csv
orderId,productId,productName,quantity,priceAtPurchase
```

## 4) Error Handling Plan (Step 3 try-catch requirements)

## 4.1 Type A: IO failure handling (`IOException`)
Location:
- `CsvReportService.exportReport()`
- all CSV store read/write methods

Behavior:
- return `ERROR:<message>` for report export
- return `false` for persistence write calls
- append detailed trace to `errors.log`

## 4.2 Type B: numeric parse failure (`NumberFormatException`)
Location:
- parsing `id`, `price`, `stockQuantity`, `quantity`, `discountPercentage`

Behavior:
- skip malformed row
- log file name + row index + content
- continue loading remaining rows

## 4.3 Type C: date parse failure (`java.time.format.DateTimeParseException`)
Location:
- parse `createdDate` and `orderDate` with strict formatter `yyyy-MM-dd`

Behavior:
- skip malformed row and log parse error
- continue initialization

## 4.4 Type D: invalid schema/state (`IllegalArgumentException`)
Location:
- header mismatch validation
- invalid boolean text (`isAdmin`, `isDiscounted`)
- illegal empty mandatory fields

Behavior:
- throw or return parse error object
- initialization logs reason and proceeds according to fail policy

## 4.5 Multi-catch + finally requirement
Mandatory implementation location:
- `CsvBootstrapInitializer.loadProducts()` (or centralized parser helper)

Required structure:
```java
BufferedReader reader = null;
try {
    reader = new BufferedReader(new FileReader(path));
    // parse rows
} catch (NumberFormatException e) {
    // row numeric issues
} catch (DateTimeParseException e) {
    // date issues
} catch (IOException e) {
    // file IO issues
} finally {
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException closeError) {
            // log close error
        }
    }
}
```

This block explicitly satisfies: multiple catches + finally.

## 5) File-by-File Implementation Order and Specifications

The sequence below is the recommended coding order.

## 5.1 `src/com/university/shopping/persistence/CsvPaths.java` **
Purpose:
- centralize all path constants to avoid duplicated literals.

Dependencies:
- none.

Class contract:
- `public final class CsvPaths`

Methods/fields:
- `public static final String DATA_DIR`
- `public static final String USERS_FILE`
- `public static final String PRODUCTS_FILE`
- `public static final String ORDERS_FILE`
- `public static final String ORDER_ITEMS_FILE`
- `public static final String ERROR_LOG_FILE`

Returns:
- constants only.

## 5.2 `src/com/university/shopping/persistence/CsvEscaper.java` **
Purpose:
- escape/unescape CSV text safely.

Dependencies:
- none.

Methods:
- `public static String escape(String value)`
- `public static String[] splitCsvLine(String line)`

Input/Output:
- input raw text or csv line, output escaped text or parsed columns.

Key behavior:
- supports comma, quote, and newline quoting rules.

## 5.3 `src/com/university/shopping/persistence/CsvErrorLogger.java` **
Purpose:
- append non-fatal errors to `errors.log`.

Dependencies:
- `CsvPaths`
- Java IO/time APIs.

Methods:
- `public static void log(String source, String message)`
- `public static void logException(String source, Exception ex)`

Input/Output:
- accepts source label and details, returns `void`.

## 5.4 `src/com/university/shopping/model/User.java` *
Purpose of change:
- support restoring persisted `id` values without corrupting next-id state.

Dependencies:
- `MockDatabase`.

New/changed methods:
- `public User(int id, String username, String password, boolean isAdmin, String createdDate, boolean autoRegister)`
- existing constructor may delegate to new constructor.
- optional static factory: `public static User fromPersistence(...)`

Input/Output:
- accepts explicit id and registration flag.
- returns constructed `User`.

Critical behavior:
- if `autoRegister=false`, constructor must not auto-insert into `MockDatabase.users`.

## 5.5 `src/com/university/shopping/model/Product.java` *
Purpose of change:
- allow explicit persisted id restore.

New/changed methods:
- overloaded constructor with explicit `productId`.
- optional static factory `fromPersistence`.

## 5.6 `src/com/university/shopping/model/Order.java` *
Purpose of change:
- allow explicit persisted `orderId` restore.

New/changed methods:
- overloaded constructor with explicit `orderId`.
- optional static factory `fromPersistence`.

## 5.7 `src/com/university/shopping/model/MockDatabase.java` *
Purpose of change:
- provide deterministic reset for bootstrap reload.

New methods:
- `public static void resetAll()`
- `public static void recalculateNextIds()` or separate setter helpers.

Input/Output:
- no inputs, returns `void`.

## 5.8 `src/com/university/shopping/persistence/UserCsvStore.java` **
Purpose:
- read/write all users from/to CSV.

Dependencies:
- `User`, `MockDatabase`, `CsvPaths`, `CsvEscaper`, `CsvErrorLogger`.

Methods:
- `public void ensureFileWithHeader()`
- `public User[] loadAll()`
- `public boolean writeAll(User[] users, int userCount)`

Input/Output:
- `loadAll` returns compact array with valid parsed users.
- `writeAll` returns `true` on success.

## 5.9 `src/com/university/shopping/persistence/ProductCsvStore.java` **
Purpose:
- read/write products.

Dependencies:
- `Product`, helpers above.

Methods:
- `public void ensureFileWithHeader()`
- `public Product[] loadAll()`
- `public boolean writeAll(Product[] products, int productCount)`

## 5.10 `src/com/university/shopping/persistence/OrderCsvStore.java` **
Purpose:
- read/write orders (without items table payload).

Dependencies:
- `Order`, helpers above.

Methods:
- `public void ensureFileWithHeader()`
- `public Order[] loadAllWithoutItems()`
- `public boolean writeAll(Order[] orders, int orderCount)`

## 5.11 `src/com/university/shopping/persistence/OrderItemCsvStore.java` **
Purpose:
- read/write order item rows and attach by `orderId`.

Dependencies:
- `Order`, `OrderItem`, helpers above.

Methods:
- `public void ensureFileWithHeader()`
- `public Map<Integer, OrderItem[]> loadItemsGroupedByOrderId()`
- `public boolean writeAll(Order[] orders, int orderCount)`

## 5.12 `src/com/university/shopping/persistence/CsvBootstrapInitializer.java` **
Purpose:
- one-time startup bootstrap from CSV into `MockDatabase`.

Dependencies:
- all CsvStore classes, `MockDatabase`, `CsvErrorLogger`.

Methods:
- `public static synchronized void initializeIfNeeded()`
- `private static void ensureFiles()`
- `private static void loadUsers()`
- `private static void loadProducts()`
- `private static void loadOrdersAndItems()`
- `private static void finalizeCounters()`

Input/Output:
- no external input; populates global in-memory arrays.

Critical behavior:
- idempotent guard (`initialized` flag).
- contains the required multi-catch + finally try-catch block.

## 5.13 `src/com/university/shopping/repository/UserRepository.java` *
Purpose of change:
- replace hardcoded seed logic with CSV bootstrap.
- persist immediately after mutation.

Dependencies:
- `CsvBootstrapInitializer`, `UserCsvStore`.

Changed flow:
- static block: call `CsvBootstrapInitializer.initializeIfNeeded()`.
- `save`, `update`, `delete`: after successful in-memory mutation call `userCsvStore.writeAll(...)`.

Method contracts unchanged for caller.

## 5.14 `src/com/university/shopping/repository/ProductRepository.java` *
Purpose of change:
- replace static seeded products with CSV load.
- persist on `save`, `update`, `delete`, `updateStock`.

Dependencies:
- `CsvBootstrapInitializer`, `ProductCsvStore`.

Method contracts unchanged for caller.

## 5.15 `src/com/university/shopping/repository/OrderRepository.java` *
Purpose of change:
- persist orders whenever saved/updated.
- persist order_items alongside orders.

Dependencies:
- `OrderCsvStore`, `OrderItemCsvStore`.

Changed methods:
- `save(Order order)` now writes `orders.csv` and `order_items.csv` on success.

## 5.16 `src/com/university/shopping/repository/CartRepository.java` *
Purpose of change:
- optional integration to support future `carts.csv` (not required by Step 3).

Recommended now:
- keep current in-memory cart behavior.
- no disk persistence required for cart in this step.

## 5.17 `src/com/university/shopping/service/report/CsvReportService.java` *
Purpose of change:
- strengthen file writing with robust try-catch and finally compliance.

Dependencies:
- Java IO classes and optional `CsvErrorLogger`.

Changed methods:
- `exportReport()` updated to use defensive resource handling.

Minimum behavior:
- creates parent directory if missing.
- returns `SUCCESS:<path>` on success.
- returns `ERROR:<message>` on failure.
- logs exception details.

## 5.18 `src/com/university/shopping/Main.java` *
Purpose of change:
- force explicit bootstrap before services if repository static init is not preferred.

Option A:
- explicit call at startup `CsvBootstrapInitializer.initializeIfNeeded();`

Option B:
- keep bootstrap in repository static blocks and no Main change.

Recommendation:
- use Option A for transparency and testability.

## 6) Data Integrity and Transaction Safety
- Always write to temp file first (`*.tmp`) and then rename/replace target file.
- Never partially rewrite in-place.
- On write failure, do not alter target file.
- Preserve header row at all times.
- Validate row column count before parsing.

## 7) Compatibility Notes for Current Code
- Existing services (`AuthService`, `ShopService`, `AdminService`) can remain mostly unchanged.
- Repository APIs remain same signatures to avoid UI/service breakage.
- Most required refactor is in models + repositories + new persistence package.

## 8) Minimal Acceptance Checklist
1. App starts with empty `data/` and auto-creates CSV files with headers.
2. App starts with existing CSV and restores users/products/orders correctly.
3. Adding/updating/deleting product updates `products.csv` immediately.
4. Register/add/update/delete user updates `users.csv` immediately.
5. Checkout updates `orders.csv` and `order_items.csv` immediately.
6. CSV report export handles IO error gracefully.
7. At least 4 distinct error types are handled in try-catch.
8. At least one code path uses multi-catch + finally.

## 9) Suggested Build Sequence (practical)
1. Add persistence helpers (`CsvPaths`, `CsvEscaper`, `CsvErrorLogger`).
2. Add model constructor/factory support for persisted IDs.
3. Add CsvStore classes for users/products/orders/items.
4. Add bootstrap initializer and wire startup.
5. Modify repositories to persist on mutation.
6. Harden `CsvReportService` try-catch/finally.
7. Run manual scenarios for admin, customer, and report export.

## 10) Non-Goals for This Step
- Full relational DB migration.
- Full ACID transactions.
- Concurrent multi-process file locking strategy.

This implementation keeps your current architecture and completes Step 3 with low disruption and clear extension points.
