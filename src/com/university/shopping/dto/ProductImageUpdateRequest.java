package com.university.shopping.dto;

public class ProductImageUpdateRequest {
	private final int productId;
	private final String sourceFilePath;
	private final String imageName;

	public ProductImageUpdateRequest(int productId, String sourceFilePath, String imageName) {
		this.productId = productId;
		this.sourceFilePath = sourceFilePath;
		this.imageName = imageName;
	}

	public int getProductId() {
		return productId;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public String getImageName() {
		return imageName;
	}
}
