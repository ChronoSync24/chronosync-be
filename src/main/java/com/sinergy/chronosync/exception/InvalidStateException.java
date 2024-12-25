package com.sinergy.chronosync.exception;

/**
 * Exception thrown when an invalid state is detected.
 */
public class InvalidStateException extends RuntimeException {
	public InvalidStateException(String message) {
		super(message);
	}
}