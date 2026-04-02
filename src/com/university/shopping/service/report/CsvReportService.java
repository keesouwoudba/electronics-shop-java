package com.university.shopping.service.report;

import com.university.shopping.model.Order;
import com.university.shopping.repository.OrderRepository;
import com.university.shopping.repository.ProductRepository;
import com.university.shopping.repository.UserRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvReportService extends AbstractReportService {
	private final String outputPath;

	public CsvReportService(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository,
							String outputPath) {
		super(productRepository, userRepository, orderRepository);
		this.outputPath = outputPath;
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

		String csvText = "timestamp,productCount,userCount,orderCount,revenue\n"
				+ escapeCsv(buildTimestamp()) + ","
				+ productCount + ","
				+ userCount + ","
				+ orderCount + ","
				+ escapeCsv(formatCurrency(revenue)) + "\n";

		try {
			File file = new File(outputPath);
			File parent = file.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}

			FileWriter writer = new FileWriter(file);
			writer.write(csvText);
			writer.close();

			return "SUCCESS:" + file.getAbsolutePath();
		} catch (IOException e) {
			return "ERROR:" + e.getMessage();
		}
	}

	private String escapeCsv(String value) {
		if (value == null) {
			return "";
		}
		boolean shouldQuote = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
		String escaped = value.replace("\"", "\"\"");
		return shouldQuote ? "\"" + escaped + "\"" : escaped;
	}
}
