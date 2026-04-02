package com.university.shopping.service.report;

import com.university.shopping.repository.OrderRepository;
import com.university.shopping.repository.ProductRepository;
import com.university.shopping.repository.UserRepository;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractReportService {
	protected final ProductRepository productRepository;
	protected final UserRepository userRepository;
	protected final OrderRepository orderRepository;

	public AbstractReportService(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository) {
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}

	public String buildTimestamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	public String formatCurrency(double amount) {
		DecimalFormat df = new DecimalFormat("0.00");
		return "$" + df.format(amount);
	}

	public String buildHeader(String title) {
		return "===== " + title + " =====";
	}

	public abstract String exportReport();
}
