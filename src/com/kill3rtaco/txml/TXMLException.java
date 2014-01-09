package com.kill3rtaco.txml;

public class TXMLException extends RuntimeException {
	
	private static final long	serialVersionUID	= 5006244966177443652L;
	private Throwable			_cause;
	
	public TXMLException(String message) {
		super(message);
	}
	
	public TXMLException(Throwable cause) {
		super(cause.getMessage());
		_cause = cause;
	}
	
	/**
	 * Returns the cause of this exception or null if the cause is uknown or nonexistent
	 * 
	 * @return The cause of this exception
	 */
	public Throwable getCause() {
		return _cause;
	}
	
}
