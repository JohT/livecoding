package org.joht.livecoding.eventsourcing.message;

import java.beans.ConstructorProperties;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * This command is only for demonstration purposes.
 * <p>
 * It shouldn't be done this way, when using eventsourcing in conjunction with CQRS. 
 * The latter states, that reading should be done on the query side, not the command side,
 * where the aggregate is located.
 */
public class ReadProductCommand {

	@TargetAggregateIdentifier
	private final String productId;

	@ConstructorProperties({ "productId" })
	public ReadProductCommand(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}

	@Override
	public String toString() {
		return "ReadProductCommand [productId=" + productId + "]";
	}
}