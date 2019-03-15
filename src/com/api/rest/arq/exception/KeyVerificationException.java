package com.api.rest.arq.exception;

public class KeyVerificationException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public KeyVerificationException() {
		super();
	}
	
	public KeyVerificationException(String message) {
		super(message);
	}
	
	public KeyVerificationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public KeyVerificationException(Throwable cause) {
		super(cause);
	}
	
}
