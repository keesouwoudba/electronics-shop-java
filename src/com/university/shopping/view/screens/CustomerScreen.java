package com.university.shopping.view.screens;

import com.university.shopping.model.Cart;
import com.university.shopping.model.OrderItem;
import com.university.shopping.model.Product;
import com.university.shopping.service.AdminService;
import com.university.shopping.service.AuthService;
import com.university.shopping.service.ShopService;
import com.university.shopping.view.contracts.MenuActions;

import java.util.Scanner;

public class CustomerScreen extends AbstractScreen implements MenuActions {

	public CustomerScreen(Scanner scanner, AuthService authService, ShopService shopService, AdminService adminService) {
		super(scanner, authService, shopService, adminService);
	}

	@Override
	public void showMenu() {
		System.out.println("\n+-----------------------------------+");
		System.out.println("|          CUSTOMER MENU           |");
		System.out.println("+-----------------------------------+");
		System.out.println("| 1. Browse All Products           |");
		System.out.println("| 2. View Product Details          |");
		System.out.println("| 3. Add Product to Cart           |");
		System.out.println("| 4. View My Cart                  |");
		System.out.println("| 5. Remove Item from Cart         |");
		System.out.println("| 6. Checkout                      |");
		System.out.println("| 7. Logout                        |");
		System.out.println("+-----------------------------------+");
		System.out.print("Choose an option: ");
	}

	@Override
	public void handleOption(int option) {
		switch (option) {
			case 1:
				browseAllProducts();
				break;
			case 2:
				viewProductDetails();
				break;
			case 3:
				addProductToCart();
				break;
			case 4:
				viewCart();
				break;
			case 5:
				removeFromCart();
				break;
			case 6:
				checkout();
				break;
			case 7:
				authService.logout();
				System.out.println("\nLogged out successfully.");
				pause();
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
		}
	}

	private void browseAllProducts() {
		printTitle("AVAILABLE PRODUCTS");

		Product[] products = shopService.getAllProducts();
		if (products == null || products.length == 0) {
			System.out.println("No products available.");
		} else {
			displayProductTable(products);
		}

		pause();
	}

	private void viewProductDetails() {
		System.out.print("\nEnter Product ID to view details: ");
		int productId = readIntSafe();

		Product product = shopService.getProductById(productId);
		if (product == null) {
			System.out.println("Product not found.");
		} else {
			printTitle("PRODUCT DETAILS");
			System.out.println("ID:          " + product.getProductId());
			System.out.println("Name:        " + product.getName());
			System.out.println("Category:    " + product.getCategory());
			System.out.println("Description: " + product.getDescription());
			System.out.println("Price:       $" + String.format("%.2f", product.getPrice()));
			System.out.println("Stock:       " + product.getStockQuantity() + " units");

			if (product.isDiscounted()) {
				System.out.println("Discount:    " + product.getDiscountPercentage() + "% OFF");
				System.out.println("Final Price: $" + String.format("%.2f", product.getFinalPrice()));
			}
		}

		pause();
	}

	private void addProductToCart() {
		System.out.print("\nEnter Product ID: ");
		int productId = readIntSafe();

		System.out.print("Enter Quantity: ");
		int quantity = readIntSafe();

		if (quantity <= 0) {
			System.out.println("Quantity must be positive.");
			pause();
			return;
		}

		int userId = authService.getCurrentUser().getUserId();
		boolean result = shopService.addToCart(productId, quantity, userId);
		if (result) {
			System.out.println("Product added to cart successfully.");
		} else {
			System.out.println("Failed to add product. Product may not exist or insufficient stock.");
		}

		pause();
	}

	private void viewCart() {
		int userId = authService.getCurrentUser().getUserId();
		Cart cart = shopService.viewCart(userId);

		if (cart == null || cart.getItemCount() == 0) {
			System.out.println("\nYour cart is empty.");
		} else {
			printTitle("YOUR CART");
			OrderItem[] items = cart.getItems();

			System.out.println("+------+-------------------------+----------+------------+------------+");
			System.out.println("|  ID  |      Product Name       | Quantity |   Price    |   Total    |");
			System.out.println("+------+-------------------------+----------+------------+------------+");

			for (int i = 0; i < cart.getItemCount(); i++) {
				if (items[i] != null) {
					OrderItem item = items[i];
					double itemTotal = item.getQuantity() * item.getPriceAtPurchase();
					System.out.printf("| %-4d | %-23s | %-8d | $%-9.2f | $%-9.2f |%n",
							item.getProductId(),
							truncate(item.getProductName(), 23),
							item.getQuantity(),
							item.getPriceAtPurchase(),
							itemTotal);
				}
			}

			System.out.println("+------+-------------------------+----------+------------+------------+");
			System.out.printf("\nTOTAL: $%.2f%n", cart.getTotalPrice());
		}

		pause();
	}

	private void removeFromCart() {
		System.out.print("\nEnter Product ID to remove: ");
		int productId = readIntSafe();

		int userId = authService.getCurrentUser().getUserId();
		boolean result = shopService.removeFromCart(productId, userId);
		if (result) {
			System.out.println("Product removed from cart.");
		} else {
			System.out.println("Failed to remove product.");
		}

		pause();
	}

	private void checkout() {
		int userId = authService.getCurrentUser().getUserId();
		Cart cart = shopService.viewCart(userId);

		if (cart == null || cart.getItemCount() == 0) {
			System.out.println("\nYour cart is empty. Add items before checkout.");
			pause();
			return;
		}

		printTitle("CHECKOUT");
		System.out.printf("Total Amount: $%.2f%n", cart.getTotalPrice());
		System.out.print("\nConfirm purchase? (yes/no): ");
		String confirm = scanner.nextLine();

		if (confirm.equalsIgnoreCase("yes")) {
			String result = shopService.checkout(userId);
			if (result.equals("Success")) {
				System.out.println("\nOrder placed successfully.");
				System.out.println("Thank you for your purchase.");
			} else {
				System.out.println("\nCheckout failed: " + result);
			}
		} else {
			System.out.println("\nCheckout cancelled.");
		}

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
}
