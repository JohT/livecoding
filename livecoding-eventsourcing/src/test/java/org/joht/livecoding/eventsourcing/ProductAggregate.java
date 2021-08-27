package org.joht.livecoding.eventsourcing;

import java.beans.ConstructorProperties;
import java.util.Objects;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateRoot;
import org.joht.livecoding.eventsourcing.message.ChangeProductNameCommand;
import org.joht.livecoding.eventsourcing.message.CreateProductCommand;
import org.joht.livecoding.eventsourcing.message.ProductCreatedEvent;
import org.joht.livecoding.eventsourcing.message.ProductNameChangedEvent;
import org.joht.livecoding.eventsourcing.message.ProductNamePresetEvent;

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
		ProductAggregate newAggregate = new ProductAggregate();
		newAggregate.id = command.getProductId();
		AggregateLifecycle.apply(new ProductCreatedEvent(command.getProductId()));
		AggregateLifecycle.apply(ProductNamePresetEvent.emptyNameFor(command.getProductId()));
		return newAggregate;
	}

	@CommandHandler
	public void changeNickname(ChangeProductNameCommand command) {
		if (!Objects.equals(getName(), command.getName())) {
			AggregateLifecycle.apply(new ProductNameChangedEvent(id, command.getName(), getName()));
		}
	}

	@EventSourcingHandler
	private void onCreated(ProductCreatedEvent event) {
		this.id = event.getproductId();
	}

	@EventSourcingHandler
	private void onChangedName(ProductNameChangedEvent event) {
		this.name = event.getName();
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