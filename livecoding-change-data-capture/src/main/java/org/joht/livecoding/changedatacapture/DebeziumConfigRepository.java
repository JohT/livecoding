package org.joht.livecoding.changedatacapture;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DebeziumConfigRepository {

	private static final Logger LOG = Logger.getLogger(DebeziumConfigRepository.class.getName());

	public Properties getProperties() {
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream propertyFile = classLoader.getResourceAsStream("debezium.properties")) {
			properties.load(propertyFile);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e, () -> "Error loading 'debezium.properties'");
			throw new IllegalStateException(e);
		}
		LOG.info("Debezium config: " + properties);
		return properties;
	}
	
}
