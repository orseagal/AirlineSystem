package com.airlines.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "aircraft")
public class Aircraft {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AIRCRAFT_ID")
	private long id;
	@Column(name = "price", nullable = false)
	private double price;

	@Column(name = "max_distance", nullable = false)
	private double max_distance;

	@Column(name = "time_in_use", nullable = false)
	private int time_in_use;

	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH, })
	@JoinColumn(name = "AIRLINE_ID")
	@JsonBackReference
	private Airline airline;

	 @Version
	 private long version;
	
	public Aircraft(double price, double max_distance, int time_in_use) {
		super();
		this.price = price;
		this.max_distance = max_distance;
		this.time_in_use = time_in_use;
		
	}

	public Aircraft() {
	};

	public long getId() {
		return id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getMax_distance() {
		return max_distance;
	}

	public void setMax_distance(double max_distance) {
		this.max_distance = max_distance;
	}

	public int getTime_in_use() {
		return time_in_use;
	}

	public void setTime_in_use(int time_in_use) {
		this.time_in_use = time_in_use;
	}

	public Airline getAirline() {
		return airline;
	}

	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	
	
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Aircraft [id=" + id + ", price=" + price + ", max_distance=" + max_distance + ", time_in_use="
				+ time_in_use + " ]";
	}

}
