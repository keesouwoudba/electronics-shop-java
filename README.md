
# Electronics Shop (Java Console Application)

A simple **console-based electronics shop** written in Java. The project demonstrates a layered architecture (Model–Repository–Service–View) with basic authentication, product browsing, cart management, and order placement.

## ✨ Features

* User authentication (login/register)
* Product catalog
* Shopping cart (add/remove items)
* Order creation
* Admin capabilities (manage products)
* Console UI (text-based interface)

## 🧱 Architecture

The project follows a layered structure:

```
src/com/university/shopping
│
├── model        # Core data classes (Product, User, Cart, Order, etc.)
├── repository   # Data access layer (simulated with MockDatabase)
├── service      # Business logic (AuthService, ShopService, AdminService)
├── view         # Console UI (ConsoleUI)
└── Main.java    # Application entry point
```

### Layer Responsibilities

* **Model** — Data structures used across the app
* **Repository** — CRUD operations and data storage abstraction
* **Service** — Business logic and workflows
* **View** — User interaction through console

## 📦 Main Classes

* `Product` — Represents an item in the shop
* `User` — Represents a registered user
* `Cart` — Stores selected products
* `Order` & `OrderItem` — Represent completed purchases
* `MockDatabase` — In‑memory storage

## ▶️ How to Run

### Requirements

* Java 17+ (or Java 11+ depending on your setup)
* IntelliJ IDEA / any Java IDE

### Run from IDE

1. Open the project in IntelliJ IDEA
2. Navigate to:

```
src/com/university/shopping/Main.java
```

3. Run the `Main` class

### Run from Terminal

```bash
javac -d out src/com/university/shopping/Main.java
java -cp out com.university.shopping.Main
```

## 🔐 Demo Accounts

If preconfigured in `MockDatabase`, you may use sample users for testing.

## 🎯 Purpose

This project was created for educational purposes to practice:

* Object-Oriented Programming (OOP)
* Layered architecture
* Repository & Service patterns
* Console application design

## 🚀 Possible Improvements

* Replace MockDatabase with a real database (MySQL/PostgreSQL)
* Add GUI (JavaFX/Swing)
* Implement REST API (Spring Boot)
* Add unit tests (JUnit)

## 👨‍💻 Author

Student Software Engineering Project

---

If you find this project useful, feel free to fork and improve it.
