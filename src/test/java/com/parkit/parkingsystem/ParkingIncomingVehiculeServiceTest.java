package com.parkit.parkingsystem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingIncomingVehiculeServiceTest {

	private static ParkingService parkingService;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	private void setUpTroisMockEtBisouTest() {
		try {
			when(inputReaderUtil.readSelection()).thenReturn(1);
			when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}

	}

	//Nouveau BeforeEach et Tests pour l'entrée du véhicule dans un parking.
	//Test de place de parking mise à jour.
	@Test
	void testAParkingSpotIsUpdated_WhileAVehicleIsIncomingTest() {
		//Appel de la méthode à tester :
		parkingService.processIncomingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	}

	//Test sur la disponibilité de la prochaine place de parking, pour les voitures.
	//Invocation du mock ou moins 1 fois.
	@Test
	void testIfASlotWillBeAvailable_WhileACarIsIncomingTest() {
		parkingService.processIncomingVehicle();
		verify(parkingSpotDAO, Mockito.atLeast(1)).getNextAvailableSlot(ParkingType.CAR);
	}

	//Test sur la disponibilité de la prochaine place de parking, pour les motos.
	//Invocation du Mock, maximum 2x.
	@Test
	void testIfASlotWillBeAvailable_WhileABikeIsIncoming() {
		parkingService.processIncomingVehicle();
		verify(parkingSpotDAO, Mockito.atMost(2)).getNextAvailableSlot(ParkingType.BIKE);
	}

	//Test de ticket sauvegardé en BDD.
	@Test
	void testIfATicketIsWellSaved_WhileAVehicleIsIncomingTest() {
		parkingService.processIncomingVehicle();
		verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
	}
}