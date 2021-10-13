package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void calculateFareCarTest() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());

	}

	@Test
	public void calculateFareBikeTest() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	@Test
	public void calculateFareUnkownTypeTest() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithFutureInTimeTest() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	// Nouveau test de calcul de stationement pour une voiture avec +1h.
	@Test
	public void calculateFareCarWithFutureInTimeTest() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTimeTest() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																		// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	// Nouveau Test pour le stationnement d'une voiture sur 45min.
	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime1Test() throws ParseException {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		double price = (0.75 * Fare.CAR_RATE_PER_HOUR);
		BigDecimal bd = new BigDecimal(price);
		bd = bd.setScale(2, RoundingMode.HALF_DOWN);
		price = bd.doubleValue();
		assertEquals(price, ticket.getPrice());
	}

	// Nouveau Test pour le stationnement d'une voiture sur 35min.
	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime2Test() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (35 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		double price = (7.0 / 12.0 * Fare.CAR_RATE_PER_HOUR);
		BigDecimal bd = new BigDecimal(price);
		bd = bd.setScale(2, RoundingMode.HALF_DOWN);
		price = bd.doubleValue();
		assertEquals(price, ticket.getPrice());
	}

	// Nouveau Test pour le stationnement d'une voiture sur 50min.
	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime3Test() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (50 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);

		assertEquals((5.0 / 6.0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTimeTest() {
		Date inTime = new Date();
		// 24 hours parking time should give 24 * parking fare per hour
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());

	}

	// Nouveau Test pour le stationnement d'une moto sur 48h.
	@Test
	public void calculateFareBikeWithMoreThanADayParkingTimeTest() {
		Date inTime = new Date();
		// 48 hours parking time should give 24 * parking fare per hour
		inTime.setTime(System.currentTimeMillis() - (48 * 60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((48 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());

	}

	// Nouveau test pour vérifier le calcul de la durée entre sortie et entrée 
	// d'un véhicule.
	@Test
	public void calculateTicketParkTimeTest() {

		long duration = fareCalculatorService.calculateTicketParkTime(20, 10);
		assertEquals(10, duration);
	}

	// Calculer 30min de stationnement gratuit. Test OK !
	@Test
	public void calculate30MinFreeParkingForAcarTest() {

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis());
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis() + (15 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(0, (ticket.getPrice()));
	}


	// Nouveau test sur la levée d'exception sur le calcul de l'heure.
	@Test
	public void calculateConditionExceptionTest() {
		assertThrows(IllegalArgumentException.class,
				() -> fareCalculatorService.calculateTicketParkTime(10, 20));

	}

	// Nouveau test sur le switch case :
	@Test
	public void switchFareCalculatorServiceCasesTest() {

		long duration = fareCalculatorService.calculateTicketParkTime(120, 30);
		assertEquals(duration * Fare.CAR_RATE_PER_HOUR, 135);
		assertEquals(duration * Fare.BIKE_RATE_PER_HOUR, 90);

	}
}
