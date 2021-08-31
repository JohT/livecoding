package org.joht.livecoding.eventsourcing.query.model;

import java.util.Optional;

import org.axonframework.queryhandling.QueryHandler;
import org.joht.livecoding.eventsourcing.Product;
import org.joht.livecoding.eventsourcing.message.query.ProductQuery;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductQueryHandler {

	@Inject
	ProductRepository productRepository;
	
	@QueryHandler
	Optional<Product> getProduct(ProductQuery query) {
		return productRepository.read(query.getProductId());
	}
}