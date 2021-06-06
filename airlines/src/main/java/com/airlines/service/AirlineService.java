package com.airlines.service;

import java.util.List;
import java.util.Map;

import com.airlines.entity.Aircraft;
import com.airlines.entity.Airline;
import com.airlines.entity.Destination;
import com.airlines.exceptionHandler.AirlinesException;

public interface AirlineService {

	public List<Aircraft> getAllAircrafts() throws AirlinesException;
	
	public List<Aircraft> getAllAirlineAircrafts (long airlineId) throws AirlinesException;
	
	public void addAirline(Airline airline) throws AirlinesException;

	public List<Airline> getAllAirlines() throws AirlinesException;
	
	public void addAircraft(Aircraft aircraft, long airlineId) throws AirlinesException;
	
	public void sellAircraft(long aircraftId , long airlineId) throws AirlinesException;
	
	public void addDestination(Destination destination) throws AirlinesException;
	
	public List<Destination> getAllDestinations() throws AirlinesException;
	
	public Map<String, Double> getAirlineDistanceFromAllDestinations(long airlineId);
	
	public List<Destination> getAvaliableDestinationsForAirline(long airlineId);
	
	public void init() throws AirlinesException;
	
	
}
