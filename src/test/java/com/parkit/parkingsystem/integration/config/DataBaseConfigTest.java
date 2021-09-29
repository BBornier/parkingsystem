package com.parkit.parkingsystem.integration.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;

public class DataBaseConfigTest extends DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		logger.info("Create DB connection");
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(
				// Correction du test en indiquant le bon mdp et le bon fuseau horaire :
				"jdbc:mysql://localhost:3306/prod?serverTimezone=Europe/Paris", "root",
				"X8AHNdCXq&n6e&ugRkM+dTN2#H+!uJ!Fa9U85*G3We*7nn4DU*=+Dt4csgF!h?dX!XaMg-8CM5V5MV-pTEG7Q-#-Nns6D?N8bw^Frn+CxR-FjAbzL+Fty6=R%mT26gYXu7rN-$$8BGD&&b?^rm7Tje7HWDW8W=5CYyjY_ZWcDcgzVCymAj^KwgK^FHfb^w2BV96^h8kpsK!T=!eCt5U$Hxc=wTGrS@8yqjm@ngX&WE#QsYBj@26wdwsjLwAp&&@*");

	}

	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}
