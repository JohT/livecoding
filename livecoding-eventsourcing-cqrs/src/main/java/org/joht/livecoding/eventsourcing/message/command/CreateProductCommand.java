package org.joht.livecoding.eventsourcing.message.command;

import java.beans.ConstructorProperties;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateProductCommand {

	@TargetAggregateIdentifier
	private final String productId;

	@ConstructorProperties({ "productId" })
	public CreateProductCommand(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}

	@Override
	public String toString() {
		return "CreateProductCommand [productId=" + productId + "]";
	}
}