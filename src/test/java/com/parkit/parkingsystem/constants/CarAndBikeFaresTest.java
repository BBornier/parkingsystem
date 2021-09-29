package com.parkit.parkingsystem.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CarAndBikeFaresTest {

	@Test
	void testCarFareTest() {
		// Fare fare = new Fare(); Les variables sont static, on peut donc faire appel à
		// la classe Fare sans passer par un objet fare.
		assertEquals(1.5, Fare.CAR_RATE_PER_HOUR);

	}

	@Test
	void testBikeFareTest() {
		// Fare fare = new Fare();
		assertEquals(1.0, Fare.BIKE_RATE_PER_HOUR);
	}

}
