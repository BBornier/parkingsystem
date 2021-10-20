package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * ParkingSpotDAO is the class where the application give a new parking spot to
 * a new car or bike incoming. It updates the slot too.
 *
 * @author BBornier
 * 
 * @version 0.1
 */
public class ParkingSpotDAO {
	private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * Get the next available parking slot when a car/bike is incoming.
	 * <p>
	 * Connect to database, fetch a new parking slot, first if it's not taken,
	 * second if the first is still taken, third if the second is still taken, etc.
	 * <p>
	 * 
	 * @see PreparedStatement
	 * @see ResultSet
	 * @see ParkingType
	 * @see Connection
	 * @see DBConstants
	 * @see DataBaseConfig
	 * 
	 * @param parkingType is an instance of ParkingType.
	 * 
	 * @return Return the result of the new parking slot.
	 * 
	 */
	public int getNextAvailableSlot(ParkingType parkingType) {
		Connection con = null;
		int result = -1;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
			ps.setString(1, parkingType.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
				;
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return result;
	}

	/**
	 * Update parking slot when a car/bike is incoming or is exiting.
	 * <p>
	 * Connect to database, fetch a new parking slot.
	 * <p>
	 * 
	 * @see PreparedStatement
	 * @see ResultSet
	 * @see ParkingType
	 * @see Connection
	 * @see DBConstants
	 * @see DataBaseConfig
	 * 
	 * @param parkingSpot is an instance of ParkingSpot.
	 * 
	 * @return Return the result of the new parking slot.
	 * 
	 */
	public boolean updateParking(ParkingSpot parkingSpot) {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
			ps.setBoolean(1, parkingSpot.isAvailable());
			ps.setInt(2, parkingSpot.getId());
			int updateRowCount = ps.executeUpdate();
			dataBaseConfig.closePreparedStatement(ps);
			return (updateRowCount == 1);
		} catch (Exception ex) {
			logger.error("Error updating parking info", ex);
			return false;
		} finally {
			dataBaseConfig.closeConnection(con);
		}
	}

}
