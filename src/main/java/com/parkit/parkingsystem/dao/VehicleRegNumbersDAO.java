package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ArrayList;
import com.parkit.parkingsystem.service.VehicleRegNumbers;

public class VehicleRegNumbersDAO {

	private static final Logger logger = LogManager.getLogger("VehicleRegNumberDAO");
	
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();
	

	public List<VehicleRegNumbersDAO> getVehicleRegNumbers(){
		
		List<VehicleRegNumbersDAO> vehicleRegNumbers = new ArrayList<VehicleRegNumbersDAO>();
		
	}
	
}
