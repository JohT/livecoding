package org.joht.livecoding.changedatacapture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.spi.JsonProvider;

class ChangeDeserializerTest {

	private final Jsonb jsonb = JsonbBuilder.create();
	private final JsonProvider jsonProvider = JsonProvider.provider();

	private ChangeDeserializerService deserializerUnderTest = new ChangeDeserializerService(jsonProvider, jsonb);

	@Test
	@DisplayName("should contain deserialized payload state before the change")
	void shouldContainDeserializedPayloadStateBeforeTheChange() {
		String expectedValue = "value before";
		String json = createJson(expectedValue, "", "");
		Change<String> desierialized = deserializerUnderTest.deserializeChangeEvent(json, String.class);
		assertEquals(expectedValue, desierialized.getBefore());
	}

	@Test
	@DisplayName("should contain deserialized payload state after the change")
	void shouldContainDeserializedPayloadStateAfterTheChange() {
		String expectedValue = "value after";
		String json = createJson("", expectedValue, "");
		Change<String> desierialized = deserializerUnderTest.deserializeChangeEvent(json, String.class);
		assertEquals(expectedValue, desierialized.getAfter());
	}

	@Test
	@DisplayName("should contain deserialized payload change operation")
	void shouldContainDeserializedPayloadChangeOperation() {
		String expectedValue = "u";
		String json = createJson("A", "B", "u");
		Change<String> desierialized = deserializerUnderTest.deserializeChangeEvent(json, String.class);
		assertEquals(expectedValue, desierialized.getOperation().getCode());
	}

	@Test
	@DisplayName("should fail if json is null'")
	void shouldFailIfJsonIsNull() {
		assertThrows(RuntimeException.class, () -> deserializerUnderTest.deserializeChangeEvent(null, String.class));
	}

	@Test
	@DisplayName("should fail if json is invalid'")
	void shouldFailIfJsonIsInvalid() {
		assertThrows(RuntimeException.class, () -> deserializerUnderTest.deserializeChangeEvent("{,}", String.class));
	}

	@Test
	@DisplayName("should fail if 'payload' is missing in json")
	void shouldFailIfPayloadIsMissingJsonIsInvalid() {
		assertThrows(RuntimeException.class, () -> deserializerUnderTest.deserializeChangeEvent("{}", String.class));
	}

	private static String createJson(String valueBefore, String valueAfter, String operationCode) {
		JsonObjectBuilder payload = Json.createObjectBuilder();
		payload.add("before", valueBefore);
		payload.add("after", valueAfter);
		payload.add("op", operationCode);
		return Json.createObjectBuilder().add("payload", payload).build().toString();
	}

}