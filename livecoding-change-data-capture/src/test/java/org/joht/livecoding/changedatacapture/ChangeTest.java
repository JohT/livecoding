package org.joht.livecoding.changedatacapture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChangeTest {

	private Change<String> changeUnderTest;
	
	@Test
	@DisplayName("should contain the state before the change")
	void shouldContainTheStateBeforeTheChange() {
		String expectedValue = "before the change";
		changeUnderTest = new Change<String>(expectedValue, null, "");
		assertEquals(expectedValue, changeUnderTest.getBefore());
	}
	
	@Test
	@DisplayName("should contain the state after the change")
	void shouldContainTheStateAfterTheChange() {
		String expectedValue = "afterwards";
		changeUnderTest = new Change<String>(null, expectedValue, "");
		assertEquals(expectedValue, changeUnderTest.getAfter());
	}
	
	@Test
	@DisplayName("should contain the change operation")
	void shouldContainTheChageOperation() {
		Change.Operation expectedOperation = Change.Operation.CREATE;
		changeUnderTest = new Change<String>(null, null, expectedOperation.getCode());
		assertEquals(expectedOperation, changeUnderTest.getOperation());
	}
	
	@Test
	@DisplayName("every change operation should be createable by its change code")
	void everyChangeOperationShouldBeCreateableByItsChangeCode() {
		for (Change.Operation expectedOperation : Change.Operation.values()) {
			assertEquals(expectedOperation, Change.Operation.valueOfCode(expectedOperation.getCode()));
		}
	}
}