package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		// Problème de calcul ici.
		// Méthodes dépréciées. remplacées par getTime().
		// Appel de la méthode tout simplement avec son nom.
		// On stocke les infos de la variable duration avec la méthode
		// calculateTicketParkTime.
		long duration = calculateTicketParkTime(ticket.getOutTime().getTime(), 
				ticket.getInTime().getTime());
		// @SuppressWarnings("deprecation")
		// long inHour = ticket.getInTime().getTime();
		// @SuppressWarnings("deprecation")
		// long outHour = ticket.getOutTime().getTime();
		// TODO: Some tests are failing here. Need to check if this logic is correct
		// long duration = outHour - inHour;
		 
		if (duration <= (30 * 60 * 1000)) {
			ticket.setPrice(0);
		} else {
			switch (ticket.getParkingSpot().getParkingType()) {

			case CAR: { 
				ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR) / (60 * 60 * 1000));
				break;
			}
			case BIKE: {
				ticket.setPrice((duration * Fare.BIKE_RATE_PER_HOUR) / (60 * 60 * 1000));
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}
	}

	public long calculateTicketParkTime(long outHour, long inHour) {

		// réadaptation du if de départ.
		if ((outHour == 0) || (outHour < inHour)) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + outHour);
		}
		long duration = outHour - inHour;
		return duration;

	}
	
	public void calculate5PercentDiscount(Ticket ticket) {
		
		long duration = calculateTicketParkTime(ticket.getOutTime().getTime(), ticket.getInTime().getTime());
		
		double nb1 = (duration * Fare.CAR_RATE_PER_HOUR) / (60 * 60 * 1000);
		double nb2 = (((duration * Fare.CAR_RATE_PER_HOUR) / (60 * 60 * 1000))*5/100);
		double discount = nb1 - nb2;
		ticket.setPrice(discount);
		
	}
	
}
