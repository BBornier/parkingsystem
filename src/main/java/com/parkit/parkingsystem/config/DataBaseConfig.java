package com.parkit.parkingsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.service.FareCalculatorService;

/**
 * DataBaseConfig is the class where database is set up.
 *
 * @author BBornier
 * 
 * @version 0.1
 */

public class DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseConfig");

	/**
	 * Get the connection to a database with the good jdbc driver.
	 * <p>
	 * Works with Mysql Workbench for instance.
	 * <p>
	 * 
	 * @see FareCalculatorService
	 * @see ParkingSpotDAO
	 * @see TicketDAO
	 * 
	 * @return Return the localhost port, the good Time Zone server (Europe/Paris).
	 *         Thanks to it, hours, time are well calculated in the
	 *         FareCalculatorService class. the admin login "root" and its password
	 *         (generated thanks to a random keygen - 256 characters).
	 * 
	 * @throws ClassNotFoundException, if the name of the driver is incorrect.
	 * @throws SQLException            if there is an access error connecting to the
	 *                                 database for exemple.
	 * 
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		logger.info("Create DB connection");
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/prod?serverTimezone=Europe/Paris", "root", "X8AHNdCXq&n6e&ugRkM+dTN2#H+!uJ!Fa9U85*G3We*7nn4DU*=+Dt4csgF!h?dX!XaMg-8CM5V5MV-pTEG7Q-#-Nns6D?N8bw^Frn+CxR-FjAbzL+Fty6=R%mT26gYXu7rN-$$8BGD&&b?^rm7Tje7HWDW8W=5CYyjY_ZWcDcgzVCymAj^KwgK^FHfb^w2BV96^h8kpsK!T=!eCt5U$Hxc=wTGrS@8yqjm@ngX&WE#QsYBj@26wdwsjLwAp&&@*");

	}

	/**
	 * Method in order to close a ressource as the connection.
	 * 
	 * @param con is an instance of Connection. Close the running connection.
	 * @see Connection
	 * 
	 */
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

	/**
	 * Method in order to a prepared sql statement.
	 * 
	 * @param ps is an instance of PreparedStatement.
	 * @see DataBaseConfig
	 * @see PreparedStatement
	 * 
	 */
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

	/**
	 * Method in order to a close an instance of Resultset.
	 * 
	 * @param rs is an instance of ResultSet.
	 * @see DataBaseConfig
	 * @see ResultSet
	 * 
	 */
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
