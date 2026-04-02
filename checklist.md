# OOP Extension Checklist

```text
electronics-shop-java/
├── OOP_EXTENSION_IMPLEMENTATION_PLAN.md
├── checklist.md
└── src/
    └── com/
        └── university/
            └── shopping/
                ├── Main.java
                ├── model/
                │   ├── Cart.java
                │   ├── MockDatabase.java
                │   ├── Order.java
                │   ├── OrderItem.java
                │   ├── Product.java
                │   └── User.java
                ├── repository/
                │   ├── CartRepository.java
                │   ├── OrderRepository.java
                │   ├── ProductRepository.java
                │   └── UserRepository.java
                ├── service/
                │   ├── AdminService.java
                │   ├── AuthService.java
                │   ├── ShopService.java
                │   ├── pricing/
                │   │   ├── DiscountPolicy.java *
                │   │   ├── PricingMode.java *
                │   │   ├── StandardDiscountPolicy.java *
                │   │   └── SeasonalDiscountPolicy.java *
                │   └── report/
                │       ├── AbstractReportService.java *
                │       ├── ConsoleReportService.java *
                │       └── CsvReportService.java *
                └── view/
                    ├── ConsoleUI.java
                    ├── contracts/
                    │   └── MenuActions.java *
                    └── screens/
                        ├── AbstractScreen.java *
                        ├── CustomerScreen.java *
                        └── AdminScreen.java *
```

## Step 1 Checklist (From OOP Plan)

- Add `MenuActions` interface with `showMenu()` and `handleOption(int)`.
- Add `AbstractScreen` abstract class with shared scanner/services helpers.
- Add `CustomerScreen` and `AdminScreen` classes that extend `AbstractScreen`.
- Add `DiscountPolicy` strategy interface and two implementations.
- Add `PricingMode` enum for `LOCK_AT_ADD` and `RECALCULATE_AT_CHECKOUT`.
- Add `AbstractReportService` with shared report helper methods.
- Add `ConsoleReportService` and `CsvReportService` overriding `exportReport()`.
- Update `ShopService` to use injectable `DiscountPolicy` + `PricingMode`.
- Update `AdminService` to route reports by format map.
- Update `ConsoleUI` to delegate role menus via `MenuActions` polymorphism.
- Update `Main` to wire pricing and reporting dependencies.
- Preserve existing behavior and response style where compatibility is expected.

## Step 2 Checklist (From Current Codebase Gap Analysis)

- Refactor `ConsoleUI` customer methods into `CustomerScreen`.
- Refactor `ConsoleUI` admin product/user methods into `AdminScreen`.
- Keep auth flows in `ConsoleUI` and delegate only logged-in role menus.
- Replace direct `showCustomerMenu()` / `showAdminMenu()` loop calls with polymorphic screen dispatch.
- In `ShopService.addToCart(...)`, validate stock before adding item.
- In `ShopService.addToCart(...)`, store policy-applied price snapshot for lock-at-add mode.
- In `ShopService.checkout(...)`, support optional recalculation mode before order creation.
- In `ShopService.checkout(...)`, ensure total uses item snapshots consistently.
- In `AdminService.setProductDiscount(...)`, honor incoming `isDiscounted` flag instead of forcing `true`.
- In `AdminService`, add overloaded constructor/backward-compatible defaults.
- In `AdminService`, add `exportSystemReport(String format)` with normalized keys and unsupported-format error.
- In `Main`, construct report services map (`console`, `csv`) and inject into `AdminService`.
- In `Main`, construct and inject default `StandardDiscountPolicy` into `ShopService`.
- Add QA pass for menu switching, checkout totals, stock checks, and report export.
