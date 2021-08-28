package org.joht.livecoding.eventsourcing;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.joht.livecoding.eventsourcing.message.ChangeProductNameCommand;
import org.joht.livecoding.eventsourcing.message.CreateProductCommand;
import org.joht.livecoding.eventsourcing.message.ReadProductCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductService {

	@Inject
	CommandGateway commands;

	public Product createProduct() {
		String newProductId = UUID.randomUUID().toString();
		commands.sendAndWait(new CreateProductCommand(newProductId));
		return new Product(newProductId, "");
	}

	/**
	 * While command processing, the current state of the aggregate is recreated by reading
	 * all its events from the event store. 
	 * <p> 
	 * This is only meant to be used for demonstration purposes and shouldn't be done here (on the command
	 * side) but rather on the query side (when CQRS is used in conjunction with eventsourcing). 
	 * For big event stores, aggregates with lots of events and many queries this would be far too slow.
	 * 
	 * @param produktId {@link String}
	 * @return {@link Product}
	 */
	public Product getProduct(String produktId) {
		return commands.sendAndWait(new ReadProductCommand(produktId));
	}
	
	public Product changeProductName(String produktId, String productName) {
		commands.sendAndWait(new ChangeProductNameCommand(produktId, productName));
		return new Product(produktId, productName);
	}
}