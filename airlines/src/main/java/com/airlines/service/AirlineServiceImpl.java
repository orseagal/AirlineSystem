package com.airlines.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airlines.entity.Aircraft;
import com.airlines.entity.Airline;
import com.airlines.entity.Destination;
import com.airlines.entity.Location;
import com.airlines.exceptionHandler.AirlinesException;
import com.airlines.exceptionHandler.ErrorType;
import com.airlines.repository.AircraftRepository;
import com.airlines.repository.AirlineRepository;
import com.airlines.repository.DestinationRepository;
import com.airlines.utils.Haversine;

@Service
public class AirlineServiceImpl implements AirlineService {

	@Autowired
	AirlineRepository airlineRepository;
	@Autowired
	AircraftRepository aircraftRepository;
	@Autowired
	DestinationRepository destinationRepository;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	
	/**
	 * Fetching all the aircrafts from DB
	 * 
	 */
	@Override
	public List<Aircraft> getAllAircrafts() throws AirlinesException {
		return aircraftRepository.getAllAircrafts();
	}

	/**
	 * Fetching all the Aircrafts from specific airline 
	 */
	@Override
	public List<Aircraft> getAirlineAircrafts(long airlineId) throws AirlinesException {

		if (airlineRepository.existsById(airlineId)) {
			return aircraftRepository.getAllAircraftByAirlineId(airlineId);
		} else {
			throw new AirlinesException(ErrorType.AIRLINE_NOT_EXIST);
		}

	}

	/**
	 * Adding new airline entry to the DB
	 */
	
	@Override
	@Transactional
	public void addAirline(Airline airline) throws AirlinesException {

		Destination destination = destinationRepository.findByname(airline.getHome_base_location().getName());
		if (destination != null) {
			airline.setHome_base_location(destination);
		}
		if (airline != null && !airlineRepository.existsByName(airline.getName())) {
			airlineRepository.save(airline);
			logger.info(airline.getName() + " Airline was successfully created ", airline);
		} else {
			logger.error("Airline name is already in use", airline);
			throw new AirlinesException(ErrorType.NAME_EXIST);

		}

	}

	/**
	 * Fetching all the airlines from DB
	 */
	@Override
	public List<Airline> getAllAirlines() throws AirlinesException {
		return airlineRepository.findAll();
	}

	/**
	 * Adding new aircraft entry to DB and link it to specific airline
	 */
	@Override
	@Transactional
	public void addAircraft(Aircraft aircraft, long airlineId) throws AirlinesException {

		Airline airline = airlineRepository.findByid(airlineId);
		if (airline == null) {
			logger.error("Airline not exist");
			throw new AirlinesException(ErrorType.AIRLINE_NOT_EXIST);
		}
		aircraft.setAirline(airline);
		aircraftRepository.save(aircraft);

		airline.addAircraft(aircraft);
		airlineRepository.save(airline);
		logger.info("Aircraft was successfully created ");

	}
	/**
	* Sell aircraft - by providing the id of the aircraft and the id of the buyer airline.
	* validating check -1: if the airline and the aircraft exists in DB; 
	* 2: buyer already have this specific aircraft;
	* 3: aircraft still in possession of the seller;
	* 4: the buyer balance is enough to complete the purchase
	* 				
	*/
	@Override
	@Transactional
	public void sellAircraft(long aircraftId, long airlineId) throws AirlinesException {
		if (!aircraftRepository.existsById(aircraftId) && !airlineRepository.existsById(airlineId)) {
			logger.error("Airline and aircraft not exist");
			throw new AirlinesException(ErrorType.AIRLINE_AND_AIRCRAFT_NOT_EXIST);
		}

		if (!aircraftRepository.existsById(aircraftId)) {
			logger.error("Aircraft not exist in system");
			throw new AirlinesException(ErrorType.AIRCRAFT_NOT_EXIST_IN_SYSTEM);
		}

		if (!airlineRepository.existsById(airlineId)) {
			logger.error("Airline not exist");
			throw new AirlinesException(ErrorType.AIRLINE_NOT_EXIST);
		}

		Aircraft aircraft = aircraftRepository.getById(aircraftId);
		Airline sellerAirline = airlineRepository.getById(aircraft.getAirline().getId());
		Airline buyerAirline = airlineRepository.getById(airlineId);

		if (buyerAirline.getAircrafts().contains(aircraft)) {
			logger.error("Airline already have this aircraft");
			throw new AirlinesException(ErrorType.AIRLINE_AlREADY_HAVE_THIS_AIRCRAFT);
			
		}
		
		if (!sellerAirline.getAircrafts().contains(aircraft)) {
			logger.error("Seller airline doesnt have this aircraft");
			throw new AirlinesException(ErrorType.SELLER_AIRLINE_DOESNT_HAVE_THIS_AIRCRAFT);
		}
		
		double price = aircraft.getPrice() * (1 - aircraft.getTime_in_use() * 0.02);
		System.out.println(price);

		if (buyerAirline.getBalance() >= price) {
			buyerAirline.setBalance(buyerAirline.getBalance() - price);
			sellerAirline.setBalance(sellerAirline.getBalance() + price);
		} else {
			logger.error("Airline dont have enough balance");
			throw new AirlinesException(ErrorType.NOT_ENOUGH_BALANCE);
		}
			
		
		aircraft.setAirline(buyerAirline);
		aircraftRepository.save(aircraft);
		logger.info("Airline: "+sellerAirline.getName()+" Sold Aircraft:"+ aircraft.getId() +" to: "+buyerAirline.getName() +" for:" +price);
	}

	/**
	 * Adding new destination to the DB - Destination object consists of Location object and name of the destination.
	 * validating check -1: if the location coordinates already exist in the DB;
	 * 2: if the destination name already exist in the DB;  
	 */
	@Override
	@Transactional
	public void addDestination(Destination destination) throws AirlinesException {

		List<Destination> locations = destinationRepository.findAll();
		for (Destination dest : locations) {
			if (dest.getLocation().equals(destination.getLocation())) {
				logger.error("Destination location is already in use");
				throw new AirlinesException(ErrorType.LOCATION_EXIST);
			}
		}
		if (!destinationRepository.existsByname(destination.getName())) {

			destinationRepository.save(destination);
			logger.info("Destination was successfully added ");
		} else {
			logger.error("Destination name is already in use");
			throw new AirlinesException(ErrorType.DESTINATION_NAME_EXIST);
		}

	}
	/**
	 * Fetching all the destination from DB
	 */
	@Override
	public List<Destination> getAllDestinations() throws AirlinesException {
		return destinationRepository.findAll();
	}

	/**
	 * Calculate the distance from the home location of the airline to all destinations in the DB. fetching the result as key,value pair Hashtable. 	
	 */
	@Override
	public Map<String, Double> getAirlineDistanceFromAllDestinations(long airlineId) {

		Map<String, Double> mapResult = new Hashtable<String, Double>();
		Airline airline = airlineRepository.findByid(airlineId);
		if (airline == null) {
			throw new AirlinesException(ErrorType.AIRLINE_NOT_EXIST);
		}
		Location airlineLocation = airline.getHome_base_location().getLocation();
		List<Destination> destinations = destinationRepository.findAll();

		for (Destination destination : destinations) {
			if (destination.getId() != airlineLocation.getId()) {
				mapResult.put(destination.getName(),
						Haversine.distance(airlineLocation.getLongitude(), airlineLocation.getLatitude(),
								destination.getLocation().getLongitude(), destination.getLocation().getLatitude()));
			}

		}

		return mapResult;
	}

	/**
	 * Calculate the available destinations according to airline location and fetching a the results in a list;
	 */
	@Override
	public List<Destination> getAvaliableDestinationsForAirline(long airlineId) {

		Airline airline = airlineRepository.findByid(airlineId);
		if (airline == null) {
			throw new AirlinesException(ErrorType.AIRLINE_NOT_EXIST);
		}
		Location airlineLocation = airline.getHome_base_location().getLocation();
		List<Aircraft> airlineAircrafts = airline.getAircrafts();
		if (airlineAircrafts.isEmpty()) {
			throw new AirlinesException(ErrorType.NO_AIRCRAFT_IN_AIRLINE);
		}
		List<Destination> destinations = destinationRepository.findAll();
		List<Destination> destinationResult = new ArrayList<>();
		double maxDisatance = 0;
		for (Aircraft aircraft : airlineAircrafts) {
			if (aircraft.getMax_distance() > maxDisatance) {
				maxDisatance = aircraft.getMax_distance();
			}
		}
		for (Destination destination : destinations) {
			if (Haversine.distance(airlineLocation.getLongitude(), airlineLocation.getLatitude(),
					destination.getLocation().getLongitude(), destination.getLocation().getLatitude()) <= maxDisatance
					&& destination.getId() != airline.getHome_base_location().getId()) {
				destinationResult.add(destination);
			}
		}

		return destinationResult;
	}

	/**
	 * Initialing the DB with dummy values;
	 */
	@Override
	public void init() throws AirlinesException {

		addAirline(new Airline("el-al", 3600000, new Destination("TLV", new Location(32.055401, 34.759499))));
		addAirline(new Airline("Lufthansa", 8000000, new Destination("Frankfurt", new Location(50.040600, 8.556030))));
		addAirline(
				new Airline("British Airways", 4500000, new Destination("London", new Location(51.500000, -0.166670))));
		addAirline(new Airline("American Airlines", 9000000,
				new Destination("New York", new Location(43.121948, -77.668278))));
		addAirline(new Airline("AIR FRANCE", 9000000, new Destination("Paris", new Location(48.856610, 2.351499))));
		addAirline(new Airline("South African Airways", 2000000,
				new Destination("Johannesburg", new Location(-26.135663, 28.241741))));
		addAirline(
				new Airline("Czech Airlines", 5600000, new Destination("Prague", new Location(50.088040, 14.420760))));
		addAirline(new Airline("Israir", 1600000, airlineRepository.findByname("el-al").getHome_base_location()));
		// addAirline(new
		// Airline("Israir",160000000,airlineRepository.findByname("el-al").getHome_base_location()));

		addAircraft(new Aircraft(300000, 15000, 3), 1);
		addAircraft(new Aircraft(100000, 9000, 8), 1);
		addAircraft(new Aircraft(1500000, 300, 2), 2);
		addAircraft(new Aircraft(7800000, 500, 22), 2);
		addAircraft(new Aircraft(2800000, 400, 62), 2);
		addAircraft(new Aircraft(1500000, 5000, 2), 3);
		addAircraft(new Aircraft(7800000, 7000, 22), 3);
		addAircraft(new Aircraft(5660000, 2000, 44), 4);
		addAircraft(new Aircraft(500000, 10000, 7), 4);
		addAircraft(new Aircraft(2300000, 5000, 30), 5);
		addAircraft(new Aircraft(1200000, 8000, 21), 5);
		addAircraft(new Aircraft(4500000, 10000, 7), 6);
		addAircraft(new Aircraft(2300000, 5000, 30), 6);
		addAircraft(new Aircraft(1200000, 8000, 61), 7);
		addAircraft(new Aircraft(1200000, 9000, 21), 7);

		addDestination(new Destination("Bucharest", new Location(44.432250, 26.106260)));
		addDestination(new Destination("Berlin", new Location(52.559690,13.287710)));
		addDestination(new Destination("Rome", new Location(41.891930,12.511330)));
		addDestination(new Destination("Venice", new Location(45.437130,12.332650)));
		addDestination(new Destination("Beirot", new Location(33.888940,35.494420)));
		addDestination(new Destination("Cairo", new Location(30.062630,31.249670)));
		addDestination(new Destination("Los Angeles", new Location(34.052230,-118.243680)));
		addDestination(new Destination("Miami", new Location(25.774270,-80.193660)));
		addDestination(new Destination("Birmingham", new Location(52.477688,-1.894852)));
		addDestination(new Destination("Athens", new Location(37.979450,23.716220)));
		addDestination(new Destination("Crete", new Location(35.155850,24.895020)));
		addDestination(new Destination("Barcelona", new Location(41.388790,2.158990)));
		addDestination(new Destination("Madrid", new Location(40.416705,-3.703582)));
		addDestination(new Destination("Mosscow", new Location(55.752220,37.615560)));
		addDestination(new Destination("Mumbai", new Location(19.072830,72.882610)));
		addDestination(new Destination("Maldives", new Location(3.200000,73.000000)));
		
	}


}
