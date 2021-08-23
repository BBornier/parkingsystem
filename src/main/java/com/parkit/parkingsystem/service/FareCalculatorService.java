package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.util.Calendar;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        //Appel de la méthode tout simplement avec son nom. 
        //On stocke les infos de la variable duration avec la méthode calculateTicketParkTime.
        long duration = calculateTicketParkTime(ticket.getOutTime().getTime(), ticket.getInTime().getTime());
        // Méthodes dépréciées. remplacer par Calendar.get ?
        // @SuppressWarnings("deprecation")
		// long inHour = ticket.getInTime().getTime();
        // @SuppressWarnings("deprecation")
		// long outHour = ticket.getOutTime().getTime();

        // TODO: Some tests are failing here. Need to check if this logic is correct
        // long duration = outHour - inHour;

		switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration  * Fare.CAR_RATE_PER_HOUR);
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
    	long duration = outHour - inHour;
		return duration;	
    }
}
