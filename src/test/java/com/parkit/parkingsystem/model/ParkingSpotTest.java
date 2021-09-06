package com.parkit.parkingsystem.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;

class ParkingSpotTest {
	
	ParkingSpot parking = new ParkingSpot(0, null, false);

	@Test
	public void testSetParkingId() {
		parking.setId(1);
		assertEquals(1, parking.getId());
	}
	
	@Test
	public void testSetParkingType() {
		Object CAR = null;
		parking.setParkingType(null);
		assertEquals(CAR , parking.getParkingType());
	}
	
	@Test
	public void testSetParkingAvailable() {
		parking.isAvailable();
		assertEquals(false, parking.isAvailable());
	}

}
