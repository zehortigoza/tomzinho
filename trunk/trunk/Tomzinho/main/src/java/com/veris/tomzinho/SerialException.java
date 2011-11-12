package com.veris.tomzinho;

public class SerialException extends Exception {
	private static final long serialVersionUID = 1L;

	public enum Error {
	    PORT_IN_USE, PORT_NOT_FOUND, UNSUPORTED_CONFIGURATIONS, IO
	}
	
	private Error Error;
	private String Message;
	
	public Error getError() {
		return Error;
	}
	
	public String getMessage() {
		return Message;
	}
	
	public SerialException(Error error, String message) {
		Error = error;
		Message = message;
	}
	
	public SerialException(Error error) {
		this(error,null);
	}
}
