# OOP Extension Implementation Plan (Reasonable, Incremental, QA-Friendly)

## 1. Goal
This document defines a practical, low-risk plan to extend the current Electronics Shop project with:

- 2 inheritance examples (implemented)
- 2 abstract classes (with implemented methods)
- 2 interfaces (with implemented methods)
- 3 method overriding examples (implemented)
- 3 polymorphism examples (implemented)

The plan avoids a full rewrite and preserves current behavior while adding clear, testable OOP structures.

---

## 2. Constraints and Design Principles

## 2.1 Constraints
- Keep existing startup and DI pattern in `Main.java`.
- Avoid replacing persistence (`MockDatabase`) in this phase.
- Keep user-visible behavior mostly unchanged.
- Add OOP structures in places where they naturally fit and can be demonstrated in QA scenarios.

## 2.2 Principles
- Small, reversible refactors.
- Clear contracts (interfaces + abstract base classes).
- Keep business logic in services, not in UI classes.
- Prefer constructor injection.
- Keep compatibility with existing classes by using overloaded constructors where needed.
- Single ownership per responsibility (input reading, format selection, pricing lifecycle).

---

## 3. Architectural Choices (Exact)

1. **UI role behavior extraction**
- Introduce role-oriented screen classes (`CustomerScreen`, `AdminScreen`) with shared base behavior (`AbstractScreen`) and shared contract (`MenuActions`).
- Keep `ConsoleUI` as high-level app loop coordinator.

2. **Pricing behavior strategy**
- Introduce `DiscountPolicy` interface with multiple implementations.
- `ShopService` will apply discounts through this interface (polymorphic behavior).

3. **Reporting behavior hierarchy**
- Introduce `AbstractReportService` with shared implemented methods and two concrete report outputs (`ConsoleReportService`, `CsvReportService`).
- `AdminService` will depend on the abstract type and select concrete implementation from a format map.

4. **No persistence migration in this phase**
- CSV/file persistence is valuable, but out of scope for this assignment increment.
- This keeps risks lower and gives clean OOP examples without destabilizing repository code.

---

## 4. Target Class Tree (New + Modified)

```text
src/com/university/shopping/
├── Main.java (MODIFY: wire new dependencies)
├── view/
│   ├── ConsoleUI.java (MODIFY: delegate customer/admin menus)
│   ├── contracts/
│   │   └── MenuActions.java (NEW interface)
│   └── screens/
│       ├── AbstractScreen.java (NEW abstract class)
│       ├── CustomerScreen.java (NEW)
│       └── AdminScreen.java (NEW)
├── service/
│   ├── AuthService.java (NO REQUIRED CHANGE)
│   ├── ShopService.java (MODIFY: add DiscountPolicy usage)
│   ├── AdminService.java (MODIFY: add report service abstraction)
│   ├── pricing/
│   │   ├── DiscountPolicy.java (NEW interface)
│   │   ├── PricingMode.java (NEW enum)
│   │   ├── StandardDiscountPolicy.java (NEW)
│   │   └── SeasonalDiscountPolicy.java (NEW)
│   └── report/
│       ├── AbstractReportService.java (NEW abstract class)
│       ├── ConsoleReportService.java (NEW)
│       └── CsvReportService.java (NEW)
└── repository/
    ├── ... (NO REQUIRED CHANGE)
```

---

## 5. Requirement Mapping Matrix

## 5.1 Inheritance (2 examples)
1. `CustomerScreen extends AbstractScreen`
2. `AdminScreen extends AbstractScreen`

Additional implemented inheritance examples:
- `ConsoleReportService extends AbstractReportService`
- `CsvReportService extends AbstractReportService`

## 5.2 Abstract Classes (2, with implemented methods)
1. `AbstractScreen`
- implemented: `printTitle`, `pause`, `readIntSafe`, `truncate`

2. `AbstractReportService`
- implemented: `formatCurrency`, `buildTimestamp`, `buildHeader`

## 5.3 Interfaces (2, with implemented methods)
1. `MenuActions`
- implemented by `CustomerScreen`, `AdminScreen`

2. `DiscountPolicy`
- implemented by `StandardDiscountPolicy`, `SeasonalDiscountPolicy`

## 5.4 Method Overriding (3 examples)
1. `CustomerScreen.showMenu()` overrides abstract/base declaration in `AbstractScreen`
2. `AdminScreen.showMenu()` overrides abstract/base declaration in `AbstractScreen`
3. `CsvReportService.exportReport(...)` overrides abstract declaration in `AbstractReportService`

(Also extra)
- `ConsoleReportService.exportReport(...)` override

## 5.5 Polymorphism (3 examples)
1. `MenuActions actions = authService.isAdmin() ? adminScreen : customerScreen;`
2. `DiscountPolicy policy = isSeasonal ? new SeasonalDiscountPolicy() : new StandardDiscountPolicy();`
3. `AbstractReportService reportService = reportServicesByFormat.get(format);`

---

## 6. Detailed Class Specifications

## 6.1 `view/contracts/MenuActions.java` (NEW)
Purpose:
- Contract for any role-specific menu handler.

Fields:
- None.

Methods:
1. `void showMenu()`
- Input: none
- Output: none
- Behavior: prints available options for the concrete role.

2. `void handleOption(int option)`
- Input: menu option number
- Output: none (side effects on services/UI)
- Behavior: executes the selected role-specific action.

Example flow:
- Input `option=1` on customer screen -> browse products.
- Input `option=1` on admin screen -> open product management action.

---

## 6.2 `view/screens/AbstractScreen.java` (NEW ABSTRACT)
Purpose:
- Shared UI utilities and common dependencies for role-specific screens.

Fields:
1. `protected final Scanner scanner`
2. `protected final AuthService authService`
3. `protected final ShopService shopService`
4. `protected final AdminService adminService`

Methods:
1. `public AbstractScreen(Scanner, AuthService, ShopService, AdminService)`
- Input: dependencies
- Output: instance setup

2. `public void printTitle(String title)` (implemented)
- Input: title text
- Output: none
- Behavior: prints standardized title box/header.

3. `public void pause()` (implemented)
- Input: none
- Output: none
- Behavior: waits for Enter.

4. `public int readIntSafe()` (implemented)
- Input: none
- Output: validated integer
- Behavior: loops until valid integer.

5. `public String truncate(String value, int maxLength)` (implemented)
- Input: string + max length
- Output: truncated string
- Behavior: utility for table formatting.

6. `public abstract void showMenu()`
- Input: none
- Output: none
- To be overridden by concrete screens.

7. `public abstract void handleOption(int option)`
- Input: option
- Output: none
- To be overridden by concrete screens.

---

## 6.3 `view/screens/CustomerScreen.java` (NEW)
Purpose:
- Encapsulate customer-specific menu and actions currently inside `ConsoleUI`.

Extends:
- `AbstractScreen`

Implements:
- `MenuActions`

Fields:
- Inherits shared fields.

Methods:
1. `@Override public void showMenu()`
- Input: none
- Output: none
- Behavior: prints customer menu.

2. `@Override public void handleOption(int option)`
- Input: customer option
- Output: none
- Behavior: routes to helper methods.

3. `private void browseAllProducts()`
- Input: none
- Output: none
- Behavior: reads `shopService.getAllProducts()` and displays table.

4. `private void viewProductDetails()`
- Input: product id from user
- Output: none
- Behavior: prints product details from `shopService.getProductById(id)`.

5. `private void addProductToCart()`
- Input: product id + quantity
- Output: none
- Behavior: calls `shopService.addToCart(productId, quantity, userId)`.

6. `private void viewCart()`
- Input: none
- Output: none
- Behavior: calls `shopService.viewCart(userId)`, prints contents.

7. `private void removeFromCart()`
- Input: product id
- Output: none
- Behavior: calls `shopService.removeFromCart(productId, userId)`.

8. `private void checkout()`
- Input: confirmation
- Output: none
- Behavior: calls `shopService.checkout(userId)` and prints status.

---

## 6.4 `view/screens/AdminScreen.java` (NEW)
Purpose:
- Encapsulate admin-specific menu and actions currently inside `ConsoleUI`.

Extends:
- `AbstractScreen`

Implements:
- `MenuActions`

Fields:
- Inherits shared fields.

Methods:
1. `@Override public void showMenu()`
- Input: none
- Output: none
- Behavior: prints admin menu.

2. `@Override public void handleOption(int option)`
- Input: admin option
- Output: none
- Behavior: routes to product/user/report actions.

3. Product methods (private)
- `addNewProduct()`
- `updateProductPrice()`
- `updateProductName()`
- `setProductDiscount()`
- `addStock()`
- `removeStock()`
- `deleteProduct()`
- Inputs: product-related values
- Outputs: none
- Behavior: call corresponding `AdminService` methods and print status.

4. User methods (private)
- `addNewUser()`
- `updateUser()`
- `deleteUser()`
- `viewAllUsers()`
- Inputs: user-related values
- Outputs: none

5. `private void exportReport()`
- Input: selected format (console/csv)
- Output: none
- Behavior: calls `adminService.exportSystemReport(format)`.

---

## 6.5 `service/pricing/DiscountPolicy.java` (NEW INTERFACE)
Purpose:
- Pluggable discount calculation behavior.

Methods:
1. `double apply(Product product)`
- Input: product
- Output: final price
- Behavior: compute price under a specific policy.

---

## 6.5.1 `service/pricing/PricingMode.java` (NEW ENUM)
Purpose:
- Declares pricing lifecycle policy used by `ShopService`.

Values:
1. `LOCK_AT_ADD`
- Price is calculated once when item is added to cart and then remains stable.

2. `RECALCULATE_AT_CHECKOUT`
- Price is recalculated at checkout with the currently active `DiscountPolicy`.

---

## 6.6 `service/pricing/StandardDiscountPolicy.java` (NEW)
Purpose:
- Default discount logic matching current behavior.

Implements:
- `DiscountPolicy`

Fields:
- None.

Methods:
1. `@Override public double apply(Product product)`
- Input: product
- Output: discounted or base price
- Behavior: equivalent to `product.getFinalPrice()` semantics.

---

## 6.7 `service/pricing/SeasonalDiscountPolicy.java` (NEW)
Purpose:
- Alternative policy for campaigns/QA scenario.

Implements:
- `DiscountPolicy`

Fields:
1. `private final double extraPercent` (e.g., 5%)

Methods:
1. `public SeasonalDiscountPolicy(double extraPercent)`
- Input: additional percentage
- Output: initialized policy

2. `@Override public double apply(Product product)`
- Input: product
- Output: final price with base discount + seasonal factor (clamped)

Example:
- Product base: 1000, product discount 10%, seasonal 5% -> output 850

---

## 6.8 `service/report/AbstractReportService.java` (NEW ABSTRACT)
Purpose:
- Shared report formatting logic + abstract export mechanism.

Fields:
1. `protected final ProductRepository productRepository`
2. `protected final UserRepository userRepository`
3. `protected final OrderRepository orderRepository`

Methods:
1. `public AbstractReportService(ProductRepository, UserRepository, OrderRepository)`
- Input: repos
- Output: instance

2. `public String buildTimestamp()` (implemented)
- Input: none
- Output: ISO date-time string

3. `public String formatCurrency(double amount)` (implemented)
- Input: amount
- Output: formatted currency string

4. `public String buildHeader(String title)` (implemented)
- Input: report title
- Output: decorated header

5. `public abstract String exportReport()`
- Input: none
- Output: status/message
- Behavior: implemented by subclasses for destination-specific output.

CSV operational contract (applies to CSV implementation):
- Output directory is created automatically if missing.
- Export mode is overwrite (single latest snapshot).
- Delimiter is comma, values containing comma/quote/newline are quoted and escaped.
- Header row is always written.
- Errors are returned as `ERROR:<reason>` without crashing the app loop.

---

## 6.9 `service/report/ConsoleReportService.java` (NEW)
Purpose:
- Generate and print system summary to console.

Extends:
- `AbstractReportService`

Methods:
1. `@Override public String exportReport()`
- Input: none
- Output: success message
- Behavior:
  - compute counts from repositories
  - compute aggregate revenue from orders
  - print report in console format

---

## 6.10 `service/report/CsvReportService.java` (NEW)
Purpose:
- Generate and save system summary to CSV.

Extends:
- `AbstractReportService`

Fields:
1. `private final String outputPath`

Methods:
1. `public CsvReportService(..., String outputPath)`
- Input: repos + file path
- Output: instance

2. `@Override public String exportReport()`
- Input: none
- Output: status string (`SUCCESS:<path>` or `ERROR:<reason>`)
- Behavior:
  - generate same summary values as console service
  - write CSV with header + values
  - handle IO errors safely

---

## 6.11 `service/AdminService.java` (MODIFY)
Purpose change:
- Keep existing admin business logic and add report orchestration.

New field:
1. `private AbstractReportService reportService`
2. `private Map<String, AbstractReportService> reportServicesByFormat`

Constructor changes:
- Option A (recommended): overload constructor
  - keep existing constructor for backward compatibility
  - add new constructor with `Map<String, AbstractReportService> reportServicesByFormat`
  - default behavior: if map is not provided, initialize with `"console"` mapped to `ConsoleReportService`

New methods:
1. `public void setReportService(AbstractReportService reportService)`
- Input: report service implementation
- Output: none
- Behavior: runtime switching (polymorphism demo).

2. `public String exportSystemReport(String format)`
- Input: `"console"` or `"csv"`
- Output: status string
- Behavior:
  - verify admin
  - normalize format key (lowercase/trim)
  - resolve concrete service from `reportServicesByFormat`
  - call selected `exportReport()`
  - if key not found, return `ERROR:Unsupported format`

Existing methods:
- stay intact.

---

## 6.12 `service/ShopService.java` (MODIFY)
Purpose change:
- Keep shopping logic and delegate price policy to `DiscountPolicy`.

New field:
1. `private DiscountPolicy discountPolicy`
2. `private PricingMode pricingMode = PricingMode.LOCK_AT_ADD`

Constructor changes:
- Overload:
  - Existing constructor preserved.
  - New constructor accepts `DiscountPolicy`.
  - default behavior: if policy is not provided, use `StandardDiscountPolicy`.

New method:
1. `public void setDiscountPolicy(DiscountPolicy discountPolicy)`
- Input: policy impl
- Output: none

2. `public void setPricingMode(PricingMode mode)`
- Input: pricing mode enum
- Output: none
- Behavior: allows explicit pricing lifecycle selection for QA scenarios.

Method adjustments:
1. `checkout(int userId)`
- In default mode `LOCK_AT_ADD`, use `OrderItem.priceAtPurchase` snapshot for total and order creation.
- If `RECALCULATE_AT_CHECKOUT` mode is enabled, recompute with current `discountPolicy` before creating order.
- Return values remain existing style for compatibility.

2. `addToCart(...)`
- In default mode `LOCK_AT_ADD`, store `discountPolicy.apply(product)` as `priceAtPurchase`.
- Validate stock against requested quantity before adding.

---

## 6.13 `view/ConsoleUI.java` (MODIFY)
Purpose change:
- Continue as top-level loop + auth flows.
- Delegate role menu rendering/execution to polymorphic `MenuActions` objects.

New fields:
1. `private MenuActions customerScreen`
2. `private MenuActions adminScreen`

Constructor changes:
- instantiate screens with shared dependencies and scanner.

Method changes:
1. `start()`
- when logged in:
  - `MenuActions active = authService.isAdmin() ? adminScreen : customerScreen;`
  - `active.showMenu();`
  - `int choice = getIntInput();`
  - `active.handleOption(choice);`

Auth/guest methods:
- keep existing login/register/guest browse in `ConsoleUI` to reduce migration risk.

---

## 6.14 `Main.java` (MODIFY)
Purpose change:
- Wire new policy/report dependencies.

New wiring (example):
1. Create discount policy
- `DiscountPolicy discountPolicy = new StandardDiscountPolicy();`

2. Create report service
- `Map<String, AbstractReportService> reportServicesByFormat = Map.of(`
- `  "console", new ConsoleReportService(productRepository, userRepository, orderRepository),`
- `  "csv", new CsvReportService(productRepository, userRepository, orderRepository, "reports/system.csv")`
- `);`

3. Pass to services via new constructors/setters.

---

## 7. Data Flow (Post-Refactor)

## 7.1 Customer menu flow (polymorphic)
1. `ConsoleUI.start()` checks session.
2. Chooses `MenuActions active` based on role.
3. Calls `active.showMenu()`.
4. `ConsoleUI` reads choice via centralized input helper.
5. Calls `active.handleOption(choice)`.

Result:
- Same coordinator path.
- Different runtime behavior by concrete class.

## 7.2 Checkout flow with policy
1. `CustomerScreen.checkout()` -> `ShopService.checkout(userId)`.
2. `ShopService` validates stock.
3. Default mode uses locked `OrderItem.priceAtPurchase` values captured at add-to-cart.
4. Optional mode recalculates at checkout via `DiscountPolicy.apply(product)` for scenario testing.
5. Order saved.
6. Cart cleared.

Result:
- Strategy pattern provides polymorphic pricing.

## 7.3 Admin report export flow
1. `AdminScreen.exportReport()` asks output type.
2. `AdminService.exportSystemReport(format)` checks admin rights.
3. Resolves concrete service by format key from `reportServicesByFormat`.
4. Actual implementation executes (`ConsoleReportService` or `CsvReportService`).

Result:
- Shared algorithm base + destination-specific override.

---

## 8. Implementation Guarantees (Finalized)

1. **Status handling compatibility**
- Existing response strings remain compatible in this phase.
- Next hardening step introduces constants/enums for response codes.

2. **Money precision correctness**
- Cart and checkout arithmetic use decimal-safe operations.
- Order totals and report revenue align exactly.

3. **Stock validation lifecycle**
- Validation runs on add-to-cart for immediate feedback.
- Checkout repeats validation as final transaction guard.

4. **Discount control behavior**
- Admin discount operations apply the provided boolean and percentage values directly.

5. **ID lookup behavior**
- Repository operations resolve by entity id value instead of index assumptions.

6. **Object persistence behavior**
- Current `User` constructor side effects are preserved in this milestone for compatibility.
- Persistence refactor remains an isolated follow-up phase.

7. **Documentation alignment**
- `index.html` OOP blueprint mirrors this finalized plan and execution flow.

8. **Pricing lifecycle clarity**
- Default mode: `LOCK_AT_ADD`.
- Optional mode: `RECALCULATE_AT_CHECKOUT` for controlled QA scenarios.

9. **Report routing determinism**
- `exportSystemReport(format)` resolves implementation from `reportServicesByFormat` using normalized keys.
- Unsupported formats return `ERROR:Unsupported format`.

---

## 9. Feature Completeness Check

Required OOP features are covered by design and runtime paths.

Checklist:
- [x] 2 inheritance examples
- [x] 2 abstract classes with implemented methods
- [x] 2 interfaces with implementations
- [x] 3 overriding examples
- [x] 3 polymorphism examples

Functional completeness:
- [x] Customer and admin flows still available
- [x] Existing services remain callable
- [x] New functionality demonstrable via QA scenarios
- [x] Input ownership is centralized in `ConsoleUI`
- [x] Pricing lifecycle is deterministic by default
- [x] CSV export behavior is operationally specified

---

## 10. Phased Implementation Plan

Phase 1: Add core OOP types
1. Add `MenuActions`, `AbstractScreen`, `CustomerScreen`, `AdminScreen`.
2. Add `DiscountPolicy` and implementations.
3. Add `AbstractReportService` and implementations.

Phase 2: Integrate services
1. Update `ShopService` with policy injection + application.
2. Update `AdminService` with report abstraction.
3. Add default dependency fallbacks (`StandardDiscountPolicy`, console report map).

Phase 3: Integrate UI
1. Update `ConsoleUI` to delegate role menus via polymorphism.
2. Keep auth flows in `ConsoleUI` for stability.

Phase 4: Wiring
1. Update `Main` with new dependency initialization.

Phase 5: Consistency fixes (recommended)
1. Fix cart total decimal bug.
2. Fix discount boolean usage in admin discount method.
3. Validate add-to-cart stock.
4. Remove index-based id assumptions in repository checks.

Phase 6: QA scenarios
1. Run role-based menu tests.
2. Run checkout totals tests.
3. Run report generation tests (console + csv).

---

## 11. QA Scenario Suggestions (for your QA team)

1. **Polymorphic role menu**
- Login as customer -> customer options shown and executed.
- Login as admin -> admin options shown and executed.

2. **Discount policy switch**
- Run checkout under standard policy and seasonal policy; verify totals differ as expected.

3. **Report polymorphism**
- Use `ConsoleReportService` and verify output.
- Switch to `CsvReportService` and verify CSV file created and valid.
- Verify unsupported format returns `ERROR:Unsupported format`.

4. **Overriding checks**
- Verify distinct `showMenu` behavior in customer/admin screens.
- Verify `exportReport` behavior differs between console/csv services.

5. **Pricing lifecycle behavior**
- In `LOCK_AT_ADD`, change policy after adding item and verify checkout still uses locked cart snapshot.
- In `RECALCULATE_AT_CHECKOUT`, verify checkout reflects active policy at purchase time.

---

## 12. Why This Plan Is Reasonable Now

- It satisfies all assignment criteria directly.
- It keeps existing logic mostly intact.
- It demonstrates OOP concepts in meaningful runtime paths (not artificial examples).
- It limits regression risk by avoiding persistence migration in the same phase.

This is the recommended implementation baseline for the current milestone.
