package org.joht.livecoding.changedatacapture;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.spi.JsonProvider;

/**
 * Deserializes the data change event JSON (emitted by debezium) to a {@link Change} Object
 * of the given type.
 *
 * @param <T> type of the changed object.
 */
@ApplicationScoped
public class ChangeDeserializerService {

	private static final Logger LOG = Logger.getLogger(ChangeDeserializerService.class.getName());

	private final JsonProvider jsonProvider;
	private final Jsonb jsonb;
	
	@Inject
	public ChangeDeserializerService(Instance<JsonProvider> jsonProvider, Jsonb jsonb) {
		this(jsonProvider.isResolvable()? jsonProvider.get() : JsonProvider.provider(), jsonb);
	}

	public ChangeDeserializerService(JsonProvider jsonProvider, Jsonb jsonb) {
		this.jsonProvider = jsonProvider;
		this.jsonb = jsonb;
	}

	public <T> Change<T> deserializeChangeEvent(String changeEventValueJson, Class<T> type) {
		try (Reader recordReader = new StringReader(changeEventValueJson); 
				JsonReader jsonReader =	jsonProvider.createReader(recordReader)) {
			JsonObject payload = jsonReader.readObject().getJsonObject("payload");
			if (payload == null) {
				throw new IllegalArgumentException("missing 'payload' in " + changeEventValueJson);
			}
			T before = deserializeJsonValue(payload.get("before"), type);
			T after = deserializeJsonValue(payload.get("after"), type);
			return new Change<T>(before, after, payload.getString("op", ""));
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e, () -> "Couldn't deserialize data change event from " + changeEventValueJson);
			throw new IllegalStateException(e);
		}
	}

	private <T> T deserializeJsonValue(JsonValue json, Class<T> type) {
		String jsonString = Objects.toString(json, null);
		return (jsonString != null)? jsonb.fromJson(jsonString, type) : null;
	}

	@Override
	public String toString() {
		return "ChangeDeserializerService [jsonProvider=" + jsonProvider + ", jsonb=" + jsonb + "]";
	}	
}