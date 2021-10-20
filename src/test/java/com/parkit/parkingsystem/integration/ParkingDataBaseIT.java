package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.parkit.parkingsystem.integration.config.DataBaseConfigTest;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseConfigTest dataBaseTestConfig = new DataBaseConfigTest();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;
	private static FareCalculatorService fareCalculatorService; // mock Farecalc. NEW !

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testATicketIsRegistered_WhileParkingA_CarIT() {
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

	@Test
	public void testParkingTableIsUpdated_WhileParkingA_BikeIT() {
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
	}

	@Test
	public void testParkingPriceIsWellGeneratedAndRegisteredInBdd_WhileA_CarIsExitingIT() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		Ticket ticket = new Ticket();
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		ParkingSpot pkSpot = new ParkingSpot(2, ParkingType.CAR, false);

		ticket.setVehicleRegNumber("BA-788-QQ");
		ticket.setParkingSpot(pkSpot);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		fareCalculatorService.calculateFare(ticket);
		try {
			dataBaseTestConfig.getConnection();
			ticketDAO.saveTicket(ticket);
			assertNotNull(ticket.getPrice());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testOutTimeIsWellGeneratedAndRegisteredInBdd_WhileA_CarIsExitingIT() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		Ticket ticket = new Ticket();
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		ParkingSpot pkSpot = new ParkingSpot(2, ParkingType.CAR, false);

		ticket.setVehicleRegNumber("BA-788-QQ");
		ticket.setParkingSpot(pkSpot);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		try {
			dataBaseTestConfig.getConnection();
			ticketDAO.saveTicket(ticket);
			assertNotNull(ticket.getOutTime());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIfAcarIsKnown_And_if5PercentDiscountIsAppliedIT() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		Ticket ticket = new Ticket();
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis() + (120 * 60 * 1000));
		ParkingSpot pkSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setVehicleRegNumber("BA-788-QQ");
		ticket.setParkingSpot(pkSpot);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		fareCalculatorService.calculateFare(ticket);

		try {
			dataBaseTestConfig.getConnection();
			parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
			parkingSpotDAO.updateParking(pkSpot);
			ticketDAO.saveTicket(ticket);
			assertNotNull(ticket.getPrice());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		ParkingService parkingService2 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService2.processIncomingVehicle();
		Ticket ticket2 = new Ticket();
		Date inTime2 = new Date();
		inTime2.setTime(System.currentTimeMillis() + (120 * 60 * 1000));
		Date outTime2 = new Date();
		outTime2.setTime(System.currentTimeMillis() + (240 * 60 * 1000));
		ParkingSpot pkSpot2 = new ParkingSpot(2, ParkingType.CAR, false);

		ticket2.setVehicleRegNumber("BA-788-QQ");
		ticket2.setParkingSpot(pkSpot2);
		ticket2.setInTime(inTime2);
		ticket2.setOutTime(outTime2);
		fareCalculatorService.calculateFare(ticket2);

		try {
			dataBaseTestConfig.getConnection();
			parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
			parkingSpotDAO.updateParking(pkSpot2);
			ticketDAO.saveTicket(ticket2);
			assertEquals(2.85, ticket2.getPrice());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

}
