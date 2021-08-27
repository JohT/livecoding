package org.joht.livecoding.eventsourcing.message;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class ProductNameChangedEvent {

	private final String productId;
	private final String name;
    private final String oldName;

    @ConstructorProperties({ "productId", "name", "oldName" })
    public ProductNameChangedEvent(String productId, String name, String oldName) {
        this.productId = Objects.requireNonNull(productId, () -> "productId may not be null");
        this.name = Objects.requireNonNull(name, () -> "name may not be null");
        this.oldName = (oldName != null) ? oldName : "";
	}

	public String getProductId() {
		return productId;
	}

	public String getName() {
		return name;
	}

	public String getOldName() {
		return oldName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		ProductNameChangedEvent other = (ProductNameChangedEvent) obj;
		return Objects.equals(name, other.name) 
				&& Objects.equals(oldName, other.oldName)
				&& Objects.equals(productId, other.productId);
	}

	@Override
	public String toString() {
		return "ProductNameChangedEvent [productId=" + productId + ", name=" + name + ", oldName=" + oldName + "]";
	}
}