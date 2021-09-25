# livecoding eventdriven

live coding examples for different styles of event-driven architecture

##### references:
* https://martinfowler.com/articles/201701-event-driven.html

### Livecoding:

The following steps describe how this example can be used for (prepared) livecoding.
It can all be done live or only the most important parts, if time matters. 
To make it easy to compare event notification with event state transfers,
all examples are handle address changes.

### Contents:

* The `address` package contains a basic address value object and its repository. 
They are pretty basic and not relevant for event-driven architecture. 

* The `eventnotification` package and its tests show how event notification 
can be implemented without any infrastructure just using CDI events.

* The `eventstatetransfer` package and its tests show how event state transfer 
can be implemented again without any infrastructure just using CDI events.

### Guide:

* The event itself can be found in [AddressChangedEvent](src/main/java/org/joht/livecoding/eventdriven/eventnotification/AddressChangedEvent.java).

* [LiveAddressWithNotificationServiceTest](src/test/java/org/joht/livecoding/eventdriven/eventnotification/LiveAddressWithNotificationServiceTest.java) can be used as a starting point.
Use [AddressWithNotificationServiceTest](src/test/java/org/joht/livecoding/eventdriven/eventnotification/AddressWithNotificationServiceTest.java) as a reference.

* [LiveAddressWithNotificationService](src/main/java/org/joht/livecoding/eventdriven/eventnotification/LiveAddressWithNotificationService.java) implements the test case above and is needed to get the test running. Use [AddressWithNotificationService](src/main/java/org/joht/livecoding/eventdriven/eventnotification/AddressWithNotificationService.java) as a reference.

* Based on the previous example for event notification the test and its implementation can be
(copied and) changed to show event state transfer. These are the references:
    * [AddressStateChangedEvent](src/main/java/org/joht/livecoding/eventdriven/eventstatetransfer/AddressStateChangedEvent.java)
    * [AddressWithStateTransferServiceTest](src/test/java/org/joht/livecoding/eventdriven/eventstatetransfer/AddressWithStateTransferServiceTest.java)	
    * [AddressWithStateTransferService](src/main/java/org/joht/livecoding/eventdriven/eventstatetransfer/AddressWithStateTransferService.java)