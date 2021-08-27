package org.joht.livecoding.eventsourcing;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.joht.livecoding.eventsourcing.message.ChangeProductNameCommand;
import org.joht.livecoding.eventsourcing.message.CreateProductCommand;
import org.joht.livecoding.eventsourcing.message.ProductCreatedEvent;
import org.joht.livecoding.eventsourcing.message.ProductNameChangedEvent;
import org.joht.livecoding.eventsourcing.message.ProductNamePresetEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductAggregateTest {

	private FixtureConfiguration<ProductAggregate> fixture = new AggregateTestFixture<>(ProductAggregate.class);

	@Test
	@DisplayName("should be createable by the CreateProductCommand resulting in a ProductCreatedEvent")
    public void productShouldBeCreateable() {
        String productId = "test-product-id-321";
		fixture.givenNoPriorActivity()
               .when(new CreateProductCommand(productId))
               .expectSuccessfulHandlerExecution()
               .expectEvents(new ProductCreatedEvent(productId), new ProductNamePresetEvent(productId, ""));
	}

	@Test
	@DisplayName("should change the name of the product by the ChangeProductNameCommand resulting in a ProductNameChangedEvent")
    public void productNameShouldBeChangeable() {
        String productId = "test-product-id-123";
		fixture.given(new ProductCreatedEvent(productId))
               .when(new ChangeProductNameCommand(productId, "bluetooth speaker"))
               .expectSuccessfulHandlerExecution()
               .expectEvents(new ProductNameChangedEvent(productId, "bluetooth speaker", ""));
	}
}
