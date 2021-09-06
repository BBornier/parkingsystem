package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
    	// Problème de calcul ici.
    	// Méthodes dépréciées. remplacéespar getTime().
        //Appel de la méthode tout simplement avec son nom. 
        //On stocke les infos de la variable duration avec la méthode calculateTicketParkTime.
        long duration = calculateTicketParkTime(ticket.getOutTime().getTime(), ticket.getInTime().getTime());
        // @SuppressWarnings("deprecation")
		// long inHour = ticket.getInTime().getTime();
        // @SuppressWarnings("deprecation")
		// long outHour = ticket.getOutTime().getTime();

        // TODO: Some tests are failing here. Need to check if this logic is correct
        // long duration = outHour - inHour;

		switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
          
        }
    }
    // Type long avant la méthode :
    public long calculateTicketParkTime(long outHour, long inHour) {
    	
    	// réadaptation du if de départ.
		if( (outHour == 0) || (outHour < inHour) ) {
            throw new IllegalArgumentException("Out time provided is incorrect:"+outHour);
        }
    	long duration = outHour - inHour;
		return duration;	
		
    }
}
