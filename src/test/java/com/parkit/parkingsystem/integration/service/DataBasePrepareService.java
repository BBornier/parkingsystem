package com.parkit.parkingsystem.integration.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.parkit.parkingsystem.integration.config.DataBaseConfigTest;

public class DataBasePrepareService {
	DataBaseConfigTest dataBaseTestConfig = new DataBaseConfigTest();

	public void clearDataBaseEntries() throws SQLException {
		Connection connection = null;

		try {
			connection = dataBaseTestConfig.getConnection();

			// set parking entries to available
			connection.prepareStatement("update parking set available = true").execute();

			// clear ticket entries;
			connection.prepareStatement("truncate table ticket").execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closeConnection(connection);

		}
	}

}
