package org.joht.livecoding.eventsourcing.message;

import java.beans.ConstructorProperties;

public class ProductNamePresetEvent extends ProductNameChangedEvent {

	public static final ProductNamePresetEvent emptyNameFor(String productId) {
		return new ProductNamePresetEvent(productId, "");
	}

	@ConstructorProperties({ "productId", "name" })
    public ProductNamePresetEvent(String productId, String name) {
        super(productId, name, "");
	}

	@Override
	public String toString() {
		return "ProductNamePresetEvent [toString()=" + super.toString() + "]";
	}
	
}