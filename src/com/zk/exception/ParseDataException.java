package com.zk.exception;

public class ParseDataException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ParseDataException() {
		
	}
	
	public ParseDataException(String message) {
		super(message);
	}
	
	public ParseDataException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ParseDataException(Throwable cause) {
		super(cause);
	}

	
}
