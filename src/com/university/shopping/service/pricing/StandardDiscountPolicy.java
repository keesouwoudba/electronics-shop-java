package com.university.shopping.service.pricing;

import com.university.shopping.model.Product;

public class StandardDiscountPolicy implements DiscountPolicy {
	@Override
	public double apply(Product product) {
		if (product == null) {
			return 0;
		}
		return product.getFinalPrice();
	}
}
