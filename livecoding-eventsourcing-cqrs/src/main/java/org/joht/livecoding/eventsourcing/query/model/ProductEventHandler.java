package org.joht.livecoding.eventsourcing.query.model;

import org.axonframework.eventhandling.EventHandler;
import org.joht.livecoding.eventsourcing.Product;
import org.joht.livecoding.eventsourcing.message.event.ProductCreatedEvent;
import org.joht.livecoding.eventsourcing.message.event.ProductNameChangedEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductEventHandler {

	@Inject
	ProductRepository productRepository;

	@EventHandler
	void onCreated(ProductCreatedEvent event) {
		productRepository.create(event.getProductId());
	}

	@EventHandler
	void onChanged(ProductNameChangedEvent event) {
		Product product = productRepository
				.read(event.getProductId())
				.orElseThrow(() -> notFound(event.getProductId()))
				.withName(event.getName());
		productRepository.update(product);
	}

	private static IllegalArgumentException notFound(String id) {
		return new IllegalArgumentException("Product with id " + id + " not found.");
	}
}