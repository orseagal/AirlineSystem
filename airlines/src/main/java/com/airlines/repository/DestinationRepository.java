package com.airlines.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.airlines.entity.Destination;

public interface DestinationRepository  extends JpaRepository<Destination, Long>{

	boolean existsByname(String name);
	
	Destination findByname(String name);
}
