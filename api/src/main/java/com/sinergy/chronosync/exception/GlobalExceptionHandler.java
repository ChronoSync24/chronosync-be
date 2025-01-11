package com.sinergy.chronosync.exception;

import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles ServiceException globally and returns a 400 Bad Request response with the exception message.
	 *
	 * @param e the exception to handle
	 * @return a 400 Bad Request response with the exception message
	 */
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<String> handleServiceException(ServiceException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	/**
	 * Handles InvalidStateException globally and returns a 400 Bad Request response with the exception message.
	 *
	 * @param e the exception to handle
	 * @return a 400 Bad Request response with the exception message
	 */
	@ExceptionHandler(InvalidStateException.class)
	public ResponseEntity<String> handleInvalidStateException(InvalidStateException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	/**
	 * Handles UserNotFoundException globally and returns a 404 Not Found response with the exception message.
	 *
	 * @param e the exception to handle
	 * @return a 404 Not Found response with the exception message
	 */
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
}
