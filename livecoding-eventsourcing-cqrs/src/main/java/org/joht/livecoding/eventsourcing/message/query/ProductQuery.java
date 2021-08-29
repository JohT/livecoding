package org.joht.livecoding.eventsourcing.message.query;

import java.util.Objects;

public class ProductQuery {

	private final String productId;

	public static final ProductQuery ofId(String productId) {
		return new ProductQuery(productId);
	}

	protected ProductQuery(String productId) {
		this.productId = productId.trim();
	}

	public String getProductId() {
		return productId;
	}
	
	@Override
	public boolean equals(final Object other) {
		if ((other == null) || !getClass().equals(other.getClass())) {
			return false;
		}
		ProductQuery castOther = (ProductQuery) other;
		return Objects.equals(productId, castOther.productId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId);
	}
	
	@Override
	public String toString() {
		return "ProductQuery [productId=" + productId + "]";
	}
}