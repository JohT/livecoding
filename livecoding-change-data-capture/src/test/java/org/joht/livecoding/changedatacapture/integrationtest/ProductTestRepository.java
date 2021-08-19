package org.joht.livecoding.changedatacapture.integrationtest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joht.livecoding.changedatacapture.integrationtest.infrastructure.DatabaseConnection;
import org.joht.livecoding.changedatacapture.integrationtest.infrastructure.Integrationtest;

import jakarta.inject.Inject;

public class ProductTestRepository {

	private final Connection connection;
	
	@Inject
	public ProductTestRepository(@Integrationtest Connection connection) {
		this.connection = connection;
	}
	
	public void insertProduct(String productName, String productDescription) {
		String sql = "INSERT INTO \"change_data_capture_example\".\"product\" (NAME, DESCRIPTION) values (?, ?)";
		executeSql(sql, productName, productDescription);
	}

	public void updateProduct(String productName, String productDescription) {
		String sql = "UPDATE \"change_data_capture_example\".\"product\" SET DESCRIPTION = ? WHERE NAME = ?";
		executeSql(sql, productDescription, productName);
	}

	public void deleteProduct(String productName) {
		String sql = "DELETE FROM \"change_data_capture_example\".\"product\" WHERE NAME = ?";
		executeSql(sql, productName);
	}

	public String getProductDescription(String productName) {
		String sql = "SELECT DESCRIPTION FROM \"change_data_capture_example\".\"product\" WHERE NAME = ?";
		return selectOneStringColumn(sql, productName);
	}

	private void executeSql(String sql, String... stringParameters) {
		try (Connection connection = DatabaseConnection.localPostgreSQL();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			for (int i = 0; i < stringParameters.length; i++) {
				statement.setString(i + 1, stringParameters[i]);
			}
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			throw new IllegalStateException("sql execution failed: " + e);
		}
	}
	
	private String selectOneStringColumn(String sql, String... stringParameters) {
		try (Connection connection = DatabaseConnection.localPostgreSQL();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			for (int i = 0; i < stringParameters.length; i++) {
				statement.setString(i + 1, stringParameters[i]);
			}
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet == null) {
					return "";
				}
				return (resultSet.next())? resultSet.getString(1) : "";
			}
		} catch (SQLException e) {
			throw new IllegalStateException("sql execution failed: " + e);
		}
	}
	
	@Override
	public String toString() {
		return "ProductTestRepository [connection=" + connection + "]";
	}
}