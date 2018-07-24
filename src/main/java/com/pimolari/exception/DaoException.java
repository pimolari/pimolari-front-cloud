package com.pimolari.exception;

public class DaoException extends BaseException {

		
	/**
	 * @param message
	 * @param cause
	 */
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * @param message
	 */
	public DaoException(String message, String level) {
		super(message, -1);
		setLevel(level);
	}
	
	/**
	 * @param message
	 */
	public DaoException(String message, String level, int code) {
		super(message, level, code);
	}
}
