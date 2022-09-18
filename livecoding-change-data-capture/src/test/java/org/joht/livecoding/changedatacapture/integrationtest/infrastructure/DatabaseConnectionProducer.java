package org.joht.livecoding.changedatacapture.integrationtest.infrastructure;

import java.sql.Connection;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class DatabaseConnectionProducer {

	@Produces
	@ApplicationScoped
	@Integrationtest
	public Connection localPostgreSQL() {
		return DatabaseConnection.localPostgreSQL();
	}

}
