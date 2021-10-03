package com.parkit.parkingsystem.service;

import java.text.DecimalFormat;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
    	// Problème de calcul ici.
    	// Méthodes dépréciées. remplacées par getTime().
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
            	// Essai de mettre le résultat en 2 decimales après la virgule.
            	ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR)/(60*60*1000)); 
                //double price = ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR)/(60*60*1000)); 
                //DecimalFormat df = new DecimalFormat("#.##");         
            	//df.format(price);
                break;
            }
            case BIKE: { // Dans le cas d'un vélo qui se gare
                ticket.setPrice((duration * Fare.BIKE_RATE_PER_HOUR)/(60*60*1000));               
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
          
        }
    }
    	// Simplification et décomposition de la méthode calculateFare.
    	// Ici on décompose en calculant à part la différence entre l'heure de sortie et l'heure d'arrivée.
    public long calculateTicketParkTime(long outHour, long inHour) {
    	
    	// réadaptation du if de départ.
		if( (outHour == 0) || (outHour < inHour) ) {
            throw new IllegalArgumentException("Out time provided is incorrect:"+outHour);
        }
    	long duration = outHour - inHour;
		return duration;	
		
    }
    	// Ajout d'une méthode de calcul de 2 chiffres seulement après la décimale :
    public void onlyTwoDecimalNumbers(long duration, Fare fare) {
    	Ticket ticket = new Ticket();
    	DecimalFormat df = new DecimalFormat("00.00");
    	df.format(ticket);
    	
    }
}
