package com.airlines.exceptionHandler;

public enum ErrorType {
	NAME_EXIST(904, "NAME_EXIST", false),
	DESTINATION_NAME_EXIST(904, "DESTINATION_NAME_EXIS", false),
	LOCATION_EXIST(904, "LOCATION_EXIST", false),
	AIRLINE_NOT_EXIST(904, "AIRLINE_NOT_EXIST", false),
	AIRCRAFT_NOT_EXIST_IN_SYSTEM(904, "AIRCRAFT_NOT_EXIST_IN_SYSTEM", false),
	AIRCRAFT_NOT_EXIST_IN_AIRLINE(904, "AIRCRAFT_NOT_EXIST_IN_AIRLINE", false),
	NO_AIRCRAFT_IN_AIRLINE(904, "NO_AIRCRAFT_IN_AIRLINE", false),
	AIRLINE_AND_AIRCRAFT_NOT_EXIST(904, "AIRLINE_AND_AIRCRAFT_NOT_EXIST", false),
	AIRLINE_AlREADY_HAVE_THIS_AIRCRAFT(904, "AIRLINE_AlREADY_HAVE_THIS_AIRCRAFT", false),
	SELLER_AIRLINE_DOESNT_HAVE_THIS_AIRCRAFT(904, "SELLER_AIRLINE_DOESNT_HAVE_THIS_AIRCRAFT", false),
	NOT_ENOUGH_BALANCE(904, "NOT_ENOUGH_BALANCE", false),
	NO_MATCHES(905, "No exact matches found", false);


	private final String errorClientMessage;
	private final int error;
	private final boolean logDataBase;

	private ErrorType(int error, String errorClientMessage, boolean logDataBase) {
		this.error = error;
		this.errorClientMessage = errorClientMessage;
		this.logDataBase = logDataBase;
	}

	public boolean isLogDataBase() {
		return logDataBase;
	}

	public String getErrorClientMessage() {
		return errorClientMessage;
	}

	public int getError() {
		return error;
	}

}
