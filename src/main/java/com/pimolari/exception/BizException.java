package com.pimolari.exception;

public class BizException extends BaseException {

	/**
	 * @param message
	 * @param cause
	 */
	public BizException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * @param String message
	 * @param String level
	 */
	public BizException(String message, String level) {
		super(message, -1);
		setLevel(level);
	}
	
	/**
	 * @param String message
	 * @param String level
	 * @param int code
	 */
	public BizException(String message, String level, int code) {
		super(message, level, code);
	}
	
	/**
	 * @param DaoException e
	 */
	public BizException(DaoException e) {
		super(e.getMessage(), e.getLevel(), e.getCode());
	}
}
