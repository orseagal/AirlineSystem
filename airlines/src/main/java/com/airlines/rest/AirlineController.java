package com.airlines.rest;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.airlines.entity.Aircraft;
import com.airlines.entity.Airline;
import com.airlines.entity.Destination;
import com.airlines.exceptionHandler.AirlinesException;
import com.airlines.service.AirlineService;

@RestController
@RequestMapping("/airline")
public class AirlineController {

	@Autowired
	AirlineService airlineService;

	@PostMapping("/addAirline")
	public ResponseEntity<String> addAirline(@RequestBody Airline airline) throws AirlinesException {
		airlineService.addAirline(airline);
		return new ResponseEntity<>("Airline Registration Successful", HttpStatus.OK);
	}

	@PostMapping("/addAircraft/{airlineId}")
	public ResponseEntity<String> addAircraft(@RequestBody Aircraft aircraft, @PathVariable Long airlineId)
			throws AirlinesException {
		airlineService.addAircraft(aircraft, airlineId);
		return new ResponseEntity<>("Aircraft Registration Successful", HttpStatus.OK);
	}

	@GetMapping("/getAllAircrafts")
	public List<Aircraft> getAllAircrafts() throws AirlinesException {
		return airlineService.getAllAircrafts();

	}

	@GetMapping("/getAirlineAircrafts/{airlineId}")
	@ResponseBody
	public List<Aircraft> getAirlineAircrafts(@PathVariable Long airlineId) throws AirlinesException {
		return airlineService.getAirlineAircrafts(airlineId);
	}

	@PutMapping("/sellAircraft/{aircraftId}/{airlineId}")
	public ResponseEntity<String> sellAircraft(@PathVariable long aircraftId, @PathVariable long airlineId) {
		airlineService.sellAircraft(aircraftId, airlineId);
		return new ResponseEntity<>("Aircraft Successfully Sold", HttpStatus.OK);
	}

	@GetMapping("/getAllAirlines")
	public List<Airline> getAllAirlines() throws AirlinesException {

		return airlineService.getAllAirlines();

	}

	@GetMapping("/getAirlineDistanceFromAllDestinations/{airlineId}")
	public Map<String, Double> getairlineDistanceFromAllDestinations(@PathVariable long airlineId)
			throws AirlinesException {
		return airlineService.getAirlineDistanceFromAllDestinations(airlineId);

	}

	@GetMapping("/getAvaliableDestinationsForAirline/{airlineId}")
	public List<Destination> getAvaliableDestinationsForAirline(@PathVariable long airlineId) throws AirlinesException {
		return airlineService.getAvaliableDestinationsForAirline(airlineId);
	}

	@GetMapping("/getAllDestinations")
	public List<Destination> getAllDestinations() throws AirlinesException {
		return airlineService.getAllDestinations();
	}

	@GetMapping("/init")
	public ResponseEntity<String> init() {
		airlineService.init();
		return new ResponseEntity<>("Airine System initialization Successful", HttpStatus.OK);

	}

}
