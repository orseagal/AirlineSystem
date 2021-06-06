package com.airlines.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "airline")
public class Airline {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AIRLINE_ID")
	private long id;
	@Column(name = "name", unique = true)
	String name;

	@Column(name = "balance", nullable = false)
	private double balance;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "home_base_location", nullable = false)
	private Destination home_base_location;

	@OneToMany(mappedBy = "airline", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JsonManagedReference
	private List<Aircraft> aircrafts = new ArrayList<>();

	@Version
	private long version;

	public Airline(String name, double balance, Destination home_base_location) {
		super();
		this.name = name;
		this.balance = balance;
		this.home_base_location = home_base_location;

	}

	public Airline() {
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Destination getHome_base_location() {
		return home_base_location;
	}

	public void setHome_base_location(Destination home_base_location) {
		this.home_base_location = home_base_location;
	}

	public List<Aircraft> getAircrafts() {
		return aircrafts;
	}

	public void setAircrafts(List<Aircraft> aircrafts) {
		this.aircrafts = aircrafts;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public void addAircraft(Aircraft aircraft) {

		List<Aircraft> list = getAircrafts();
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(aircraft);
		setAircrafts(list);
	}

	@Override
	public String toString() {
		return "Airline [id=" + id + ", name=" + name + ", balance=" + balance + ", home_base_location="
				+ home_base_location + ", aircrafts=" + aircrafts + "]";
	}

}
