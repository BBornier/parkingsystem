package com.parkit.parkingsystem;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.service.InteractiveShell;

public class App {
	//Appel de la signature de la méthode getLogger avec objet .class.
	private static final Logger logger = LogManager.getLogger("App");

	public static void main(String args[]) {

		logger.info("Initializing Parking System");
		InteractiveShell.loadInterface();

		logger.log(Level.INFO, "Logs with Log4J2");
		//logger.log(Level.ERROR, "Houston, we have a problem");

		logger.trace("Entering application");
		App appParking = new App();
		if (!appParking.doIt()) {
			logger.error("Didn't do it");
		}

		logger.trace("Existing application");

	}

	private boolean doIt() {
		return false;
	}
}
