package com.airlines.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.airlines.entity.Airline;


public interface AirlineRepository extends JpaRepository<Airline, Long>{

	boolean existsByName(String name);
		
	Airline findByid(long id);
	
	Airline findByname(String name);
	
}
