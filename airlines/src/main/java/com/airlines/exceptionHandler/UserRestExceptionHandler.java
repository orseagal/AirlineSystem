package com.airlines.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class UserRestExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<UserErrorResponse> handleException(AirlinesException exc) {

		UserErrorResponse error = new UserErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		//error.setMessage(exc.getMessage());
		error.setMessage(exc.getErrorType().getErrorClientMessage());
		error.setTimeStamp(System.currentTimeMillis());

		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);

	}
	
	
	@ExceptionHandler
	public ResponseEntity<UserErrorResponse> handleException(Exception exc) {

		UserErrorResponse error = new UserErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

	}
	
}
