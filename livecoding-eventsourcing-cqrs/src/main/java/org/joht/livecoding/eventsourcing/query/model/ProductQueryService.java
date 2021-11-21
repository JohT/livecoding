package org.joht.livecoding.eventsourcing.query.model;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.joht.livecoding.eventsourcing.Product;
import org.joht.livecoding.eventsourcing.message.query.ProductQuery;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductQueryService {

	@Inject
	QueryGateway queryGateway;

	public Optional<Product> getProduct(String productId) {
		ProductQuery query = new ProductQuery(productId);
		try {
			return queryGateway.query(query, ResponseTypes.optionalInstanceOf(Product.class)).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new IllegalStateException(e);
		}
	}
}