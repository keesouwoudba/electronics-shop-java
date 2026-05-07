package com.university.shopping.service.pricing;

import com.university.shopping.model.Product;

public class SeasonalDiscountPolicy implements DiscountPolicy {
	private final double extraPercent;

	public SeasonalDiscountPolicy(double extraPercent) {
		this.extraPercent = Math.max(0, extraPercent);
	}

	@Override
	public double apply(Product product) {
		if (product == null) {
			return 0;
		}
		double combinedDiscount = product.getDiscountPercentage() + extraPercent;
		double clampedDiscount = Math.min(100.0, Math.max(0.0, combinedDiscount));
		return product.getPrice() * (1 - clampedDiscount / 100.0);
	}
}
