package org.joht.livecoding.changedatacapture.integrationtest.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	public static final boolean AUTO_COMMIT_ON = true;
	public static final boolean AUTO_COMMIT_OFF = false;
	
	private final String driverClassName;
	private final String databaseUrl;
	private final String databaseUser;
	private final String databasePassword;
	private final boolean autoCommit;

	public static final Connection localPostgreSQL() {
		return new DatabaseConnection("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/postgres", "postgres",
				"postgres", AUTO_COMMIT_OFF).createConnection();
	}

	public DatabaseConnection(String driverClassName, String databaseUrl, String databaseUser,
			String databasePassword, boolean autoCommit) {
		this.driverClassName = driverClassName;
		this.databaseUrl = databaseUrl;
		this.databaseUser = databaseUser;
		this.databasePassword = databasePassword;
		this.autoCommit = autoCommit;
	}

	protected DatabaseConnection() {
		this("", "", "", "", false);
	}
	
	public Connection createConnection() {
		registerJdbcDriver();
		try {
			return setAutoCommit(DriverManager.getConnection(databaseUrl, databaseUser, databasePassword));
		} catch (SQLException e) {
			throw new IllegalStateException("Error while creating the connection: " + this, e);
		}
	}

	private Connection setAutoCommit(Connection connection) {
		try {
			connection.setAutoCommit(this.autoCommit);
		} catch (SQLException e) {
			throw new IllegalStateException("AutoCommit couldn't be set to " + autoCommit, e);
		}
		return connection;
	}

	private void registerJdbcDriver() {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Error while registering JDBC driver " + driverClassName, e);
		}
	}

	@Override
	public String toString() {
		return "DatabaseConnection [driverClassName=" + driverClassName + ", databaseUrl=" + databaseUrl
				+ ", databaseUser=" + databaseUser + ", autoCommit=" + autoCommit + "]";
	}
}