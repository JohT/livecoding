# livecoding change data capture (CDC)

live coding example for the change data capture and "outbox" pattern.

### Livecoding Guide:

The following steps describe how to to use this example for (prepared) livecoding.

* Open [example-database-setup.ddl](example-database-setup.ddl).
* Establish a connection to a local PostgreSql database.
* If not setup yet, run the setup steps in [example-database-setup.ddl](example-database-setup.ddl) or simply the whole script.
* Open the already prepared integration test [ChangeDataCaptureExampleServiceIT](src/test/java/org/joht/livecoding/changedatacapture/integrationtest/ChangeDataCaptureExampleServiceIT.java).
* Comment or remove `@Disabled` on top of the Method `exploratoryManualDatabaseChangeExample` to get a first manual test.
* Create a new file `ChangeDataCaptureExampleService.java` inside the folder `src/main/java/org/joht/livecoding/changedatacapture` and annotate it `@ApplicationScoped`.
* Implement the method `startDebeziumEngine` (for details see reference implementation), that gets called when the application starts (parameter annotated with `@Observes @Initialized(ApplicationScoped.class`).
* Don't forget to start the debezium engine using an injected `ManagedExecutorService`.
* Use [ChangeDataCaptureService](src/main/java/org/joht/livecoding/changedatacapture/ChangeDataCaptureService.java) as a reference for the service implementation.
* Use [ChangeDataCaptureServiceIT](src/test/java/org/joht/livecoding/changedatacapture/integrationtest/ChangeDataCaptureExampleIT.java) as a reference for the integration test.

### References:
* [A Gentle Introduction to Event-driven Change Data Capture](https://medium.com/event-driven-utopia/a-gentle-introduction-to-event-driven-change-data-capture-683297625f9b)
* [Debezium Examples: DatabaseChangeEventListener](https://github.com/debezium/debezium-examples/blob/master/cache-invalidation/src/main/java/io/debezium/examples/cacheinvalidation/persistence/DatabaseChangeEventListener.java)
* [Debezium Engine](https://debezium.io/documentation/reference/1.6/development/engine.html#engine-properties)
* [Debezium Connector for PostgreSQL](https://debezium.io/documentation/reference/1.6/connectors/postgresql.html#postgresql-property-column-include-list)
* [Lessons Learned from Running Debezium with PostgreSQL on Amazon RDS](https://debezium.io/blog/2020/02/25/lessons-learned-running-debezium-with-postgresql-on-rds)
* [Weld JUnit 5 Auto Extensions](https://github.com/weld/weld-junit/blob/master/junit5/README.md#weldjunit5autoextension)