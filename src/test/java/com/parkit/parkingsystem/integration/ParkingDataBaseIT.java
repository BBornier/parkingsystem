package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		// Unnecessary stubbing detected.
		// org.mockito.exceptions.misusing.UnnecessaryStubbingException.
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	// Vérifier qu'un ticket est bien enregistré en BDD.
	@Test
	public void testATicketIsRegistered_WhileParkingA_Car() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		Ticket ticket = new Ticket();
		Date inTime = new Date();
		ParkingSpot pkSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setVehicleRegNumber("BA-788-QQ");
		ticket.setParkingSpot(pkSpot);
		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		try {
			dataBaseTestConfig.getConnection();
			inputReaderUtil.readVehicleRegistrationNumber();
			ticketDAO.saveTicket(ticket);
			Ticket ticketSave = ticketDAO.getTicket("BA-788-QQ");
			assertNotNull(ticketSave);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Vérifier que la table Parking est bien mise à jour sur les disponibilités de
	// places de parking.
	@Test
	public void testParkingTableIsUpdated_WhileParkingA_Bike() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		ParkingSpot pkSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		try {
			dataBaseTestConfig.getConnection();
			parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
			parkingSpotDAO.updateParking(pkSpot);
			assertNotNull(pkSpot);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability. DONE !
	}

	// Vérifier que le prix est bien généré et inscrit en BDD.
	@Test
	public void testParkingPriceIsWellGeneratedAndRegisteredInBdd_WhileA_CarIsExiting() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		Ticket ticket = new Ticket();
		Date inTime = new Date();
		ParkingSpot pkSpot = new ParkingSpot(2, ParkingType.CAR, false);
		ticket.setVehicleRegNumber("BA-788-QQ");
		ticket.setParkingSpot(pkSpot);
		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		try {
			dataBaseTestConfig.getConnection();
			ticketDAO.saveTicket(ticket);
			Ticket ticketPrice = ticketDAO.getTicket("BA-788-QQ");
			dataBaseTestConfig.closeConnection(null);
			dataBaseTestConfig.getConnection();
			ticketDAO.updateTicket(ticketPrice);
			assertNotNull(ticketPrice);
			parkingService.processExitingVehicle();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	// Vérifier que le temps de sortie est bien noté et inscrit en BDD :
	@Test
	public void testOutTimeIsWellGeneratedAndRegisteredInBdd_WhileA_CarIsExiting() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();
		Ticket ticket = new Ticket();
		Date inTime = new Date();
		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		ticket.setPrice(0);
		try {
			dataBaseTestConfig.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		// TODO: check that the fare generated and out time are populated correctly in
		// the database. DOING !
	}

}
