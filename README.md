# 🛒 Electronics Shop - Java Application

> A professional **layered architecture** electronics shop system demonstrating enterprise-grade Java development patterns, clean code principles, and comprehensive OOP design. Available in **two versions**: Console and JavaFX GUI.

## 📋 Project Overview

This is an educational software engineering project showcasing a **production-ready e-commerce platform** with two distinct implementations:

1. **Console Application** (`Main.java`) - Traditional text-based interface with professional enterprise architecture patterns
2. **JavaFX GUI Application** (`Launcher.java` → `MainFx.java`) - Modern graphical interface with reactive components

**Status**: Fully functional dual-mode application with professional enterprise architecture patterns.

## ✨ Key Features

- 🔐 **Advanced Authentication** - Login/Register with strong password validation
  - Password requirements: 8+ chars, uppercase letter, numeric digit
  - Role-based access control (Customer & Admin)
  
- 🛍️ **Product Catalog Management**
  - Browse all electronics with detailed information
  - Real-time stock tracking
  - Discount system with percentage-based pricing
  - Product image metadata support
  
- 🛒 **Shopping Cart System**
  - Add/remove items with quantity validation
  - Real-time cart management
  - Two pricing modes: Lock-at-add and Recalculate-at-checkout
  
- 💳 **Order Management**
  - Complete checkout workflow with inventory validation
  - Automatic stock deduction on purchase
  - Atomic transactions with rollback capability
  - Order history tracking
  
- 👨‍💼 **Admin Panel** - Comprehensive management tools
  - Product CRUD operations with bulk stock management
  - User management and role assignment
  - Advanced discount configuration
  - System reporting (Console & CSV exports)
  
- 💾 **Data Persistence**
  - CSV-based data initialization (CsvBootstrapInitializer)
  - In-Memory repository pattern abstraction
  - Atomic operations with transactional rollback

- 🎨 **Multi-UI Support**
  - Console version for terminal-based interaction
  - JavaFX GUI for modern desktop application experience

## 🏗️ Architecture

The project follows a **4-layer architecture** pattern ensuring separation of concerns and maintainability:

```
src/com/university/shopping/
│
├── app/                    # Application state management
│   ├── AppState.java
│   └── Router.java         # SPA routing (JavaFX only)
│
├── model/                  # Data models and in-memory database
│   ├── Product.java        # Electronics product entity
│   ├── User.java           # User with role management
│   ├── Cart.java           # Shopping cart container
│   ├── Order.java          # Purchase order record
│   ├── OrderItem.java      # Line items in orders
│   └── MockDatabase.java   # In-memory data store
│
├── repository/             # Data access abstraction layer
│   ├── ProductRepository.java   # Product CRUD & stock operations
│   ├── UserRepository.java      # User CRUD & authentication
│   ├── CartRepository.java      # Cart persistence
│   └── OrderRepository.java     # Order persistence
│
├── service/                # Business logic & workflows
│   ├── AuthService.java         # Authentication & authorization
│   ├── ShopService.java         # Shopping operations & checkout
│   ├── AdminService.java        # Admin panel operations
│   ├── CsvBootstrapInitializer.java  # Data initialization
│   │
│   ├── pricing/             # Pricing strategies
│   │   ├── DiscountPolicy.java         # Strategy interface
│   │   ├── StandardDiscountPolicy.java # Default implementation
│   │   └── PricingModeConstants.java   # Pricing mode configuration
│   │
│   ├── report/              # Reporting services
│   │   ├── AbstractReportService.java   # Base report class
│   │   ├── ConsoleReportService.java    # Console output
│   │   └── CsvReportService.java        # CSV file export
│   │
│   ├── media/               # Media handling (JavaFX)
│   │   └── ProductImageService.java    # Product image management
│   │
│   └── ui/                  # UI utilities (JavaFX)
│
├── view/                   # Presentation layer
│   │
│   ├── Console (Console version)
│   │   ├── ConsoleUI.java          # Main UI controller
│   │   ├── contracts/
│   │   │   └── MenuActions.java    # Menu interface
│   │   └── screens/
│   │       ├── AuthScreen.java     # Login/Register UI
│   │       ├── CustomerScreen.java # Shopping UI
│   │       └── AdminScreen.java    # Admin UI
│   │
│   └── JavaFX (JavaFX version)
│       ├── layout/                # Layout components
│       │   └── AppShell.java      # Main application shell
│       ├── screens/               # Page components
│       ├── contracts/
│       │   └── ScreenContext.java # Context for screens
│       ├── renderer/
│       │   └── UiRenderer.java    # Component renderer
│       └── styles/
│           ├── app.css            # Application styles
│           └── theme.css          # Theme styles
│
├── Main.java               # Console application entry point
├── Launcher.java           # JavaFX application launcher
└── MainFx.java            # JavaFX application entry point
```

### Architecture Layers Explained

| Layer | Purpose | Responsibilities |
|-------|---------|------------------|
| **Model** | Data representation | Entity definitions, business object state |
| **Repository** | Data access abstraction | CRUD operations, persistence, query logic |
| **Service** | Business logic | Workflows, validations, orchestration, pricing |
| **View** | User interface | Console menus or JavaFX components, user interaction |

## 📊 Design Patterns & Principles

### Implemented Design Patterns
- ✅ **Repository Pattern** - Abstract data access layer from business logic
- ✅ **Service Layer Pattern** - Encapsulate and orchestrate business workflows
- ✅ **Strategy Pattern** - Pluggable pricing policies and report formats
- ✅ **Dependency Injection** - Loose coupling via constructor injection
- ✅ **MVC-inspired** - Clear separation of concerns
- ✅ **DAO (Data Access Object)** - Data persistence abstraction
- ✅ **Singleton-like** - MockDatabase centralized state management
- ✅ **Template Method** - AbstractReportService for report generation
- ✅ **Router Pattern** - SPA-style routing in JavaFX version

### SOLID Principles Applied
- **S**ingle Responsibility - Each class has one reason to change
- **O**pen/Closed - Open for extension (new report types, UI versions), closed for modification
- **L**iskov Substitution - Report services and UI components are interchangeable
- **I**nterface Segregation - MenuActions and ScreenContext interfaces focus on specific concerns
- **D**ependency Inversion - High-level modules don't depend on low-level modules

## 🔄 Core Workflows

### User Registration & Purchase Flow
```
Start App → Browse Products → Add to Cart → 
Forced Login (if not authenticated) → Register → 
Proceed to Checkout → Validate Stock → 
Deduct Inventory → Create Order → Confirmation
```

### Checkout Process with Transactional Safety
```
View Cart → Validate ALL Stock → 
Begin Transaction → Update Stock → 
Create Order → Clear Cart → 
Transaction Success OR Complete Rollback
```

### Admin Product Management
```
Login as Admin → Access Admin Panel → 
Manage Products (CRUD) → Update Stock → 
Set Discounts → Manage Users → 
Export Reports → Logout
```

## 🎯 Advanced Features

### Pricing System
- **Flexible Pricing Modes**:
  - `LOCK_AT_ADD`: Price fixed when item added to cart
  - `RECALCULATE_AT_CHECKOUT`: Recalculates with current discounts at checkout
  
- **Discount Policy**:
  - Implements Strategy pattern for extensibility
  - Standard discount calculation: `finalPrice = price * (1 - discount% / 100)`
  - Safe bounds checking (0-100% discount validation)

### Reporting Services
- **Console Reports**: Real-time system statistics displayed in terminal
- **CSV Reports**: Exportable data for external analysis
- **Pluggable Architecture**: Add new report formats without modifying core code

### Transaction Safety
- Stock validation before checkout
- Atomic inventory updates with rollback capability
- Prevents double-booking and stock inconsistencies

### Multi-UI Architecture
- **Console Version**: Traditional text-based interface with full feature parity
- **JavaFX GUI Version**: Modern graphical interface with reactive components and styling
- **Shared Services**: Both versions share the same business logic and data models

## 🚀 Getting Started

### Requirements

- **Java 17+** (or Java 11+ with compatibility flags)
- **JavaFX SDK 17+** (for JavaFX version only)
- **IDE**: IntelliJ IDEA, Eclipse, VS Code with Java extensions, or NetBeans
- **Build Tool** (optional): Maven, Gradle, or javac CLI
- **No external dependencies** (except JavaFX for GUI version) - Pure Java standard library

### Quick Start

#### 📱 Option 1: Run Console Version (Text-based Interface)

From IDE (IntelliJ IDEA):
1. Open project folder in IntelliJ IDEA
2. Navigate to: `src/com/university/shopping/Main.java`
3. Click **Run** or press `Shift + F10`

From Terminal:
```bash
# Navigate to project directory
cd electronics-shop-java

# Compile all Java files
javac -d out src/com/university/shopping/**/*.java

# Run the console application
java -cp out com.university.shopping.Main
```

With Gradle:
```bash
./gradlew runConsole
```

With Maven:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.university.shopping.Main"
```

---

#### 🎨 Option 2: Run JavaFX GUI Version (Graphical Interface)

From IDE (IntelliJ IDEA):
1. Open project folder in IntelliJ IDEA
2. Navigate to: `src/com/university/shopping/Launcher.java`
3. Click **Run** or press `Shift + F10`

**Note**: Make sure JavaFX is configured in your IDE:
- **IntelliJ**: Add JavaFX SDK via Project Structure → Libraries
- **Eclipse**: Install e(fx)clipse plugin
- **VS Code**: Install Extension Pack for Java

From Terminal:
```bash
# Navigate to project directory
cd electronics-shop-java

# Compile all Java files (including JavaFX classes)
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml \
      -d out src/com/university/shopping/**/*.java

# Run the JavaFX application via Launcher
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml \
     -cp out com.university.shopping.Launcher
```

With Gradle:
```bash
./gradlew runFx
```

With Maven:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.university.shopping.Launcher"
```

**Replace `/path/to/javafx-sdk` with your actual JavaFX SDK installation path.**

---

### Which Version Should I Use?

| Feature | Console | JavaFX GUI |
|---------|---------|-----------|
| **Setup Complexity** | ⭐ Easier | ⭐⭐ Requires JavaFX |
| **Learning Focus** | Business logic, architecture | UI design, reactive patterns |
| **User Experience** | Text-based menus | Modern graphical interface |
| **Performance** | Minimal overhead | Full GUI overhead |
| **Best For** | Learning architecture patterns | Understanding UI frameworks |

## 🔓 Demo Accounts

Pre-configured user accounts in `MockDatabase`:

| Username | Password | Role | Purpose |
|----------|----------|------|---------|
| `admin` | `admin123` | Admin | System administration |
| `user1` | `pass123` | Customer | Testing purchases |
| `user2` | `pass123` | Customer | Testing purchases |

**Note**: You can register new accounts through the application's registration flow.

## 🧪 Testing the Application

### Customer User Journey
1. **Start application** - Welcome screen appears
2. **Login** or **Register** new account
3. **Browse products** - View all available electronics
4. **Add to cart** - Select products and quantities
5. **View cart** - Review items before checkout
6. **Checkout** - Complete purchase with stock validation
7. **View order confirmation** - Order successfully created

### Admin User Journey
1. **Login as admin** - Use admin credentials
2. **Access admin panel** - Manage system
3. **Product management**:
   - Add new products
   - Update prices and descriptions
   - Set discounts (e.g., 20% off)
   - Manage stock levels
4. **User management**:
   - Create new users
   - Update user roles
   - View all system users
5. **Generate reports**:
   - Console report (display to terminal)
   - CSV report (export to file)
6. **Logout** - Return to main menu

## 📚 Code Highlights

### Example: Checkout with Transaction Safety
The `ShopService.checkout()` method demonstrates professional transaction handling:
```java
// Validate ALL items exist and have sufficient stock
for (OrderItem item : items) {
    Product product = productRepository.findById(item.getProductId());
    if (product == null || item.getQuantity() > product.getStockQuantity()) {
        return "STOCK_ERROR"; // Fail fast
    }
}

// Update stock for all items
for (OrderItem item : items) {
    if (!productRepository.updateStock(productId, newStock)) {
        rollbackStockUpdates(...); // Atomic rollback on failure
        return "PERSISTENCE_ERROR";
    }
}

// Create order
if (!orderRepository.save(order)) {
    rollbackStockUpdates(...); // Ensure consistency
    return "PERSISTENCE_ERROR";
}
```

### Example: Dependency Injection in Main (Console)
```java
public class Main {
    public static void main(String[] args) {
        // Explicit dependency injection - easy to test and modify
        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();
        DiscountPolicy discountPolicy = new StandardDiscountPolicy();
        
        AuthService authService = new AuthService(userRepository);
        ShopService shopService = new ShopService(
            productRepository, orderRepository, cartRepository, 
            authService, discountPolicy
        );
        
        ConsoleUI ui = new ConsoleUI(authService, shopService, adminService);
        ui.start();
    }
}
```

### Example: JavaFX Application Initialization
```java
public class MainFx extends Application {
    @Override
    public void start(Stage stage) {
        initializeServices();    // Setup all business logic
        buildAndShowSPA(stage);  // Build and display UI
    }

    private void initializeServices() {
        // All services initialized here with dependency injection
        this.appState = new AppState();
        this.authService = new AuthService(userRepository, appState);
        // ... more services
    }
}
```

## 🔐 Security Considerations

- **Password Validation**:
  - Minimum 8 characters
  - At least one uppercase letter
  - At least one numeric digit
  - Prevents weak passwords

- **Role-Based Access Control**:
  - Admin and Customer roles
  - Service methods check authorization
  - Self-deletion prevention for admins

- **Data Integrity**:
  - Transactional operations with rollback
  - Stock validation before deduction
  - Immutable entity IDs (private final fields)

## 📖 Learning Outcomes

This project demonstrates mastery of:

- ✅ Object-Oriented Programming (OOP) principles
- ✅ SOLID design principles and clean architecture
- ✅ Design pattern implementation (Strategy, Repository, DAO, DI)
- ✅ Separation of concerns (layered architecture)
- ✅ Data persistence abstraction
- ✅ Business logic encapsulation
- ✅ Transaction management and atomic operations
- ✅ Role-based access control
- ✅ Advanced pricing strategies
- ✅ Professional reporting systems
- ✅ User authentication and validation
- ✅ Error handling and recovery
- ✅ Multi-UI implementation (Console & JavaFX)
- ✅ GUI frameworks and reactive patterns (JavaFX)

## 🚀 Possible Future Enhancements

### Database Integration
- [ ] Replace MockDatabase with **PostgreSQL/MySQL**
- [ ] Implement **JPA/Hibernate ORM**
- [ ] Add database migrations (Liquibase, Flyway)
- [ ] Connection pooling

### Web & API Layer
- [ ] **Spring Boot REST API** for web/mobile clients
- [ ] **JWT authentication** for stateless sessions
- [ ] **OAuth2** for third-party integrations
- [ ] **WebSocket** for real-time notifications

### UI Enhancements
- [ ] **Web UI** (React, Vue.js, or Angular)
- [ ] Mobile app (React Native, Flutter, or Android/iOS native)
- [ ] Progressive Web App (PWA)
- [ ] Dark mode for JavaFX GUI
- [ ] Internationalization (i18n) support

### Quality & Testing
- [ ] **Unit tests** (JUnit 5)
- [ ] **Integration tests** (Testcontainers)
- [ ] **End-to-end tests** (Selenium, Cucumber)
- [ ] **CI/CD pipeline** (GitHub Actions, Jenkins)
- [ ] **Code coverage** reports (JaCoCo)
- [ ] **Performance testing** (JMH, load testing)

### Advanced Features
- [ ] **Full-text search** on products
- [ ] Product reviews & 5-star ratings
- [ ] Wishlist & comparison functionality
- [ ] Payment gateway integration (Stripe, PayPal)
- [ ] Email notifications (order confirmation, shipping)
- [ ] Inventory management alerts
- [ ] Customer analytics & insights
- [ ] Multi-currency support

### DevOps & Deployment
- [ ] Docker containerization
- [ ] Kubernetes orchestration
- [ ] Cloud deployment (AWS, Azure, GCP)
- [ ] Monitoring and logging (ELK stack)
- [ ] Performance optimization

## 📁 Project Structure

```
electronics-shop-java/
├── src/com/university/shopping/
│   ├── app/                 # State management & routing
│   ├── model/               # Data models
│   ├── repository/          # Data access layer
│   ├── service/             # Business logic
│   ├── view/                # UI components (Console & JavaFX)
│   ├── Main.java            # Console app entry point
│   ├── Launcher.java        # JavaFX launcher
│   └── MainFx.java          # JavaFX app entry point
├── out/                     # Compiled bytecode
├── .idea/                   # IntelliJ IDEA configuration
├── electronics.iml          # Module file
├── README.md                # This file
├── index.html               # Architecture documentation
└── .gitignore
```

## 🎓 Use Cases & Applications

- **Learning OOP Concepts** - Study professional code organization
- **Design Pattern Training** - Understand enterprise patterns in practice
- **Interview Preparation** - Demonstrate architectural knowledge
- **Teaching Reference** - Use as curriculum example for students
- **Springboard Project** - Base for more advanced applications
- **UI Framework Learning** - Understand JavaFX and GUI development
- **Refactoring Exercise** - Practice improving existing code
- **Testing Practice** - Foundation for unit/integration tests

## 🤝 Contributing

This is an open educational project. Contributions welcome:

- 🔧 Fork and extend with new features
- 📝 Create pull requests with improvements
- 🧪 Add unit and integration tests
- 🏗️ Refactor with additional design patterns
- 📚 Enhance documentation
- 🐛 Report and fix bugs
- ⚡ Optimize performance

**Guidelines**:
- Follow existing code style and patterns
- Add tests for new features
- Update documentation
- Maintain layer separation
- Test both Console and JavaFX versions

## 📝 License

Educational project - use freely for learning, teaching, and non-commercial purposes.

## 👨‍💻 Author

Student Software Engineering Project - Demonstrating Professional Java Development

---

## 📌 Quick Tips

**For Developers:**
- The code is heavily commented - start with `Main.java` or `Launcher.java`
- Study the layered structure; it's a professional pattern used in enterprise applications
- Try adding a new discount policy by implementing `DiscountPolicy` interface
- Extend reporting with new report formats
- Compare Console and JavaFX implementations to understand different UI paradigms

**For Students:**
- Review architecture layers to understand separation of concerns
- Analyze `ShopService.checkout()` for transaction safety patterns
- Study dependency injection in `Main.java` and `MainFx.java`
- Examine how repositories abstract data access
- Learn how `Router` and `UiRenderer` implement SPA patterns in JavaFX

**Choosing Your Version:**
- **New to Java?** Start with Console version (`Main.java`)
- **Learning GUI frameworks?** Start with JavaFX version (`Launcher.java`)
- **Advanced learner?** Study both to see shared architecture differences

**Need Help?**
- Check the inline code comments for implementation details
- Review the architecture layers section above
- Look at design patterns section for pattern explanations
- Study the service layer for business logic organization
- Compare the two Main classes to see architectural differences

**Recommended Reading Order:**

*For Console Version:*
1. `Main.java` - See the big picture with dependency injection
2. `ConsoleUI.java` - Understand the user interaction layer
3. `ShopService.java` - Study the business logic and transaction handling
4. `Repository` classes - Learn data access abstraction
5. `Model` classes - Understand entity design

*For JavaFX Version:*
1. `Launcher.java` → `MainFx.java` - JavaFX app initialization
2. `AppShell.java` - Main layout and routing
3. `ScreenContext.java` & `UiRenderer.java` - UI rendering and navigation
4. Individual screen components - Learn component architecture
5. `service/` layer - Same business logic as console version

---

**Happy Coding! 🚀**
