package org.joht.livecoding.eventsourcing.command.model;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.joht.livecoding.eventsourcing.Product;
import org.joht.livecoding.eventsourcing.message.command.ChangeProductNameCommand;
import org.joht.livecoding.eventsourcing.message.command.CreateProductCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductService {

	@Inject
	CommandGateway commandGateway;

	/**
	 * Returns the id of the newly created product.
	 * @return {@link String} product id
	 */
	public String createProduct() {
		String newProductId = UUID.randomUUID().toString();
		return commandGateway.sendAndWait(new CreateProductCommand(newProductId));
	}

	/**
	 * Changes the name of the product with the given id.
	 * @param id {@link String}
	 * @param newName {@link String}
	 * @return Updated {@link Product}
	 */
	public Product updateProductName(String id, String newName) {
		return commandGateway.sendAndWait(new ChangeProductNameCommand(id, newName));
	}
}