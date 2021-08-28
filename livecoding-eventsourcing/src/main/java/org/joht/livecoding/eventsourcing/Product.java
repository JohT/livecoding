package org.joht.livecoding.eventsourcing;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class Product {

	private final String productId;
	private final String name;

	@ConstructorProperties({ "productId", "name" })
	public Product(String productId, String name) {
		this.productId = productId;
		this.name = name;
	}

	public String getProductId() {
		return productId;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId);
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Product other = (Product) obj;
		return Objects.equals(name, other.name) && Objects.equals(productId, other.productId);
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + "]";
	}
}