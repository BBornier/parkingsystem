package com.parkit.parkingsystem.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class TicketTest {

	Ticket ticket = new Ticket();
	Date a = new Date(0);

	@Test
	public void ticketGetRightIdTest() {
		ticket.setId(0);
		assertEquals(0, ticket.getId());
	}

	@Test
	public void ticketGetRightParkingSpotTest() {
		ticket.setParkingSpot(null);
		assertEquals(null, ticket.getParkingSpot());
	}

	@Test
	public void ticketGetRightVehiculeNumberTest() {
		ticket.setVehicleRegNumber("1234");
		;
		assertEquals("1234", ticket.getVehicleRegNumber());
	}

	@Test
	public void ticketGetRightPriceTest() {
		ticket.setPrice(2.5);
		assertEquals(2.5, ticket.getPrice());
	}

	@Test
	public void ticketGetRightInTimeTest() {
		ticket.setInTime(a);
		assertEquals(a, ticket.getInTime());
	}

	@Test
	public void ticketGetRightOutTimeTest() {
		ticket.setOutTime(a);
		assertEquals(a, ticket.getOutTime());
	}

}
