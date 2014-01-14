package com.kill3rtaco.txml;

/*
 * TXML
 * Copyright (c) 2014 Caleb Downs, aka KILL3RTACO 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Represents an exception thrown for any reason by TXML. This is a RuntimeException, so it may be thrown
 * without of the need of a try/catch. Check the javadocs of methods to see when and if they throw this exception
 * @author KILL3RTACO
 *
 */
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
