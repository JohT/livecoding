# livecoding Event Sourcing and CQRS 

live coding example for Event Sourcing with/without CQRS (command query responsibility segregation)

### Livecoding Guide:

The following steps describe how to to use this example for (prepared) livecoding.

### 1.1. Command Side - The Service
* Create an `@ApplicationScoped LiveProductService` in the same package as the reference implementation [ProductService.java](src/main/java/org/joht/livecoding/eventsourcing/command/model/ProductService.java).
* Create methods to create (and maybe later) update the product.
* Inside the create method, a UUID needs to be generated as product id.
* Create a instance of [CreateProductCommand](src/main/java/org/joht/livecoding/eventsourcing/message/command/CreateProductCommand.java) inside the "create" Method.
* Create a instance of [ChangeProductNameCommand](src/main/java/org/joht/livecoding/eventsourcing/message/command/ChangeProductNameCommand.java) inside the "update" Method.
* `@Inject` the `CommandGateway` to `sendAndWait` the commands.

### 1.2. Command Side - The Aggregate
* Create an `@Dependent @AggregateRoot LiveProductAggregate` in the same package as the reference implementation [ProductAggregate.java](src/main/java/org/joht/livecoding/eventsourcing/command/model/ProductAggregate.java).
* Implement the two properties `id` and `name` 
* Implement a static factory method annotated with `@CommandHandler` and parameter `CreateProductCommand` returning a newly created instance of the aggregate. The properties need to be set using `AggregateLifecycle.apply` by sending the regarding events, e.g. [ProductCreatedEvent](src/main/java/org/joht/livecoding/eventsourcing/message/event/ProductCreatedEvent.java).
* Implement the `@EventSourcingHandler` method for `ProductCreatedEvent` and `ProductNameChangedEvent` 
as setter for the regarding properties.
* Proceed with the `@CommandHandler` for [ChangeProductNameCommand](src/main/java/org/joht/livecoding/eventsourcing/message/command/ChangeProductNameCommand.java) or proceed and implement the update step later.
* Finish the aggregate optionally with Getters, hashCode, equals and toString method implementations.

### 1.3. Command Side - The Integration-Test
* Create `ProductLiveServiceIT` annotated with `@EnableAutoWeld @AddPackages(ProductLiveService.class) @AddBeanClasses(AxonConfiguration.class)` in the same package as the reference implementation [ProductServiceIT.java](src/test/java/org/joht/livecoding/eventsourcing/command/model/ProductServiceIT.java).
* `@Inject` the `ProductLiveService` as the `serviceUnderTest`.

### 2.1. Query Side - The Query Service
* Create an `@ApplicationScoped ProductLiveQueryService` in the same package as the reference implementation [ProductQueryService](src/main/java/org/joht/livecoding/eventsourcing/query/model/ProductQueryService.java).
* Write a `getProduct` Method with parameter `productId` returning an `Optional<Product>`.
* Create an instance of the query message type [ProductQuery](src/main/java/org/joht/livecoding/eventsourcing/message/query/ProductQuery.java) with the given id.
* `@Inject` the `QueryGateway` to send the query message and get its results back.
* Use `ResponseTypes.optionalInstanceOf(Product.class)` as response type.

### 2.2. Query Side - The Query Handler(s)
* Show (or retype) [ProductQueryHandler](src/main/java/org/joht/livecoding/eventsourcing/query/model/ProductQueryHandler.java).

### 2.3. Query Side - The Event Handlers
* Show (or retype) [ProductEventHandler](src/main/java/org/joht/livecoding/eventsourcing/query/model/ProductEventHandler.java).

### 2.4. Query Side - The Repository
* Show (or retype) [ProductRepository](src/main/java/org/joht/livecoding/eventsourcing/query/model/ProductRepository.java).

### 2.5. Query Side - The Integration-Test
* Create `ProductLiveQueryServiceIT` annotated with `@EnableAutoWeld @AddPackages(ProductService.class) @AddPackages(ProductQueryService.class) @AddBeanClasses(AxonConfiguration.class)` in the same package as the reference implementation [ProductQueryServiceIT.java](src/test/java/org/joht/livecoding/eventsourcing/query/model/ProductQueryServiceIT.java).
* `@Inject` the `ProductQueryService` as the `serviceUnderTest`.
* `@Inject` the `ProductService` as the `productService`.

### References:
* [Event Sourcing (Martin Fowler)](https://martinfowler.com/eaaDev/EventSourcing.html)
* [Exploring CQRS and Event Sourcing (Microsoft)](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwiapv7PudjyAhXOAewKHRaTBWMQFnoECAQQAQ&url=http%3A%2F%2Fdownload.microsoft.com%2Fdownload%2Fe%2Fa%2F8%2Fea8c6e1f-01d8-43ba-992b-35cfcaa4fae3%2Fcqrs_journey_guide.pdf&usg=AOvVaw3pH9fj0F7rjnJE6yATGIXN)
* [AxonFramework reference guide](https://docs.axoniq.io/reference-guide/)
* [Weld JUnit 5 Auto Extensions](https://github.com/weld/weld-junit/blob/master/junit5/README.md#weldjunit5autoextension)