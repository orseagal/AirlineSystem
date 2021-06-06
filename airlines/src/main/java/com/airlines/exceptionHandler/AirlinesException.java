package com.airlines.exceptionHandler;


import java.sql.SQLException;

public class AirlinesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ErrorType errorType;

	public AirlinesException(ErrorType errorType) {
		this.errorType = errorType;

	}

	public AirlinesException(String message, Throwable exception, ErrorType errorType) {
		super(message, exception);
		this.errorType = errorType;
	}

	public AirlinesException(ErrorType errorType, Throwable exception) {
		super(exception);
		this.errorType = errorType;
	}

	public AirlinesException(ErrorType errorType, SQLException sqlException) {
		super(sqlException);
		this.errorType = errorType;
	}

	public AirlinesException(String message, ErrorType errorType) {
		super(message);
		this.errorType = errorType;

	}
	
	public AirlinesException(String message,Throwable exception) {
		super(message,exception);
	

	}
	public AirlinesException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AirlinesException(String message, Exception exception) {
		super(message,exception);
	}

	public ErrorType getErrorType() {
		return errorType;
	}

}
