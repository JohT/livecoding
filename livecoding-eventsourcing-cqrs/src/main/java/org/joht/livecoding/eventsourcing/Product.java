package org.joht.livecoding.eventsourcing;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class Product {

	private final String id;
	private final String name;

	public static final Product ofId(String id) {
		return new Product(id, "");
	}
	
	@ConstructorProperties({ "id", "name" })
	public Product(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public Product withName(String newName) {
		return new Product(getId(), newName);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Product other = (Product) obj;
		return Objects.equals(name, other.name) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + "]";
	}
}