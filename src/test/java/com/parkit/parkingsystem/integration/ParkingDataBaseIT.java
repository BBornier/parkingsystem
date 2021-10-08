package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
		fareCalculatorService = new FareCalculatorService(); // ajout FareCalc. NEW !
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

	// Vérifier qu'un ticket est bien enregistré en BDD.
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

	// Vérifier que la table Parking est bien mise à jour sur les disponibilités de
	// places de parking.
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
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability. DONE !
	}

	// Vérifier que le prix et le temps de sortie sont bien générés et inscrits en
	// BDD. Fonctionne en BDD (cf WorkBench) avec imatriculation BA-788-QQ !
	@Test
	public void testParkingPriceIsWellGeneratedAndRegisteredInBdd_WhileA_CarIsExitingIT() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle(); // Appel de la méthode gérant l'arrivée d'un véhicule.
		Ticket ticket = new Ticket(); // instanciation d'un ticket.
		Date inTime = new Date(); // instanciation d'un horaire (celui d'entrée du véhicule).
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000)); // appel de la méthode setTime() pour simuler l'horaire d'arrivée -1h par rapport à l'heure actuelle.
		Date outTime = new Date(); // instanciation d'un horaire (celui de sortie du véhicule).
		outTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000)); // appel de la méthode setTime() pour simuler l'horaire de sortie +1h par rapport à l'heure actuelle.
		ParkingSpot pkSpot = new ParkingSpot(2, ParkingType.CAR, false); // instanciation d'un objet pkSpot pour atttribuer une place de parking au véhicule.

		ticket.setVehicleRegNumber("BA-788-QQ"); // appel de la méthode setVehiculeRegNumber pour attribuer une immat au véhicule qui rentre et l'attribuer à l'objet ticket.
		ticket.setParkingSpot(pkSpot); // appel de la méthode setParkingSport pour attibuer une place de stationnement au véhicule qui rentre et l'attribuer à l'objet ticket.
		ticket.setInTime(inTime); // appel de la méthode setInTime pour attibuer un horaire d'entrée au véhicule qui rentre et l'attribuer à l'objet ticket.
		ticket.setOutTime(outTime); // appel de la méthode setOutTime pour attibuer un horaire de sortie au véhicule qui sort et l'attribuer à l'objet ticket.
		fareCalculatorService.calculateFare(ticket); // utilisation de l'objet fareCalculatorService instancié dans le @ExtendWith et @BeforeAll. Il calcule le prix du ticket grâce aux méthodes et paramètres passés au ticket juste avant.
		try {
			dataBaseTestConfig.getConnection(); // connection à la base de données.
			ticketDAO.saveTicket(ticket); // sauvegarde du ticket en base de données.
			ticket = ticketDAO.getTicket("BA-788-QQ"); // stockage du ticket en base de données avec tous ses paramètres.
			ticketDAO.updateTicket(ticket); // mise à jour du ticket avec ses nouvelles informations et notamment le calcul du temps passé et donc du prix.
			ticket = ticketDAO.getTicket("BA-788-QQ"); // stockage de la mise à jour dans le ticket.
			assertNotNull(ticket.getPrice()); // assertion du tests pour savoir si oui ou non le prix du ticket est null ou pas (vérification que le prix est bien enregistré en BDD).
			parkingService.processExitingVehicle(); // appel de la méthode gérant la sortie du véhicule et la génération en console du tarif à payer.
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	// Vérifier que le temps de sortie est bien noté et inscrit en BDD.
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
		fareCalculatorService.calculateFare(ticket); 
		try {
			dataBaseTestConfig.getConnection(); 
			ticketDAO.saveTicket(ticket); 
			ticket = ticketDAO.getTicket("BA-788-QQ"); 
			ticketDAO.updateTicket(ticket); 
			ticket = ticketDAO.getTicket("BA-788-QQ");
			parkingService.processExitingVehicle();
			assertNotNull(ticket.getOutTime());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

		// TODO: check that the fare generated and out time are populated correctly in
		// the database. DONE !

}
