package com.university.shopping.service.report;

import com.university.shopping.model.Order;
import com.university.shopping.repository.OrderRepository;
import com.university.shopping.repository.ProductRepository;
import com.university.shopping.repository.UserRepository;

public class ConsoleReportService extends AbstractReportService {
	public ConsoleReportService(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository) {
		super(productRepository, userRepository, orderRepository);
	}

	@Override
	public String exportReport() {
		int productCount = productRepository.getProductCount();
		int userCount = userRepository.getUserCount();
		int orderCount = orderRepository.getOrderCount();

		double revenue = 0;
		Order[] orders = orderRepository.findAll();
		for (Order order : orders) {
			if (order != null) {
				revenue += order.getTotalPrice();
			}
		}

		System.out.println("\n" + buildHeader("SYSTEM REPORT"));
		System.out.println("Generated At: " + buildTimestamp());
		System.out.println("Products:     " + productCount);
		System.out.println("Users:        " + userCount);
		System.out.println("Orders:       " + orderCount);
		System.out.println("Revenue:      " + formatCurrency(revenue));
		return "SUCCESS:Console report exported";
	}
}
