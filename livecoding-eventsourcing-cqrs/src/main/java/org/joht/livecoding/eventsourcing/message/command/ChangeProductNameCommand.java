package org.joht.livecoding.eventsourcing.message.command;

import java.beans.ConstructorProperties;
import java.util.Objects;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class ChangeProductNameCommand {

	@TargetAggregateIdentifier
	private final String productId;
	
	private final String name;

	@ConstructorProperties({ "productId", "name" })
	public ChangeProductNameCommand(String productId, String name) {
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
		ChangeProductNameCommand other = (ChangeProductNameCommand) obj;
		return Objects.equals(name, other.name) && Objects.equals(productId, other.productId);
	}

	@Override
	public String toString() {
		return "ChangeProductNameCommand [productId=" + productId + ", name=" + name + "]";
	}
}