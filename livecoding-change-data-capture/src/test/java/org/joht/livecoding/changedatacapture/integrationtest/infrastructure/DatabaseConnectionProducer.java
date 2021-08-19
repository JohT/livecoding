package org.joht.livecoding.changedatacapture.integrationtest.infrastructure;

import java.sql.Connection;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

public class DatabaseConnectionProducer {

	@Produces
	@ApplicationScoped
	@Integrationtest
	public static final Connection localPostgreSQL() {
		return DatabaseConnection.localPostgreSQL();
	}

}
