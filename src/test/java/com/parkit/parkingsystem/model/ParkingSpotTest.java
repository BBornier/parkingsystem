package com.parkit.parkingsystem.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;

class ParkingSpotTest {

	ParkingSpot parking = new ParkingSpot(0, null, false);

	@Test
	public void setParkingIdTest() {
		parking.setId(1);
		assertEquals(1, parking.getId());
	}

	@Test
	public void setParkingTypeTest() {
		parking.setParkingType(ParkingType.CAR);
		assertEquals(ParkingType.CAR, parking.getParkingType());
	}

	@Test
	public void setParkingAvailableTest() {
		parking.isAvailable();
		assertEquals(false, parking.isAvailable());
	}

}
