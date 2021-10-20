package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

/**
 * TicketDAO is the class where the application save a ticket in database, get
 * and update ticket informations.
 *
 * @author BBornier
 * 
 * @version 0.1
 */
public class TicketDAO {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * Save ticket informations as the parking spot number, the vehicle numberplate,
	 * the price, the hour when the vehicle is entering in the parking, and the hour
	 * when the vehicle is exiting from parking.
	 * <p>
	 * Corrections : variables correctly writen at the good lines. Ressources closed
	 * and written at their right place, in the right order. A multicatch as been
	 * written to see how to write one (exceptions must be written from specific to
	 * general).
	 * <p>
	 * A tag "SuppressWarnings" was written in order to supress insignificant
	 * warnings.
	 * 
	 * @See Ticket
	 * @see PreparedStatement
	 * @see ResultSet
	 * @see ParkingType
	 * @see Connection
	 * @see DBConstants
	 * @see DataBaseConfig
	 * 
	 * @param ticket is an instance of Ticket.
	 * 
	 * @return the execution of SQL statments.
	 * 
	 */
	@SuppressWarnings("finally")
	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
			ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
			return ps.execute();
		} catch (IllegalArgumentException e) {
			logger.error("Error fetching something", e);
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
			return false;
		}
	}

	/**
	 * Get ticket informations (saved in database) as the parking spot number, the
	 * vehicle numberplate, the price, the hour when the vehicle is entering in the
	 * parking, and the hour when the vehicle is exiting from parking.
	 * <p>
	 * Corrections : variables correctly writen at the good lines. Ressources closed
	 * and written at their right place, in the right order. A multicatch as been
	 * written to see how to write one (exceptions must be written from specific to
	 * general).
	 * <p>
	 * A tag "SuppressWarnings" was written in order to supress insignificant
	 * warnings.
	 * 
	 * @See Ticket
	 * @see PreparedStatement
	 * @see ResultSet
	 * @see ParkingType
	 * @see Connection
	 * @see DBConstants
	 * @see DataBaseConfig
	 * 
	 * @param vehicleRegNumber is the numberplate of a car or a bike.
	 * 
	 * @return the ticket with all its informations.
	 * 
	 */
	@SuppressWarnings("finally")
	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		Ticket ticket = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_TICKET);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4));
				ticket.setOutTime(rs.getTimestamp(5));
			}
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
			return ticket;
		}
	}

	/**
	 * Update ticket informations and concerns only the price, the hour of the
	 * exiting of the vehicle, and the Id of the ticket.
	 * <p>
	 * Corrections : variables correctly writen at the good lines. Ressources closed
	 * and written at their right place, in the right order. A multicatch as been
	 * written to see how to write one (exceptions must be written from specific to
	 * general).
	 * <p>
	 * A tag "SuppressWarnings" was written in order to supress insignificant
	 * warnings.
	 * 
	 * @See Ticket
	 * @see PreparedStatement
	 * @see ResultSet
	 * @see Connection
	 * @see DBConstants
	 * @see DataBaseConfig
	 * 
	 * @param ticket is an instance of Ticket.
	 * 
	 * @return the execution of SQL statments.
	 * 
	 */
	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
			ps.setInt(3, ticket.getId());
			ps.execute();
			return true;
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
		}
		return false;
	}
}
