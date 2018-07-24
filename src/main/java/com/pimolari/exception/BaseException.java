package com.pimolari.exception;

/**
 * Base exception.
 */
@SuppressWarnings("serial")
public class BaseException extends Exception {
	
	public static final String ERROR = "error";
	public static final String WARNING = "warning";
	
	/**
	 * Error code.
	 */
	private int code = -1;
	private String level = BaseException.ERROR;
	private String description = null;
	
	/**
	 * Creates a new instance.
	 *
	 * @param code The error code.
	 */
	public BaseException(int code) {
		this.code = code;
	}

	/**
	 * Creates a new instance.
	 *
	 * @param cause The original cause.
	 * @param code The error code.
	 */
	public BaseException(Throwable cause, int code) {
		super(cause);
		this.code = code;
	}

	/**
	 * Creates a new instance.
	 *
	 * @param message The detail message.
	 * @param code The error code.
	 */
	public BaseException(String message, int code) {
		super(message);
		this.code = code;
	}

	/**
	 * Creates a new instance.
	 *
	 * @param message The detail message.
	 * @param cause The original cause.
	 * @param code The error code.
	 */
	public BaseException(String message, Throwable cause, int code) {
		super(message, cause);
		this.code = code;
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public BaseException(String message, Throwable cause) {
		super(message, cause);
		
	}
	
	/**
	 * @param message
	 */
	public BaseException(String message, String level) {
		super(message);
		this.level = level;
	}
	
	/**
	 * @param message
	 */
	public BaseException(String message, String level, int code) {
		super(message);
		this.level = level;
		this.code = code;
	}
	
	/**
	 * Gets the error code.
	 *
	 * @return The error code.
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * Returns the leves for this exception
	 * @return String
	 */
	public String getLevel() {
		return level;
	}
	
	/**
	 * Sets the level for this exception
	 * @param String level
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	
	/**
	 * Returns a json formated exception message.
	 * @return String
	 */
	public String getFormattedMessage() {
		return this.getFormatedMessage("json");
	}
	
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns a formated exception message. Two formats are supported: "json" and "xml"
	 * @param String format json or xml
	 * @return String
	 */
	public String getFormatedMessage(String format) {
		
		if (format != null) {
			StringBuilder output = new StringBuilder();
			
			if("json".equals(format.toLowerCase())) {
				output.append("{\"");
				output.append(this.level);
				output.append("\":\"");
				output.append(super.getMessage());
				output.append("\"}");
			}
			
			if ("xml".equals(format.toLowerCase())) {
				output.append("<" + this.level + ">");
				output.append(super.getMessage());
				output.append("</" + this.level + ">");
			}
			
			return output.toString();
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		String className = getClass().getName();
		String message = getLocalizedMessage();

		StringBuilder buffer = new StringBuilder();
		buffer.append(className);
		if (message != null) buffer.append(": ").append(message);
		if (this.code != -1) buffer.append(" (#").append(code).append("#)");
		return buffer.toString();
	}
}
