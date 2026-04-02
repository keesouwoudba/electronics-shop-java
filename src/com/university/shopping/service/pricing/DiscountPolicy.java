package com.university.shopping.service.pricing;

import com.university.shopping.model.Product;

public interface DiscountPolicy {
	double apply(Product product);
}
