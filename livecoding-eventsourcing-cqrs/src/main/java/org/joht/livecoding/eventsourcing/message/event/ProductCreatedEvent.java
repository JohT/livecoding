package org.joht.livecoding.eventsourcing.message.event;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class ProductCreatedEvent {

    private final String productId;

	@ConstructorProperties({ "productId" })
	public ProductCreatedEvent(String productId) {
		this.productId = Objects.requireNonNull(productId,  () -> "productId may not be null");
    }

	public String getProductId() {
        return productId;
    }

    @Override
	public boolean equals(Object obj) {
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		ProductCreatedEvent other = (ProductCreatedEvent) obj;
		return Objects.equals(productId, other.productId);
	}

    @Override
	public int hashCode() {
		return Objects.hash(productId);
	}

    @Override
	public String toString() {
		return "ProductCreatedEvent [productId=" + productId + "]";
	}
}