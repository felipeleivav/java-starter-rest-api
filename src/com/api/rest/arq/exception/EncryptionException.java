package com.api.rest.arq.exception;

public class EncryptionException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public EncryptionException() {
		super();
	}
	
	public EncryptionException(String message) {
		super(message);
	}
	
	public EncryptionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public EncryptionException(Throwable cause) {
		super(cause);
	}
	
}
