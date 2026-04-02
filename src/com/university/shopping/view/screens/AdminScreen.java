package com.university.shopping.view.screens;

import com.university.shopping.model.Product;
import com.university.shopping.model.User;
import com.university.shopping.service.AdminService;
import com.university.shopping.service.AuthService;
import com.university.shopping.service.ShopService;
import com.university.shopping.view.contracts.MenuActions;

import java.util.Scanner;

public class AdminScreen extends AbstractScreen implements MenuActions {

	public AdminScreen(Scanner scanner, AuthService authService, ShopService shopService, AdminService adminService) {
		super(scanner, authService, shopService, adminService);
	}

	@Override
	public void showMenu() {
		System.out.println("\n+-----------------------------------+");
		System.out.println("|           ADMIN PANEL            |");
		System.out.println("+-----------------------------------+");
		System.out.println("| 1. Product Management            |");
		System.out.println("| 2. User Management               |");
		System.out.println("| 3. View All Products             |");
		System.out.println("| 4. View All Users                |");
		System.out.println("| 5. Export Report                 |");
		System.out.println("| 6. Logout                        |");
		System.out.println("+-----------------------------------+");
		System.out.print("Choose an option: ");
	}

	@Override
	public void handleOption(int option) {
		switch (option) {
			case 1:
				productManagementMenu();
				break;
			case 2:
				userManagementMenu();
				break;
			case 3:
				viewAllProducts();
				break;
			case 4:
				viewAllUsers();
				break;
			case 5:
					exportReport();
					break;
				case 6:
				authService.logout();
				System.out.println("\nLogged out successfully.");
				pause();
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
		}
	}

	private void productManagementMenu() {
		System.out.println("\n+-----------------------------------+");
		System.out.println("|       PRODUCT MANAGEMENT         |");
		System.out.println("+-----------------------------------+");
		System.out.println("| 1. Add New Product               |");
		System.out.println("| 2. Update Product Price          |");
		System.out.println("| 3. Update Product Name           |");
		System.out.println("| 4. Set Product Discount          |");
		System.out.println("| 5. Add Stock                     |");
		System.out.println("| 6. Remove Stock                  |");
		System.out.println("| 7. Delete Product                |");
		System.out.println("| 8. Back to Admin Menu            |");
		System.out.println("+-----------------------------------+");
		System.out.print("Choose an option: ");

		int choice = readIntSafe();
		switch (choice) {
			case 1:
				addNewProduct();
				break;
			case 2:
				updateProductPrice();
				break;
			case 3:
				updateProductName();
				break;
			case 4:
				setProductDiscount();
				break;
			case 5:
				addStock();
				break;
			case 6:
				removeStock();
				break;
			case 7:
				deleteProduct();
				break;
			case 8:
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
		}
	}

	private void addNewProduct() {
		printTitle("ADD NEW PRODUCT");

		System.out.print("Product Name: ");
		String name = scanner.nextLine();

		System.out.print("Price: $");
		double price = readDoubleSafe();

		System.out.print("Category: ");
		String category = scanner.nextLine();

		System.out.print("Description: ");
		String description = scanner.nextLine();

		System.out.print("Initial Stock Quantity: ");
		int stock = readIntSafe();

		System.out.print("Is Discounted? (true/false): ");
		boolean isDiscounted = scanner.nextLine().equalsIgnoreCase("true");

		double discountPercentage = 0;
		if (isDiscounted) {
			System.out.print("Discount Percentage: ");
			discountPercentage = readDoubleSafe();
		}

		Product newProduct = new Product(name, price, category, description, stock, isDiscounted, discountPercentage);
		String result = adminService.addNewProduct(newProduct);

		if ("SUCCESS".equals(result)) {
			System.out.println("Product added successfully.");
		} else {
			System.out.println("Failed to add product: " + result);
		}

		pause();
	}

	private void updateProductPrice() {
		System.out.print("\nEnter Product ID: ");
		int productId = readIntSafe();

		System.out.print("Enter New Price: $");
		double newPrice = readDoubleSafe();

		String result = adminService.updateProductPrice(productId, newPrice);
		if ("SUCCESS".equals(result)) {
			System.out.println("Product price updated successfully.");
		} else {
			System.out.println("Failed to update price: " + result);
		}

		pause();
	}

	private void updateProductName() {
		System.out.print("\nEnter Product ID: ");
		int productId = readIntSafe();

		System.out.print("Enter New Name: ");
		String newName = scanner.nextLine();

		String result = adminService.updateProductName(productId, newName);
		if ("SUCCESS".equals(result)) {
			System.out.println("Product name updated successfully.");
		} else {
			System.out.println("Failed to update name: " + result);
		}

		pause();
	}

	private void setProductDiscount() {
		System.out.print("\nEnter Product ID: ");
		int productId = readIntSafe();

		System.out.print("Enable Discount? (true/false): ");
		boolean isDiscounted = scanner.nextLine().equalsIgnoreCase("true");

		System.out.print("Discount Percentage: ");
		double discountPercentage = readDoubleSafe();

		String result = adminService.setProductDiscount(productId, isDiscounted, discountPercentage);
		if ("SUCCESS".equals(result)) {
			System.out.println("Discount updated successfully.");
		} else {
			System.out.println("Failed to update discount: " + result);
		}

		pause();
	}

	private void addStock() {
		System.out.print("\nEnter Product ID: ");
		int productId = readIntSafe();

		System.out.print("Quantity to Add: ");
		int quantity = readIntSafe();

		String result = adminService.addStockToExistingProduct(productId, quantity);
		if ("SUCCESS".equals(result)) {
			System.out.println("Stock added successfully.");
		} else {
			System.out.println("Failed to add stock: " + result);
		}

		pause();
	}

	private void removeStock() {
		System.out.print("\nEnter Product ID: ");
		int productId = readIntSafe();

		System.out.print("Quantity to Remove: ");
		int quantity = readIntSafe();

		String result = adminService.removeStock(productId, quantity);
		if ("SUCCESS".equals(result)) {
			System.out.println("Stock removed successfully.");
		} else {
			System.out.println("Failed to remove stock: " + result);
		}

		pause();
	}

	private void deleteProduct() {
		System.out.print("\nEnter Product ID to delete: ");
		int productId = readIntSafe();

		System.out.print("Are you sure? (yes/no): ");
		String confirm = scanner.nextLine();

		if (confirm.equalsIgnoreCase("yes")) {
			String result = adminService.deleteProduct(productId);
			if ("SUCCESS".equals(result)) {
				System.out.println("Product deleted successfully.");
			} else {
				System.out.println("Failed to delete product: " + result);
			}
		} else {
			System.out.println("Deletion cancelled.");
		}

		pause();
	}

	private void userManagementMenu() {
		System.out.println("\n+-----------------------------------+");
		System.out.println("|         USER MANAGEMENT          |");
		System.out.println("+-----------------------------------+");
		System.out.println("| 1. Add New User                  |");
		System.out.println("| 2. Update User                   |");
		System.out.println("| 3. Delete User                   |");
		System.out.println("| 4. Back to Admin Menu            |");
		System.out.println("+-----------------------------------+");
		System.out.print("Choose an option: ");

		int choice = readIntSafe();
		switch (choice) {
			case 1:
				addNewUser();
				break;
			case 2:
				updateUser();
				break;
			case 3:
				deleteUser();
				break;
			case 4:
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
		}
	}

	private void addNewUser() {
		printTitle("ADD NEW USER");

		System.out.print("Username: ");
		String username = scanner.nextLine();

		System.out.print("Password: ");
		String password = scanner.nextLine();

		System.out.print("Is Admin? (true/false): ");
		boolean isAdmin = scanner.nextLine().equalsIgnoreCase("true");

		String result = adminService.addUser(username, password, isAdmin);
		if ("SUCCESS".equals(result)) {
			System.out.println("User added successfully.");
		} else {
			System.out.println("Failed to add user: " + result);
		}

		pause();
	}

	private void updateUser() {
		System.out.print("\nEnter User ID: ");
		int userId = readIntSafe();

		System.out.print("New Username: ");
		String newUsername = scanner.nextLine();

		System.out.print("New Password: ");
		String newPassword = scanner.nextLine();

		System.out.print("Is Admin? (true/false): ");
		boolean isAdmin = scanner.nextLine().equalsIgnoreCase("true");

		String result = adminService.updateUser(userId, newUsername, newPassword, isAdmin);
		if ("SUCCESS".equals(result)) {
			System.out.println("User updated successfully.");
		} else {
			System.out.println("Failed to update user: " + result);
		}

		pause();
	}

	private void deleteUser() {
		System.out.print("\nEnter User ID to delete: ");
		int userId = readIntSafe();

		System.out.print("Are you sure? (yes/no): ");
		String confirm = scanner.nextLine();

		if (confirm.equalsIgnoreCase("yes")) {
			String result = adminService.deleteUser(userId);
			if ("SUCCESS".equals(result)) {
				System.out.println("User deleted successfully.");
			} else {
				System.out.println("Failed to delete user: " + result);
			}
		} else {
			System.out.println("Deletion cancelled.");
		}

		pause();
	}

	private void viewAllProducts() {
		printTitle("ALL PRODUCTS");

		Product[] products = adminService.getAllProducts();
		if (products == null || products.length == 0) {
			System.out.println("No products available.");
		} else {
			displayProductTable(products);
		}

		pause();
	}

	private void viewAllUsers() {
		printTitle("ALL USERS");

		User[] users = adminService.getAllUsers();
		if (users == null || users.length == 0) {
			System.out.println("No users found.");
		} else {
			System.out.println("+------+---------------------+-----------+--------------+");
			System.out.println("|  ID  |      Username       |  Is Admin | Created Date |");
			System.out.println("+------+---------------------+-----------+--------------+");

			for (User user : users) {
				if (user != null) {
					System.out.printf("| %-4d | %-19s | %-9s | %-12s |%n",
							user.getUserId(),
							truncate(user.getUsername(), 19),
							user.isAdmin() ? "Yes" : "No",
							user.getCreatedDate());
				}
			}

			System.out.println("+------+---------------------+-----------+--------------+");
		}

		pause();
	}

	private void exportReport() {
		System.out.print("\nEnter report format (console/csv): ");
		String format = scanner.nextLine();
		String result = adminService.exportSystemReport(format);
		System.out.println(result);
		pause();
	}

	private void displayProductTable(Product[] products) {
		System.out.println("+------+-------------------------+------------+----------+-----------+");
		System.out.println("|  ID  |          Name           |  Category  |  Price   |   Stock   |");
		System.out.println("+------+-------------------------+------------+----------+-----------+");

		for (Product product : products) {
			if (product != null) {
				System.out.printf("| %-4d | %-23s | %-10s | $%-7.2f | %-9d |%n",
						product.getProductId(),
						truncate(product.getName(), 23),
						truncate(product.getCategory(), 10),
						product.getPrice(),
						product.getStockQuantity());
			}
		}

		System.out.println("+------+-------------------------+------------+----------+-----------+");
	}

	private double readDoubleSafe() {
		while (true) {
			String input = scanner.nextLine();
			try {
				return Double.parseDouble(input.trim());
			} catch (NumberFormatException e) {
				System.out.print("Invalid input. Please enter a valid number: ");
			}
		}
	}
}
