# Electronics Shop Java

## Purpose
This repository implements a Java-based electronics store application that currently supports a console UI and has an in-progress JavaFX SPA-style frontend. The codebase centers on products, carts, orders, users, authentication, admin operations, CSV persistence, and a route-driven presentation layer.

This document has two goals:
1. describe the project as it exists today,
2. explain Step 5 in detail, because that is the major architecture transition planned for the UI and image workflow.

## Project Snapshot
- Language and runtime: Java 17.
- Build system: Maven with the wrapper checked in.
- Presentation layers: console UI is fully present; JavaFX UI infrastructure is partially present and being expanded.
- Persistence: CSV files under `data/` are the canonical storage layer.
- Main domain concepts: users, products, carts, orders, order items, and reports.

## Repository Layout
The important source areas are:
- `src/com/university/shopping/` for application entry points.
- `src/com/university/shopping/app/` for route and navigation state.
- `src/com/university/shopping/model/` for domain objects and the in-memory mock database.
- `src/com/university/shopping/repository/` for CSV persistence and collection-style access to the mock database.
- `src/com/university/shopping/service/` for business logic.
- `src/com/university/shopping/view/` for console and JavaFX UI code.

The `data/` directory contains the CSV source of truth and an `images/` folder for product media.

## Build And Runtime
The project is built with Maven and the JavaFX plugin.

- `pom.xml` targets Java 17.
- JavaFX controls and FXML are declared as dependencies.
- The JavaFX launcher is configured to start `com.university.shopping.Launcher`.
- `Launcher` starts the JavaFX application.
- `Main` starts the console application.
- `MainFx` is the JavaFX application entry point.

## Core Domain Model
The application revolves around a small set of domain objects.

- `User` stores identity, password, admin flag, and creation date.
- `Product` stores catalog data, stock, discount state, and image metadata.
- `Cart` stores a user-specific list of order items.
- `Order` stores completed purchase information.
- `OrderItem` stores product, quantity, and captured purchase price.
- `MockDatabase` stores in-memory arrays and next-id counters for the whole app.

`Product` already includes the Step 5 image metadata fields `imageName` and `imagePath`, so the data model is partially aligned with the target refactor.

## Persistence Model
Persistence is CSV-based and implemented manually rather than through an ORM.

### CSV files
- `data/users.csv`
- `data/products.csv`
- `data/orders.csv`
- `data/order_items.csv`

### Persistence responsibilities
- `CsvBootstrapInitializer` loads CSV data into `MockDatabase` during startup.
- `CsvPersistenceUtil` writes the current in-memory state back to CSV.
- `ProductRepository`, `UserRepository`, `OrderRepository`, and `CartRepository` are the main access points for reading and mutating data.

### Product CSV schema
The current code writes and reads product rows with image columns:

`id,name,price,category,description,stockQuantity,isDiscounted,discountPercentage,imageName,imagePath`

The bootstrap loader still supports legacy 8-column rows by defaulting image metadata to empty strings.

## Current Application Flow
The application has two presentation paths:

1. Console flow.
2. JavaFX SPA flow.

They share the same repositories and services underneath.

### Console flow
`Main` constructs repositories and services, then creates `ConsoleUI`.

`ConsoleUI` runs a loop:
- if the user is not logged in, it shows the auth menu,
- if the user is logged in, it dispatches to a customer or admin screen based on role.

`AuthScreen`, `CustomerScreen`, and `AdminScreen` are the main console screens. They call service methods directly, print tables and prompts, and rely on `AuthService` to determine session state.

### JavaFX flow
`Launcher` starts `MainFx`.

`MainFx` bootstraps repositories and services, creates a `Router`, creates the JavaFX shell, and hands everything to the renderer.

The JavaFX navigation model is route-based rather than scanner-based:
- user actions create a navigation intent,
- the router decides the target route,
- the renderer swaps the center page node,
- the shell keeps the header and footer in stable positions.

## Service Layer
The service layer is where business rules live.

### AuthService
`AuthService` handles:
- login,
- registration,
- logout,
- current user state,
- password validation,
- role checks.

Important behavior:
- successful login or registration sets the current user in session,
- admin checks are role-based,
- UI code should not dereference the current user unless it has confirmed a logged-in session.

### ShopService
`ShopService` handles:
- listing products,
- reading a product by id,
- add-to-cart logic,
- cart removal,
- checkout,
- stock validation,
- pricing policy selection.

Checkout is guarded by stock checks and persistence rollback logic. If stock persistence or order save fails, the service attempts to roll stock back to the previous values.

### AdminService
`AdminService` handles:
- product creation, update, deletion,
- stock changes,
- user creation, update, deletion,
- report export.

It enforces admin-only access before executing management operations.

### Product image service
`ProductImageService` supports the media workflow:
- validate image extension,
- copy image into `data/images/`,
- generate thumbnails best-effort,
- remove images best-effort.

This service is central to the Step 5 media refactor.

## Navigation And Route State
The app has an explicit navigation model under `src/com/university/shopping/app/`.

### Route
`Route` is the canonical route enum. It covers:
- guest routes,
- customer routes,
- admin routes.

### NavIntent
`NavIntent` is the input to navigation decisions.

It supports these intent types:
- app start,
- open route,
- open product details,
- login success,
- checkout success.

### RouteDecision
`RouteDecision` is the router output.

It contains:
- the resolved route,
- whether the action is allowed,
- a reason or flash message.

### AppState
`AppState` is the shared UI state container.

It stores:
- current user,
- current route,
- selected product id,
- flash message,
- loading flag.

## JavaFX Shell And Renderer
The JavaFX side is designed as a shell plus route-swapped content model.

### AppShell
`AppShell` owns the stable outer layout:
- a fixed top header,
- a notification bar near the top,
- a scrollable center area,
- a page flow container,
- a footer at the bottom of the scroll content.

This is the core Step 5 layout rule: the footer is part of page flow, not fixed to the viewport.

### UiRenderer
`UiRenderer` resolves the route to a page implementation and swaps the current page node into the shell.

Its responsibilities are:
- render the initial route,
- respond to navigation intents,
- update the header after navigation,
- show notifications,
- map routes to page classes.

## Reusable UI Components
The JavaFX layer is built from reusable components.

- `AuthFormComponent` builds login/register forms.
- `HeaderNavComponent` renders branding, auth state, and logout action.
- `FooterComponent` renders support links and version text.
- `NotificationBarComponent` shows temporary success and error banners.
- `ProductCardComponent` renders catalog cards and product actions.

The `ImageHelper` utility creates image nodes with fallback placeholders when a product image is missing.

## Page And Screen Layers
The console and JavaFX UI code each have an interaction layer.

### Console screens
`AbstractScreen`, `AuthScreen`, `CustomerScreen`, and `AdminScreen` are console-oriented and menu-driven.

### JavaFX pages
The JavaFX page set is route-driven and includes:
- login,
- register,
- guest catalog,
- customer catalog,
- product details,
- cart,
- checkout,
- admin products,
- admin product editor,
- admin users,
- admin reports.

Each page is expected to:
- render a node,
- wire events to services,
- trigger router intents for navigation,
- use the shared context to access services and state.

## Interaction Flow In Practice
The most important runtime flows are:

### Authentication flow
1. User enters credentials.
2. `AuthService.login` or `AuthService.register` validates the request.
3. `AuthService` updates the current user if the action succeeds.
4. The router sees the new auth state and chooses the appropriate route.
5. The renderer updates the visible page and header state.

### Browsing flow
1. User opens the catalog.
2. The page asks `ShopService` for products.
3. `ProductRepository` reads from the in-memory database.
4. Product cards render with image fallback support.
5. Product actions send navigation or cart requests back through services and router intents.

### Checkout flow
1. Customer reviews cart contents.
2. `ShopService.checkout` validates login state and stock.
3. The service creates an order, updates stock, saves order data, and clears the cart.
4. Success or error is shown through the notification system.
5. The router returns the customer to the catalog route after checkout success.

### Admin management flow
1. Admin opens management screens.
2. The page invokes `AdminService` methods.
3. The repository layer persists the resulting product or user changes.
4. Reports are exported through the configured report service.
5. The renderer refreshes the current page or navigates to the updated route.

## Step 5 In Detail
Step 5 is the major UX and data refactor in the project. It is not just a visual change; it introduces a new navigation model and a product image workflow that affects the model, CSV persistence, services, repositories, and UI components.

### Step 5 goals
- Replace the console loop as the primary experience with a JavaFX desktop shell.
- Keep the application in a single window with SPA-style route switching.
- Preserve the business logic in services and repositories.
- Add product image support across the model, CSV, and admin workflows.
- Keep the footer in normal page flow so long pages can scroll to it.

### Step 5 target layout
The target shell uses a `BorderPane` layout:
- top: fixed header navigation,
- center: `ScrollPane`,
- inside the scroll pane: a `VBox` page flow,
- page flow order: page content first, footer last.

This means the footer scrolls with the page rather than sticking to the viewport bottom.

### Step 5 route model
The navigation model is:
1. user action creates `NavIntent`,
2. `Router` checks current state and permissions,
3. `Router` returns `RouteDecision`,
4. `UiRenderer` loads the mapped page,
5. shell updates header and notification state,
6. page content is replaced while the outer shell remains stable.

### Step 5 access rules
- Guest routes are available only when the user is not logged in.
- Customer routes require a logged-in non-admin user.
- Admin routes require an admin user.
- Invalid navigation should redirect to a safe route with a user-visible reason.

### Step 5 route ownership
The target route groups are:
- `AuthScreen` or auth pages own login, register, and guest browsing.
- `CustomerScreen` or customer pages own products, details, cart, and checkout.
- `AdminScreen` or admin pages own products, editor, users, and reports.

### Step 5 UI building blocks
The Step 5 UI layer is composed of:
- `AppShell` for structure,
- `HeaderNavComponent` for navigation and session awareness,
- `FooterComponent` for consistent footer content,
- `NotificationBarComponent` for messages,
- `ProductCardComponent` for catalog cards,
- page classes for each route.

### Step 5 product image refactor
The product image refactor extends the current product data and file flow.

#### Product model changes
Each product carries:
- `imageName`, the display name shown in UI,
- `imagePath`, the stored relative path.

#### CSV changes
`products.csv` gains two new columns:
- `imageName`
- `imagePath`

Backward compatibility is required so older 8-column product rows still load by defaulting image fields to empty strings.

#### File storage policy
- Store images under `data/images/`.
- Persist relative paths rather than machine-specific absolute paths.
- Remove image files on delete or replace as a best-effort action.

#### Admin image workflow
The admin product editor must support:
- upload image,
- replace image,
- remove image,
- show current image name and saved path,
- validate extensions such as png, jpg, jpeg, and webp.

### Step 5 component responsibilities
- `ProductImageService` validates and manages files.
- `ProductImageUpdateRequest` carries product id, source file path, and image name.
- `ProductRepository` persists updated product image metadata.
- `AdminService` provides image-related product updates.
- `ImageHelper` renders either the stored image or a placeholder.

### Step 5 interaction checklist
The target implementation should satisfy these checks:
1. Launch the JavaFX app and render the shell.
2. Keep the header visible while navigating.
3. Render the footer as part of scrollable content.
4. Block guest access to customer and admin routes.
5. Block customer access to admin routes.
6. Allow admin routes after admin login.
7. Save uploaded product images into `data/images/`.
8. Remove or replace product images when requested.
9. Keep old product CSV rows readable.

## Current State Versus Step 5 Target
The repository is already partway through the Step 5 transition.

What already exists:
- JavaFX application entry point.
- Route and intent classes.
- Shell, renderer, notification bar, header, footer, and reusable card/form components.
- Product image metadata in the model.
- Product image service and image helper.
- CSV persistence that writes image columns.

What still needs careful completion:
- a fully consistent router/state implementation across all JavaFX pages,
- page classes that cover the complete target workflow,
- polished route guard behavior and visible feedback,
- final alignment between the Step 5 spec names and the current enum/class names,
- user-facing admin image editor workflow wired end-to-end.

## General Workflow Summary
The broad workflow of the application is:

1. Start the app through either `Main` for console or `Launcher` and `MainFx` for JavaFX.
2. Load CSV data into memory.
3. Use services for all business decisions.
4. Let repositories persist any mutations back to CSV.
5. Use console screens or JavaFX pages to drive interaction.
6. Route navigation through `Router` and `AppState` in the JavaFX path.
7. Use the notification system and shell layout to communicate outcome and preserve app structure.

## Files That Anchor The Architecture
- [Main.java](src/com/university/shopping/Main.java)
- [Launcher.java](src/com/university/shopping/Launcher.java)
- [MainFx.java](src/com/university/shopping/MainFx.java)
- [AppState.java](src/com/university/shopping/app/AppState.java)
- [Router.java](src/com/university/shopping/app/Router.java)
- [AuthService.java](src/com/university/shopping/service/AuthService.java)
- [ShopService.java](src/com/university/shopping/service/ShopService.java)
- [AdminService.java](src/com/university/shopping/service/AdminService.java)
- [CsvBootstrapInitializer.java](src/com/university/shopping/repository/CsvBootstrapInitializer.java)
- [CsvPersistenceUtil.java](src/com/university/shopping/repository/CsvPersistenceUtil.java)
- [AppShell.java](src/com/university/shopping/view/layout/AppShell.java)
- [UiRenderer.java](src/com/university/shopping/view/renderer/UiRenderer.java)
- [HeaderNavComponent.java](src/com/university/shopping/view/components/HeaderNavComponent.java)
- [FooterComponent.java](src/com/university/shopping/view/components/FooterComponent.java)
- [NotificationBarComponent.java](src/com/university/shopping/view/components/NotificationBarComponent.java)
- [ProductCardComponent.java](src/com/university/shopping/view/components/ProductCardComponent.java)
- [ProductImageService.java](src/com/university/shopping/service/media/ProductImageService.java)

## Final Summary
This project is a CSV-backed electronics shop application that is moving from a menu-driven terminal interface to a route-driven JavaFX SPA. The business rules already live in services and repositories, which makes the Step 5 transition mostly about presentation, navigation, and product image handling rather than rewriting the core business logic.
