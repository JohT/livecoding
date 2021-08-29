package org.joht.livecoding.eventsourcing.command.model;

import java.beans.ConstructorProperties;
import java.util.Objects;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateRoot;
import org.joht.livecoding.eventsourcing.Product;
import org.joht.livecoding.eventsourcing.message.command.ChangeProductNameCommand;
import org.joht.livecoding.eventsourcing.message.command.CreateProductCommand;
import org.joht.livecoding.eventsourcing.message.event.ProductCreatedEvent;
import org.joht.livecoding.eventsourcing.message.event.ProductNameChangedEvent;
import org.joht.livecoding.eventsourcing.message.event.ProductNamePresetEvent;

import jakarta.enterprise.context.Dependent;

@Dependent
@AggregateRoot
public class ProductAggregate {
	
	@AggregateIdentifier
	private String id;

	private String name;
	
	/**
	 * @deprecated Only for frameworks. Not meant to be called directly.
	 */
	@Deprecated
    ProductAggregate() {
		super();
	}

    @ConstructorProperties({ "productId" })
    public ProductAggregate(String productId) {
        this.id = productId;
    }

	@CommandHandler
    public static final ProductAggregate createWith(CreateProductCommand command) {
		ProductAggregate newAggregate = new ProductAggregate(command.getProductId());
		AggregateLifecycle.apply(new ProductCreatedEvent(command.getProductId()));
		AggregateLifecycle.apply(ProductNamePresetEvent.emptyNameFor(command.getProductId()));
		return newAggregate;
	}

	@CommandHandler
	private Product changeNickname(ChangeProductNameCommand command) {
		if (!Objects.equals(getName(), command.getName())) {
			AggregateLifecycle.apply(new ProductNameChangedEvent(id, command.getName(), getName()));
		}
		return getProduct();
	}

	@EventSourcingHandler
	private void onCreated(ProductCreatedEvent event) {
		this.id = event.getProductId();
	}

	@EventSourcingHandler
	private void onChangedName(ProductNameChangedEvent event) {
		this.name = event.getName();
	}

	public Product getProduct() {
		return new Product(getId(), getName());
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (!getClass().equals(other.getClass())) {
			return false;
		}
		ProductAggregate castOther = (ProductAggregate) other;
		return Objects.equals(id, castOther.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "ProductAggregate [id=" + id + ", name=" + name + "]";
	}
}