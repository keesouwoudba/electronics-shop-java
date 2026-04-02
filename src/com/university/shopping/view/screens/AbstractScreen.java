package com.university.shopping.view.screens;

import com.university.shopping.service.AdminService;
import com.university.shopping.service.AuthService;
import com.university.shopping.service.ShopService;

import java.util.Scanner;

public abstract class AbstractScreen {
	protected final Scanner scanner;
	protected final AuthService authService;
	protected final ShopService shopService;
	protected final AdminService adminService;

	public AbstractScreen(Scanner scanner, AuthService authService, ShopService shopService, AdminService adminService) {
		this.scanner = scanner;
		this.authService = authService;
		this.shopService = shopService;
		this.adminService = adminService;
	}

	public void printTitle(String title) {
		System.out.println("\n===============================================================");
		System.out.println(title);
		System.out.println("===============================================================");
	}

	public void pause() {
		System.out.print("\nPress Enter to continue...");
		scanner.nextLine();
	}

	public int readIntSafe() {
		while (true) {
			String input = scanner.nextLine();
			try {
				return Integer.parseInt(input.trim());
			} catch (NumberFormatException e) {
				System.out.print("Invalid input. Please enter a valid number: ");
			}
		}
	}

	public String truncate(String value, int maxLength) {
		if (value == null) {
			return "";
		}
		if (maxLength <= 0) {
			return "";
		}
		if (value.length() <= maxLength) {
			return value;
		}
		if (maxLength <= 3) {
			return value.substring(0, maxLength);
		}
		return value.substring(0, maxLength - 3) + "...";
	}

	public abstract void showMenu();

	public abstract void handleOption(int option);
}
