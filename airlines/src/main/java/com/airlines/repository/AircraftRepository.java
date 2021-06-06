package com.airlines.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.airlines.entity.Aircraft;

public interface AircraftRepository  extends JpaRepository<Aircraft, Long>{

	
	List<Aircraft> getAllAircraftByAirlineId(long airlineId);
	
		
	@Query(value = "SELECT * FROM aircraft ", nativeQuery = true)
	List<Aircraft> getAllAircrafts();
	
	
}
