package com.parkit.parkingsystem.model;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class TicketTest {
	
	Ticket ticket = new Ticket();

	@Test
	public void testTicketGetRightId() {
		ticket.setId(0);
		assertEquals(0, ticket.getId());
	}
	
	@Test
	public void testTicketGetRightParkingSpot() {
		ticket.setParkingSpot(null);
		assertEquals(null, ticket.getParkingSpot());
	}
	
	@Test
	public void testTicketGetRightVehiculeNumber() {
		ticket.setVehicleRegNumber("1234");;
		assertEquals("1234", ticket.getVehicleRegNumber());
	}
	
	@Test
	public void testTicketGetRightPrice() {
		ticket.setPrice(2.5);
		assertEquals(2.5, ticket.getPrice());
	}
	
	@Test
	public void testTicketGetRightInTime() {
		ticket.setInTime(null);
		assertEquals(null, ticket.getInTime());
	}
	
	@Test
	public void testTicketGetRightOutTime() {
		ticket.getOutTime();
		assertEquals(null, ticket.getOutTime());
	}

}
