package com.parkit.parkingsystem;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.service.InteractiveShell;

/**
 * <b>Park'it is an application created by Mov'it! to calculate parking tickets
 * for cars and bikes.</b>
 * <p>
 * 
 * @author BBornier
 * @version 0.1
 * 
 */
public class App {

	/**
	 * <p>
	 * The application is launched and propose to the custumer to enter with his car
	 * or bike, write the vehicule register number. The application save in a
	 * database the incoming hour, the vehicule register number. When the custumer
	 * leave, the application ask for the vehicule register number and then show the
	 * parking fare. Park'it register in the database the exiting hour and the price
	 * to pay.
	 * <p>
	 * Here, Log4J has been set up to be used from the beginning at application
	 * launch.
	 * 
	 * @see LogManager
	 * @see getLogger
	 * 
	 */
	private static final Logger logger = LogManager.getLogger("App");

	public static void main(String args[]) {

		logger.info("Initializing Parking System");
		InteractiveShell.loadInterface();

		logger.log(Level.INFO, "Logs with Log4J2");

		logger.trace("Entering application");
		App appParking = new App();
		if (!appParking.doIt()) {
			logger.error("Didn't do it");
		}

		logger.trace("Existing application");

	}

	/**
	 * Method that returns false by default.
	 * 
	 * @see doIt
	 * 
	 */
	private boolean doIt() {
		return false;
	}
}
