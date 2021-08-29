package org.joht.livecoding.eventsourcing.query.model;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.joht.livecoding.eventsourcing.Product;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository {

	// For demonstration purposes this is only a static map and not a real database.
	private static final Map<String, Product> products = new ConcurrentHashMap<>();
	
	public void create(String productId) {
		products.put(productId, Product.ofId(productId));
	}

	public Optional<Product> read(String productId) {
		return Optional.ofNullable(products.get(productId));
	}

	public Product update(Product product) {
		return products.computeIfPresent(product.getId(), (key, value) -> product);
	}
}