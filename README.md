# Electronics Shop - Java Application

> A modern **layered architecture** electronics shop application demonstrating professional Java development patterns and best practices.

## 📋 Project Overview

This is an educational software engineering project showcasing a clean, layered architecture implementation of an e-commerce platform. The project includes comprehensive architecture documentation and is designed to teach software design patterns and enterprise Java development principles.

**Status**: Core architecture defined with detailed documentation in `index.html`

## ✨ Key Features

- 🔐 **User Authentication** - Login/Register with validation
- 🛍️ **Product Catalog** - Browse electronics with pagination
- 🛒 **Shopping Cart** - Add/remove items with real-time management
- 📦 **Order Management** - Complete checkout and order tracking
- 👨‍💼 **Admin Panel** - Product and user management
- 💾 **In-Memory Database** - MockDatabase with full CRUD operations

## 🏗️ Architecture

The project follows a **4-layer architecture** pattern:

```
src/com/university/shopping/
│
├── model/              # Data models and in-memory database
│   ├── Product.java
│   ├── User.java
│   ├── Cart.java
│   ├── Order.java
│   ├── OrderItem.java
│   └── MockDatabase.java
│
├── repository/         # Data access abstraction layer
│   ├── ProductRepository.java
│   ├── UserRepository.java
│   ├── CartRepository.java
│   └── OrderRepository.java
│
├── service/            # Business logic layer
│   ├── AuthService.java
│   ├── ShopService.java
│   └── AdminService.java
│
├── view/               # Presentation layer
│   └── ConsoleUI.java
│
└── Main.java           # Application entry point
```

### Layer Responsibilities

| Layer | Purpose | Examples |
|-------|---------|----------|
| **Model** | Data structures and storage | Product, User, Cart, Order, MockDatabase |
| **Repository** | CRUD operations & persistence abstraction | ProductRepository, UserRepository, CartRepository |
| **Service** | Business logic & workflows | Authentication, shopping, admin operations |
| **View** | User interface & interactions | Console menus, product listings, checkout flow |

## 📊 Design Patterns Used

- ✅ **Repository Pattern** - Abstract data access layer
- ✅ **Service Layer Pattern** - Encapsulate business logic
- ✅ **MVC-inspired** - Separation of concerns
- ✅ **DAO Pattern** - Data access objects
- ✅ **Singleton Pattern** - MockDatabase instance management

## 🎯 Core Workflows

### User Registration & Purchase Flow
```
Start App → Browse Products → Add to Cart → 
Forced Login → Register → Confirm Purchase → Order Created
```

### Checkout Process
```
View Cart → Validate Stock → Deduct Inventory → 
Create Order → Clear Cart → Confirmation
```

### Admin Operations
```
Login (Admin Account) → Access Admin Panel → 
Manage Products/Users → Update Statistics
```

## 📚 Detailed Documentation

**Complete architecture documentation is available in `index.html`**
- Full class specifications
- Method signatures and descriptions
- Data flows and sequences
- Critical workflows with pseudo-code
- Database schema and relationships

To view the documentation:
1. Open `index.html` in a web browser, or
2. Check the repository's GitHub Pages (if enabled)

## 🚀 Getting Started

### Requirements

- **Java 17+** (or Java 11+)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **No external dependencies** - Pure Java standard library

### Quick Start

#### From IDE (IntelliJ IDEA)

1. Open project folder in IntelliJ IDEA
2. Navigate to: `src/com/university/shopping/Main.java`
3. Click **Run** or press `Shift + F10`

#### From Terminal

```bash
# Compile
javac -d out src/com/university/shopping/**/*.java

# Run
java -cp out com.university.shopping.Main
```

#### From Gradle (if configured)

```bash
./gradlew run
```

## 🔓 Demo Accounts

Sample user accounts are configured in `MockDatabase`:

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | Admin |
| `user1` | `pass123` | Customer |
| `user2` | `pass123` | Customer |

*Note: Create new accounts through the registration flow*

## 🧪 Testing the Application

1. **Register** a new account
2. **Browse products** with pagination
3. **Add items** to cart
4. **Proceed to checkout** and complete purchase
5. **Login as admin** and manage products/users
6. **View statistics** and order history

## 📖 Learning Outcomes

This project demonstrates:

- ✅ Object-Oriented Programming (OOP) principles
- ✅ Clean code and architectural patterns
- ✅ Separation of concerns (layered architecture)
- ✅ Data persistence and in-memory databases
- ✅ Business logic encapsulation
- ✅ Console application development
- ✅ User authentication flows
- ✅ Error handling and validation

## 🔄 Data Models

### Product
```java
- productId (unique identifier)
- name, price, category
- description
- stockQuantity
- discount support (isDiscounted, discountPercentage)
- getFinalPrice() - calculates discounted price
```

### User
```java
- userId (auto-generated)
- username (unique)
- password (encrypted/validated)
- isAdmin (role flag)
```

### Cart
```java
- cartId
- userId (owner)
- items (OrderItem array)
- addItem(), removeItem(), clear()
```

### Order
```java
- orderId (auto-generated)
- userId (customer reference)
- orderDate (timestamp)
- totalPrice
- status
- items (OrderItem array)
```

## 🚀 Possible Future Enhancements

### Database Integration
- [ ] Replace MockDatabase with **MySQL/PostgreSQL**
- [ ] Implement JPA/Hibernate ORM
- [ ] Add database migrations (Flyway/Liquibase)

### API & Backend
- [ ] Build **REST API** with Spring Boot
- [ ] Add authentication (JWT, OAuth2)
- [ ] Implement caching (Redis)

### Frontend & UI
- [ ] **JavaFX GUI** application
- [ ] **Web UI** (React/Vue.js)
- [ ] Mobile app (Android/iOS)

### Quality & Testing
- [ ] Unit tests (JUnit 5)
- [ ] Integration tests (Testcontainers)
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Code coverage reports (JaCoCo)

### Features
- [ ] Product search & filtering
- [ ] User reviews & ratings
- [ ] Order history & tracking
- [ ] Wishlist functionality
- [ ] Payment gateway integration
- [ ] Email notifications

## 📁 Project Structure

```
electronics-shop-java/
├── src/com/university/shopping/
│   ├── model/
│   ├── repository/
│   ├── service/
│   ├── view/
│   └── Main.java
├── out/                        # Compiled output
├── .idea/                      # IntelliJ configuration
├── README.md                   # This file
├── index.html                  # Full documentation
├── electronics.iml             # Module file
└── .gitignore
```

## 🎓 Use Cases

- **Learning OOP** - Study clean code patterns
- **Design Patterns** - Repository, Service, DAO patterns
- **Interview Prep** - Demonstrate architectural knowledge
- **Teaching Tool** - Use as curriculum example
- **Springboard** - Base for more advanced projects

## 🤝 Contributing

This is an educational project. Feel free to:
- Fork and extend with new features
- Create pull requests with improvements
- Add tests and documentation
- Refactor with design patterns

## 📝 License

Educational project - use freely for learning purposes.

## 👨‍💻 Author

Student Software Engineering Project

---

**📌 Pro Tips:**
- Read `index.html` for detailed architecture documentation
- Study the layered structure - it's a professional pattern used in enterprise apps
- Try adding a persistence layer (Database) for learning
- Extend with REST API for web development practice

**Need Help?** Check the `index.html` documentation or review the architecture diagrams.
