package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

/**
 * FareCalculatorService is the class where the application calculate the price
 * of ticket when a vehicle is exiting.
 *
 * @author BBornier
 * 
 * @version 0.1
 */
public class FareCalculatorService {
	/**
	 * Caculate ticket fare according to the staying duration of vehicules. It
	 * calculate 30minutes free and 5% discont for already known vehicles (with
	 * their numberplate).
	 * 
	 * <p>
	 * Some times methods were deprecated and have been replaced by getTime()
	 * method.
	 * <p>
	 * 
	 * @see getTime
	 * @see Fare
	 * @see TicketDAO
	 * @see Ticket
	 * @see calculateTicketParkTime
	 * @see calculate5PercentDiscount
	 * 
	 * @param ticket is an instance of Ticket.
	 * 
	 * @throws IllegalArgumentException if parking type is unknown.
	 * 
	 */
	public void calculateFare(Ticket ticket) {
		long duration = calculateTicketParkTime(ticket.getOutTime().getTime(), ticket.getInTime().getTime());

		TicketDAO ticketDAO = new TicketDAO();
		double ticketDiscount = calculate5PercentDiscount(ticket);

		if (duration <= (30 * 60 * 1000)) {
			ticket.setPrice(0);
		} else if (ticketDAO.getTicket(ticket.getVehicleRegNumber()) != null) {
			ticket.setPrice(ticketDiscount);
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

	/**
	 * Caculate duration of a vehicle staying time. It calculate out hour minus in
	 * time hour.
	 * 
	 * @see calculateFare
	 * 
	 * @param outHour, inHour. That is hour of vehicle exiting and vehicle incoming
	 *                 time.
	 * 
	 * @return duration result.
	 * 
	 * @throws IllegalArgumentException if inHour is superior to outHour.
	 * 
	 */
	public long calculateTicketParkTime(long outHour, long inHour) {

		if ((outHour == 0) || (outHour < inHour)) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + outHour);
		}
		long duration = outHour - inHour;
		return duration;

	}

	/**
	 * Caculate 5% discount for known custumers.
	 * 
	 * @see getTime
	 * @see Fare
	 * @see TicketDAO
	 * @see Ticket
	 * 
	 * @param ticket is an instance of Ticket.
	 * 
	 * @return duration result.
	 * 
	 * @throws IllegalArgumentException if inHour is superior to outHour.
	 * 
	 */
	public double calculate5PercentDiscount(Ticket ticket) {

		long duration = calculateTicketParkTime(ticket.getOutTime().getTime(), ticket.getInTime().getTime());

		double nb1 = (duration * Fare.CAR_RATE_PER_HOUR) / (60 * 60 * 1000);
		double nb2 = (((duration * Fare.CAR_RATE_PER_HOUR) / (60 * 60 * 1000)) * 5 / 100);
		double discount = nb1 - nb2;
		ticket.setPrice(discount);
		return discount;

	}

}
